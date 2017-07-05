package com.app.myapplication.Server;


import android.os.Bundle;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;


/**
 * Created by lovely ssun on 2017-07-04.
 */

public class HTTPClient {
    private URL url = null;
    private HttpURLConnection conn = null;
    private DataOutputStream out = null;
    private BufferedReader buffer = null;
    
    Bundle bundle = null;
    
    private String URL = "연결할 서버";
    private int method = 0; //post = 0, get = 1
    
    private String action; //서버에서 구분

    public  HTTPClient(Bundle bundle){
        this.bundle = bundle;
        action = bundle.getSerializable("action").toString();
        setMethod();
    }

    private void setMethod() {
        if (action.equals("g"))
            method = 1; //GET 방식
        else
            method = 2; //POST 방식
    }

    //GET 방식
    private InputStream sendGetMessage(Properties params) throws IOException {
        url = new URL(getURL() + "?" + encodeString(params));
        conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);

        conn.connect();            //웹서버에 요청하는 시점
        Log.i("서버로 전송!!!", url.toString());

        return conn.getInputStream();
    }

    //POST 방식
    private InputStream sendPostMessage(Properties params) throws IOException {
        url = new URL(getURL());
        conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");   //POST 방식의 데이터가 인코딩된 데이터

        out = null;
        try {
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(encodeString(params));
            out.flush();
        } finally {
            if (out != null) out.close();
        }

        conn.connect();            //웹서버에 요청하는 시점
        Log.i("서버로 전송!!!", url.toString());

        return conn.getInputStream();
    }

    //key:value 형식으로 데이터 저장
    public static String encodeString(Properties params) {
        StringBuffer sb = new StringBuffer(256);
        Enumeration names = params.propertyNames();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = params.getProperty(name);
            sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));

            if (names.hasMoreElements()) sb.append("&");
        }
        return sb.toString();
    }


    //params 가져오는 메소드
    private Properties getParams() {
        Properties prop = new Properties();
        switch (action) {
            case "login":
                prop.setProperty("action", bundle.getSerializable("action").toString());
                prop.setProperty("userid", bundle.getSerializable("userid").toString());
                prop.setProperty("userpw", bundle.getSerializable("userpw").toString());
                break;
            case "insert":
                prop.setProperty("action", action);
                prop.setProperty("title",  bundle.getSerializable("title").toString());
                prop.setProperty("content", bundle.getSerializable("contents").toString());
                break;
        }
        return prop;
    }

    //url가져오기
    private String getURL() {
        String serverPage = null;

        if (action.equals("login"))
            serverPage = "login URL";
        else if (action.equals("insert"))
            serverPage = "write URL";

        return URL + serverPage;
    }

    public String request() {

        String data = "";
        InputStream is = null;

        try {
            switch (method) {
                case 1:
                    is = sendGetMessage(getParams());   //웹서버로부터 전송받을 데이터에 대한 스트림 얻기(GET 방식)
                    break;
                case 2:
                    is = sendPostMessage(getParams());  //웹서버로부터 전송받을 데이터에 대한 스트림 얻기(POST 방식)
                    break;
                default:
                    is = null;
                    break;
            }
            conn.connect();            //웹서버에 요청하는 시점


            //1byte기반의 바이트스트림이므로 한글이 깨진다.
            //따라서 버퍼처리된 문자기반의 스트림으로 업그레이드 해야 된다.
            buffer = new BufferedReader(new InputStreamReader(is));

            //스트림을 얻어왔으므로, 문자열로 반환
            StringBuffer str = new StringBuffer();
            String d = null;
            while ((d = buffer.readLine()) != null) {
                str.append(d);
            }

            data = str.toString().trim();
            Log.d("서버에서 받은 값 ", data);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println("서버에서 받은 값 : " + data);
        return data;
    }

}
