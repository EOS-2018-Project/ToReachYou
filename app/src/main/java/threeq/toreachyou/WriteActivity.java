package threeq.toreachyou;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class WriteActivity extends AppCompatActivity {

    Button register;
    EditText mTitle, mMessage;
    String userName;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        register = (Button) findViewById(R.id.register);
        mTitle = (EditText) findViewById(R.id.title);
        mMessage = (EditText) findViewById(R.id.message);
        userName = "익명" + new Random().nextInt(1000);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mDatabaseReference = mFirebaseDatabase.getReference("message");

                String message = mMessage.getText().toString();
                String title = mTitle.getText().toString();

                if(!TextUtils.isEmpty(message) && !TextUtils.isEmpty(title)){

                    mMessage.setText("");
                    ChatData chatData = new ChatData();
                    chatData.userName = userName;
                    chatData.title = title;
                    chatData.message = message;
                    chatData.time = System.currentTimeMillis();
                    mDatabaseReference.push().setValue(chatData);

                    finish();
                }

                else{
                    Toast.makeText(WriteActivity.this, "제목과 내용을 채워주세요", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
