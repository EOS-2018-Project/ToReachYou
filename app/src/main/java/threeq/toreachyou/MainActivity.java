package threeq.toreachyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    //탭레이아웃+뷰페이저
    TabLayout tabLayout = null;
    ViewPager viewPager = null;
    MainTabPagerAdapter pagerAdapter = null;

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

        //페이지가 바꼈을 때 이벤트리스너
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //탭을 클릭했을 때 이벤트리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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

       /* //파이어베이스에서 데이터 불러와서 뷰 초기화
        initView();
        initFirebaseDatabase();*/

        /*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ViewActivity.class);
                String message =  mAdapter.getItem(position).message;
                String title = mAdapter.getItem(position).title;
                i.putExtra("message",message);
                i.putExtra("title",title);
                startActivity(i);
            }
        });*/
    }

    /*private void initView(){
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



    //onDestroy 단계에서 파이어베이스 참조 해제
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseReference.removeEventListener(mChildEventListner);
    }*/
}
