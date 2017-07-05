package com.app.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myapplication.Server.HTTPAsuncTask;

import java.util.concurrent.ExecutionException;

public class login extends AppCompatActivity {
    HTTPAsuncTask task = null;

    EditText edt_id, edt_pw; //ID,PW
    Button btn_login;        //login버튼
    TextView tv_member;      //회원가입

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_id = (EditText) findViewById(R.id.edt_id);
        edt_pw = (EditText) findViewById(R.id.edt_pw);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_member = (TextView) findViewById(R.id.tv_member);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = " "; //서버에서 받아온 값

                String id = edt_id.getText().toString();
                String pw =  edt_pw.getText().toString();

                connectCheck();

                if(!id.equals("")||pw.equals("")){

                try {
                    //값 bundle에 넣고 서버로 보냄
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("action", "login");
                    bundle.putSerializable("userid", edt_id.getText().toString());
                    bundle.putSerializable("userid", edt_pw.getText().toString());
                    task = new HTTPAsuncTask(getApplicationContext(), bundle);
                    result = task.execute("").get(); //서버에서 값을 받아옴

                    if (result.equals("1")) { //아이디, 비번 모두 맞을 경우 저장
                        SharedPreferences settings = getSharedPreferences("info", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor prefEditor = settings.edit();
                        prefEditor.putString("userid", edt_id.getText().toString());   //id 저장
                        prefEditor.commit();    //반영

                        //Main 화면으로 이동
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    } else {   //둘 중 하나라도 틀릴 경우 토스트로 경고 보여주고, 입력창 reset
                        Toast.makeText(getApplicationContext(), "둘 중 하나 잘못 입력하셨습니다. 다시 입력해주세요.", Toast.LENGTH_LONG);
                        edt_id.setText("");
                        edt_pw.setText("");
                    }
                }  catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else {
                    Toast.makeText(getApplicationContext(), "아이디나 비밀번호를 입력해 주세요", Toast.LENGTH_LONG);
                }
            }
        });

        tv_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Membership.class));
            }
        });
    }

    protected void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }
    //웹에서 데이터를 가져오기 전에 먼저 네트워크 상태부터 확인
    public void connectCheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //Toast.makeText(this,"네트워크 연결중입니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"네트워크 상태를 확인하십시오", Toast.LENGTH_SHORT).show();
        }
    }
}