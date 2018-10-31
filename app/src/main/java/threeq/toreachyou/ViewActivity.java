package threeq.toreachyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewActivity extends AppCompatActivity{

    String title, message, key, tag;
    int like;

    TextView like_count;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    String tempID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        //각 태그 클래스에서 넘긴 intent extra 가져오기
        Intent i = getIntent();
        title = i.getExtras().getString("title");
        message = i.getExtras().getString("message");
        key = i.getExtras().getString("key");
        tag = i.getExtras().getString("tag");
        like = i.getExtras().getInt("like");


        //파이어베이스 참조
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(tag);

        //툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //타이틀은 툴바의 타이틀로 설정
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //메시지는 텍스트뷰로 설정
        TextView textView = (TextView) findViewById(R.id.msg);
        textView.setText(message);

        //좋아요 수 설정
        like_count = (TextView) findViewById(R.id.txt_like);
        like_count.setText(String.valueOf(like));

        Button like_click = (Button) findViewById(R.id.btn_like);
        like_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                like++;
                like_count.setText(String.valueOf(like));
                mDatabaseReference.child(key).child("like").setValue(like);


                //파이어베이스에 좋아요한 사람의 id 푸시...하고 받아와야하는데 받아오는게 너무 어려워서 보류
                String user = "user" + String.valueOf(like);
                mDatabaseReference.child(key).child("like_user").child(user).push().setValue(MainActivity.uuid);


            }
        });

        Button share = (Button) findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                Intent chooser = Intent.createChooser(intent, "너에게 닿기를..");
                startActivity(chooser);
            }
        });

        //데이터 변동시 호출(좋아요한 사람 id 푸시 됐을 때 발동)
        /*FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(int i=0; i < like; i++){
                    String user = "user" + String.valueOf(i);
                    dataSnapshot.child(key).child("like_user").child(user).getValue(ChatData.class);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



    }

    //툴바의 뒤로키 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
