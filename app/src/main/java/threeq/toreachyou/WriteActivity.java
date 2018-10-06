package threeq.toreachyou;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class WriteActivity extends AppCompatActivity {

    Button register;
    EditText mTitle, mMessage;
    String userName;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    Button family, friend, lover, company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mTitle = (EditText) findViewById(R.id.title);
        mMessage = (EditText) findViewById(R.id.message);
        userName = "익명" + new Random().nextInt(1000);
        family = (RadioButton)findViewById(R.id.family);
        friend = (RadioButton)findViewById(R.id.friend);
        lover = (RadioButton)findViewById(R.id.lover);
        company = (RadioButton)findViewById(R.id.company);

        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mMessage.setTag("family");
            }
        });
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mMessage.setTag("friend");
            }
        });
        lover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mMessage.setTag("lover");
            }
        });
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mMessage.setTag("company");
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register) {

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference("message");

            String message = mMessage.getText().toString();
            String title = mTitle.getText().toString();

            if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(title)) {

                mMessage.setText("");
                ChatData chatData = new ChatData();
                chatData.userName = userName;
                chatData.title = title;
                chatData.message = message;
                chatData.time = System.currentTimeMillis();
                mDatabaseReference.push().setValue(chatData);

                finish();
            } else {
                Toast.makeText(WriteActivity.this, "제목과 내용을 채워주세요", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
