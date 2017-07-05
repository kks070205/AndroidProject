package com.app.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.myapplication.Server.HTTPAsuncTask;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class Membership extends AppCompatActivity {

    TextView edtMember_id, edtMember_pw, edtMember_pw_re;
    Button btnMember_OK;
    HTTPAsuncTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        edtMember_id = (TextView) findViewById(R.id.edtMember_id);
        edtMember_pw = (TextView) findViewById(R.id.edtMember_pw);
        edtMember_pw_re = (TextView) findViewById(R.id.edtMmber_pw_re);
        btnMember_OK = (Button) findViewById(R.id.btnMember_OK);

        btnMember_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String result = " "; //서버에서 받아온 값

                connectCheck();

                //EditText정규화 (id - 영어와 숫자만 입력 가능
                //                      글자 제한 20)
                edtMember_id.setFilters(IDfilters);
//                edtMember_pw.setFilters(new InputFilter[]{filterAlphaNum});

                String pw = edtMember_pw.getText().toString();
                String pw_re = edtMember_pw_re.getText().toString();

                if (pw.equals(pw_re)) {

                    try {
                        //값 bundle에 넣고 서버로 보냄
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("action", "membership");
                        bundle.putSerializable("userid", edtMember_id.getText().toString());
                        bundle.putSerializable("userid", edtMember_pw.getText().toString());
                        task = new HTTPAsuncTask(getApplicationContext(), bundle);
                        result = task.execute("").get(); //서버에서 값을 받아옴

                        if (result.equals("1")) {
                            //login 화면으로 이동
                            startActivity(new Intent(getApplicationContext(), long.class));
                            Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_LONG);

                        } else {   // 가입x 토스트로 경고 보여줌
                            Toast.makeText(getApplicationContext(), "회원가입 실패, 다시 시도해 주세요", Toast.LENGTH_LONG);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "회원가입 실패, 비밀번호를 확인해 주세요.", Toast.LENGTH_LONG);
                }
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

    //네트워크 상태 확인
    public void connectCheck() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Toast.makeText(this,"네트워크 연결중입니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "네트워크 상태를 확인하십시오", Toast.LENGTH_SHORT).show();
        }
    }

    //정규화 설정(id)
    public InputFilter filterID= new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {
            Pattern patternID = Pattern.compile("^[a-zA-Z0-]*$");
            if(patternID.matcher(charSequence).matches()){
                return "";
            }
            return null;
        }
    };
    InputFilter lenghtID = new InputFilter.LengthFilter(20);
    InputFilter[] IDfilters = new InputFilter[]{filterID, lenghtID};



//    public InputFilter filterPW = new InputFilter() {
//        @Override
//        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
//            Pattern patternPW = Pattern.compile("")
//
//            return null;
//        }
//    };
}
