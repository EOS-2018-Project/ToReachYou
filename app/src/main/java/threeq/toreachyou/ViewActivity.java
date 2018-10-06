package threeq.toreachyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent i = getIntent();
        String message = i.getExtras().getString("message");
        String title = i.getExtras().getString("title");

        TextView textView = (TextView)findViewById(R.id.View_tvcontent);
        TextView textView1 = (TextView)findViewById(R.id.View_tvtitle);
        textView.setText(message);
        textView1.setText(title);
    }
}
