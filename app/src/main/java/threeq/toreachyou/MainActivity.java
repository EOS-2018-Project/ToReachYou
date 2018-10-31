package threeq.toreachyou;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    //탭레이아웃+뷰페이저
    TabLayout tabLayout = null;
    ViewPager viewPager = null;
    MainTabPagerAdapter pagerAdapter = null;
    CoordinatorLayout coordinatorLayout;
    static int tab_pos;
    static String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //탭레이아웃 + 뷰페이저 설정
        tabLayout = (TabLayout) findViewById(R.id.main_tab);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        tabLayout.setupWithViewPager(viewPager);

        //페이지어댑터 인스턴스화. 뷰페이저의 어댑토로 설정
        pagerAdapter = new MainTabPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        //SnackBar 사용을 위한 CoordinatorLayout 초기화
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        //UUID(deviceID) 세팅
        getUUID();

        //오늘의 질문 다이얼로그
        Dialog dialog = new Question(this);
        dialog.show();

        //페이지가 바꼈을 때 이벤트리스너
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //탭을 클릭했을 때 이벤트리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_pos = tab.getPosition();
                viewPager.setCurrentItem(tab_pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //플로팅액션버튼 클릭이벤트
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WriteActivity.class);
                startActivity(i);
            }
        });

    }

    //뒤로가기 버튼을 두번 연속으로 눌러야 종료되게끔 하는 메소드
    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Snackbar snack = Snackbar.make(coordinatorLayout, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Snackbar.LENGTH_SHORT);
            View view = snack.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snack.show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }

    //좋아요 중복 방지, 추후 구현할(?) 메시지 삭제 등을 위한 사용자 식별을 위해 자바에서 제공하는 랜덤 UUID를 사용
    private void getUUID() {
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("UUID", Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString("UUID_KEY", null);

        if(uuid == null){
            String uuidStr = UUID.randomUUID().toString(); //랜덤 생성
            String newId = uuidStr.substring(0,8); // 8글자로 잘라서 새 아이디로 배정

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UUID_KEY", newId); // 한번 생성했으면 계속 써야되니 sharedpreferences로 저장.
            editor.apply();
        }
        Log.d("uuid", uuid);
    }
}
