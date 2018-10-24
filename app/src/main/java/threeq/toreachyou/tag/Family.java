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

public class Family extends Fragment {

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
                i.putExtra("message",message);
                i.putExtra("title",title);
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
        mDatabaseReference = mFirebaseDatabase.getReference("family");
        mChildEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.firebaseKey = dataSnapshot.getKey();
                if(chatData.tag.equals("family"))
                    mAdapter.add(chatData);
                mListView.smoothScrollToPosition(mAdapter.getCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
}
