package threeq.toreachyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListner;
    private ChatAdapter mAdapter;
    private ListView mListView;
    private EditText mEditText;
    private Button mButton;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //플로팅액션버튼 클릭이벤트
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(i);
            }
        });

        //파이어베이스에서 데이터 불러와서 뷰 초기화
        initView();
        initFirebaseDatabase();
        userName = "익명" + new Random().nextInt(5000);

        //메시지 입력 이벤트 처리. 버튼이 아직 없어서 주석 처리
        /*mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditText.getText().toString();
                if(!TextUtils.isEmpty(message)){
                    if (!TextUtils.isEmpty(message)) {
                        mEditText.setText("");
                        ChatData chatData = new ChatData();
                        chatData.userName = userName;
                        chatData.message = message;
                        chatData.time = System.currentTimeMillis();
                        mDatabaseReference.push().setValue(chatData);
                    }
                }
            }
        });*/
    }

    private void initView(){
        mListView = (ListView) findViewById(R.id.list_message);
        mAdapter = new ChatAdapter(this, 0);
        mListView.setAdapter(mAdapter);
    }

    private void initFirebaseDatabase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("message");
        mChildEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.firebaseKey = dataSnapshot.getKey();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //onDestroy 단계에서 파이어베이스 참조 해제
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseReference.removeEventListener(mChildEventListner);
    }
}
