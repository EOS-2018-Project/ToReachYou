package threeq.toreachyou;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class WriteActivity extends AppCompatActivity {

    Button register;
    EditText mTitle, mMessage;
    String userName, tag;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    RadioGroup radioGroup;
    RadioButton family, friend, lover, company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mTitle = (EditText) findViewById(R.id.title);
        mMessage = (EditText) findViewById(R.id.message);
        userName = "너에게" + new Random().nextInt(100);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        family = (RadioButton)findViewById(R.id.family);
        friend = (RadioButton)findViewById(R.id.friend);
        lover = (RadioButton)findViewById(R.id.lover);
        company = (RadioButton)findViewById(R.id.company);

        //보고 있던 게시판(?) 위치가 디폴트 게시판(라디오버튼 체크)
        switch (MainActivity.tab_pos){
            case 0:
                radioGroup.check(R.id.family);
                break;
            case 1:
                radioGroup.check(R.id.friend);
                break;
            case 2:
                radioGroup.check(R.id.lover);
                break;
            case 3:
                radioGroup.check(R.id.company);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //등록 버튼 눌렀을 때 이벤트 처리
        if (id == R.id.action_register) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            //라디오버튼 체크에 따른 태그 부여
            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.family:
                    tag = "family";
                    break;
                case R.id.friend:
                    tag = "friend";
                    break;
                case R.id.lover:
                    tag = "lover";
                    break;
                case R.id.company:
                    tag = "company";
                    break;
            }
            mDatabaseReference = mFirebaseDatabase.getReference(tag);

            String message = mMessage.getText().toString();
            String title = mTitle.getText().toString();

            //제목과 내용이 둘 다 비어있지 않으면 실행
            if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(title)) {

                //채팅데이터 인스턴스화 -> 멤버 변수 할당
                ChatData chatData = new ChatData();
                chatData.userName = userName;
                chatData.title = title;
                chatData.message = message;
                chatData.like = 0;

                //글을 올리는 시점의 시간 기록
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
                chatData.time = simpleDateFormat.format(date);

                //라디오버튼 체크에 따른 태그 부여
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.family:
                        chatData.tag = "family";
                        break;
                    case R.id.friend:
                        chatData.tag = "friend";
                        break;
                    case R.id.lover:
                        chatData.tag = "lover";
                        break;
                    case R.id.company:
                        chatData.tag = "company";
                        break;
                }
                //푸시
                mDatabaseReference.push().setValue(chatData);

                finish();
            } else {
                Toast.makeText(WriteActivity.this, "제목과 내용을 채워주세요", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //뒤로키를 눌렀을 때 한번 더 확인하는 다이얼로그 띄우기
        if((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(WriteActivity.this);
            d.setMessage("작성중인 내용을 저장하지 않고 나가시겠습니까?");

            d.setPositiveButton("확인",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WriteActivity.this.finish();
                }
            });

            d.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
