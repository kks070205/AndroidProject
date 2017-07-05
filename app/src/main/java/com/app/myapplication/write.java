package com.app.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.myapplication.Server.HTTPAsuncTask;

import java.util.concurrent.ExecutionException;

public class write extends AppCompatActivity {
    EditText edt_title,edt_content; //제목,내용
    Button btn_write_ok; // OK 버튼

    Intent data;
    String action;
    HTTPAsuncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        edt_title = (EditText)findViewById(R.id.edt_title);
        edt_content = (EditText)findViewById(R.id.edt_content);
        btn_write_ok = (Button)findViewById(R.id.btn_write_ok);

        data = getIntent();
        action = data.getStringExtra("action");

        btn_write_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = false;
                //입력한 정보들을 서버에 insert 요청
                Bundle bundle = new Bundle();
                bundle.putSerializable("action", action);
                bundle.putSerializable("userid", getSharedPreferences("info", MODE_PRIVATE).getString("userid", ""));
                bundle.putSerializable("title", edt_title.getText().toString());
                bundle.putSerializable("contents", edt_content.getText().toString());
                task = new HTTPAsuncTask(getApplicationContext(), bundle);

                try {

                    if (task.execute("").get().trim().equals("1")) {
                        //1일 경우, 업로드
                        result = true;
                        finish();
                        //insert 성공 시
                        if (result) {
                            Intent intent = new Intent(getApplicationContext(), comm.class);
                            startActivity(intent);
                        }
                    } else {
                        //insert 실패 시
                        Toast.makeText(getApplicationContext(), "작성 실패했습니다. 다시 확인해주세요.", Toast.LENGTH_LONG);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }
}
