package threeq.toreachyou.tag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import threeq.toreachyou.ChatAdapter;
import threeq.toreachyou.ChatData;
import threeq.toreachyou.R;
import threeq.toreachyou.ViewActivity;

public class Company extends Fragment {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    ChildEventListener mChildEventListner;
    ChatAdapter mAdapter;
    ListView mListView;
    ViewGroup rootGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootGroup = (ViewGroup) inflater.inflate(R.layout.list_main, container, false);
        //파이어베이스에서 데이터 불러와서 뷰 초기화
        initView();
        initFirebaseDatabase();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), ViewActivity.class);
                String message =  mAdapter.getItem(position).message;
                String title = mAdapter.getItem(position).title;
                String tag = mAdapter.getItem(position).tag;
                String key = mAdapter.getItem(position).firebaseKey;
                int like = mAdapter.getItem(position).like;

                //인텐트 엑스트라 넘겨주기
                i.putExtra("message",message);
                i.putExtra("title",title);
                i.putExtra("like", like);
                i.putExtra("key", key);
                i.putExtra("tag", tag);
                startActivity(i);
            }
        });
        return rootGroup;
    }

    private void initView(){
        mListView = (ListView) rootGroup.findViewById(R.id.list_message);
        mAdapter = new ChatAdapter(getContext(), 0);
        mListView.setAdapter(mAdapter);
    }

    private void initFirebaseDatabase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("company");

        mChildEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.firebaseKey = dataSnapshot.getKey();
                if(chatData.tag.equals("company"))
                    mAdapter.insert(chatData, 0); // 최신 내용이 위로 올라오도록 0번째 인덱스에 삽입
                mListView.smoothScrollToPosition(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //어레이어댑터에서는 아이템의 추가,삽입,삭제가 일어나야 notifyDataSetChanged가 작동한다고 해요
                //아이템 내부의 정보 변화는 감지할 수가 없어서.. 몇시간 삽질 끝에 결국 없앴다가 다시 만들기를 선택했어요
                //성능상 당연히 비효율적이겠지만 다루는 데이터가 매우 가벼운 편이므로 실질적 타격은 없을거라 믿고 있어요ㅎㅎ

                //위에는 멍청해서 그렇게 했었던거고 삽질삽질삽질 끝에 좀 더 똑똑한 방법으로 개편.. 뿌듷
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                String firebaseKey = dataSnapshot.getKey();
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    if(mAdapter.getItem(i).firebaseKey != null){
                        if (mAdapter.getItem(i).firebaseKey.equals(firebaseKey)) {
                            mAdapter.getItem(i).like = chatData.like;
                            //mAdapter.remove(mAdapter.getItem(i));
                            //mAdapter.insert(chatData, i);
                            //mAdapter.notifyDataSetChanged();
                            //mAdapter = (ChatAdapter) mListView.getAdapter();
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    /*else{
                        mAdapter.notifyDataSetChanged();
                        mAdapter = (ChatAdapter) mListView.getAdapter();
                        if (mAdapter.getItem(i).firebaseKey.equals(firebaseKey)) {
                            mAdapter.remove(mAdapter.getItem(i));
                            mAdapter.insert(chatData, i);
                            break;
                        }
                    }*/
                }
                mListView.smoothScrollToPosition(0);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    if (mAdapter.getItem(i).firebaseKey.equals(firebaseKey)) {
                        mAdapter.remove(mAdapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListner);
    }



    //onDestroy 단계에서 파이어베이스 참조 해제
    @Override
    public void onDestroy(){
        super.onDestroy();
        mDatabaseReference.removeEventListener(mChildEventListner);
    }

    public void onRestart(){

    }
}
