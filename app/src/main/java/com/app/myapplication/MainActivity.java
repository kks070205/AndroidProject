package com.app.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

    FrameLayout main_ui; //메인화면
    ListView menu_list; //부가 컨텐츠, 메뉴list
    Button test_login;  //로그인 버튼(test용)

    String[] item = {"web","comm","c"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_ui = (FrameLayout)findViewById(R.id.main_ui);
        menu_list = (ListView)findViewById(R.id.menu_list);
        test_login = (Button)findViewById(R.id.test_login);

        menu_list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, item));
        menu_list.setOnItemClickListener(new DrawerItemClickListener());

        test_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
    }

    //리스트뷰 선택
    //fragment 실행
    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
            @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FragmentTransaction ft;
            Fragment fragment;

            switch (position) {
                case 0:
                    FragmentManager fm = getFragmentManager();
                    fragment = new Web();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.main_ui,fragment);

                    break;
                case 1:
                    FragmentManager fm1= getFragmentManager();
                    fragment= new comm();
                    ft = fm1.beginTransaction();
                    ft.replace(R.id.main_ui,fragment);
                    break;
            }
        }
    }
}
