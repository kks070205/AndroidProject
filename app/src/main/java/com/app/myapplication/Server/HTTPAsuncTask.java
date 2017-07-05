package com.app.myapplication.Server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


/*
 * Params - 비동기 작업시 필요한 데이터 자료형
 * 			ex)웹사이트에 데이터 요청시 전송할 파라미터값(ID,PW 등....)
 * 
 * Progress - 비동기 방식의 요청이 진행될때 사용될 데이터 자료형.
 * 			  숫자형 자료형을 많이 사용한다.
 * 
 * Result - 웹서버로부터 가져오게 될 데이터에 알맞는 자료형을 개발자가 결정
 * 			주로 JSON, XML등을 가져오게 되므로 String을 많이 사용한다.
 */
public class HTTPAsuncTask extends AsyncTask<String, Integer, String> {
    private Context context;
    //ProgressDialog dialog;

    HTTPClient load;//접속과 요청을 담당하는 객체 선언

    public HTTPAsuncTask(Context context, Bundle bundle) {
        this.context = context;

        //상황에 따라 오버로딩으로 바꾸시오
        load = new HTTPClient(bundle);
    }

    //백그라운드 작업 수행전에 해야할 업무등을 이 메서드에 작성하며 되는데,
    //이 메서드는 UI쓰레드에 의해 작동하므로 UI를 제어할 수 있다.
    //따라서 이 타이밍에 진행바를 보여주는 작업등을 할 수 있다.
    protected void onPreExecute() {
        super.onPreExecute();

//        dialog = new ProgressDialog(context);
//        //dialog.setCancelable(false);
//        dialog.show();
    }

    //비동기방식으로 작동할 메서드이며, 주로 메인쓰레드와는 별도로
    //웹사이트의 연동이나 지연이 발생하는 용도로 사용하면 된다.
    //사실상 개발자가 정의 쓰레드에서 run메서드와 비슷하다
    //  'String...' 가변형 파라미터로 파라미터 개수 상관없이 넣을 수 있다.
    protected String doInBackground(String... params) {
        //웹서버에 요청시도
        String data = load.request();

        Log.i("서버에서 받은 data", data);
        return data;
    }


    //백그라운드 메서드가 업무수행을 마칠때 호출되는 메서드.
    //UI쓰레드에 의해 호출되므로, UI쓰레드를 제어할 수 있다.
    //따라서 진행바를 그만 나오게 할 수 있다.
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//        dialog.dismiss();
    }
}
