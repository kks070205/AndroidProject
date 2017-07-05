package com.app.myapplication;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class comm extends Fragment {

    ListView comm_list;
    Button btn_write_go;


//    Activity에서의 onCreate()와 비슷
//    ui관련 작업은 불가
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
//    Layout을 inflater을하여 View작업을 하는 곳
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //개발자가 정의한 디자인 파일을 인플레이션 시킨 후 반환된 뷰를 현재 메서드의 반환값으로 지정
        return inflater.inflate(R.layout.activity_comm, container, false);


    }

    //Activity에서 Fragment를 모두 생성하고 난다음 호출
    // Activity의 onCreate()에서 setContentView()한 다음
    // ui변경작업이 가능
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
