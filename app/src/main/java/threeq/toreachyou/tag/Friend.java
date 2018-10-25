package threeq.toreachyou.tag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.concurrent.ConcurrentHashMap;

import threeq.toreachyou.ChatAdapter;
import threeq.toreachyou.ChatData;
import threeq.toreachyou.R;
import threeq.toreachyou.ViewActivity;

public class Friend extends Fragment {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    ChildEventListener mChildEventListner;
    ChatAdapter mAdapter, nAdapter;
    ListView mListView;
    ViewGroup rootGroup;
    ChatData data;

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
                String key = mAdapter.getItem(position).firebaseKey;
                String tag = mAdapter.getItem(position).tag;
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
        mDatabaseReference = mFirebaseDatabase.getReference("friend");
        mChildEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                data = dataSnapshot.getValue(ChatData.class);
                if(data.tag.equals("friend"))
                    data.firebaseKey = dataSnapshot.getKey();
                mAdapter.add(data);
                mListView.smoothScrollToPosition(mAdapter.getCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //어레이어댑터에서는 아이템의 추가,삽입,삭제가 일어나야 notifyDataSetChanged가 작동한다고 해요
                //아이템 내부의 정보 변화는 감지할 수가 없어서.. 몇시간 삽질 끝에 결국 없앴다가 다시 만들기를 선택했어요
                //성능상 당연히 비효율적이겠지만 다루는 데이터가 매우 가벼운 편이므로 실질적 타격은 없을거라 믿고 있어요ㅎㅎ

                //위 주석은 흔적기관.(고민의 흔적을 지우기 아까워서) -> 계속 삽질하다가 파이어베이스 구조를 깨달아서(?)
                //원래 데이터 삭제하고 해당 인덱스에 새 데이터 삽입하는 방식으로 바꾸려는 시도ing
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                int index = 0;
                for(int i=0; i<mAdapter.getCount(); i++){
                    if(mAdapter.getItem(i) == chatData){
                        index = i; break;
                    }
                }
                Log.d("index:", Integer.toString(index));// TODO: 위에 반복문 돌려도 로그보면 무조건 0만 리턴됨
                mAdapter.remove(mAdapter.getItem(index));
                mAdapter.insert(chatData, index);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
}
