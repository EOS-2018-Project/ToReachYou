package threeq.toreachyou;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Question extends Dialog implements android.view.View.OnClickListener
{
    public Activity c;
    public Dialog d;
    public Button ok;

    String text;

    TextView tv;

    public Question(Activity a)
    {
        super(a);
        this.c = a;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);

        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);

        tv = (TextView)findViewById(R.id.yh);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork()
                .build());

        try {
            StringBuffer sBuffer = new StringBuffer();
            String adress = "http://dookong.ivyro.net/question.txt";
            //자바 API를 이용한 http통신 -> 앱 업데이트 없이 조작할 수 있는 텍스트 받아오는 목적
            URL url = new URL(adress); //URL 클래스는 서버의 URL 정보를 표현
            HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //HttpURLConnection 클래스는 실제 HTTP 연결을 요청

            if(conn != null){
                conn.setConnectTimeout(10000); //연결 타임아웃 10초
                conn.setReadTimeout(10000); // 읽기 타임아웃 10초
                conn.setUseCaches(false); //캐시 사용 안함
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    while(true){
                        String line = br.readLine();
                        if(line==null){
                            break;
                        }
                        sBuffer.append(line+ "\n");
                    }
                    br.close();
                    conn.disconnect();
                }
            }
            text = sBuffer.toString();
            tv.setText(text);
        } catch (Exception e) {
        }

    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.ok:
                dismiss();
                break;
        }
    }
}