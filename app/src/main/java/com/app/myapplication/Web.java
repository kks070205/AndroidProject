package com.app.myapplication;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Web extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //개발자가 정의한 디자인 파일을 인플레이션 시킨 후 반환된 뷰를 현재 메서드의 반환값으로 지정
        return inflater.inflate(R.layout.activity_web, container, false);
    }

}
