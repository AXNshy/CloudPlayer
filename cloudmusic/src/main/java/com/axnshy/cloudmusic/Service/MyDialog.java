package com.axnshy.cloudmusic.Service;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.axnshy.cloudmusic.R;

import java.util.ArrayList;

public class MyDialog extends Dialog implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private ArrayAdapter mAdapter;
    private ArrayList<String> mList;

    private Button okBtn;
    private Button cancleBtn;
    private int buttonNum=2;

    //接口对象
    private OnBtnClickListemer mOnOKBtnClickListemer;

    public MyDialog(Context context, int buttonNum) {
        super(context, R.style.dialog);
        this.buttonNum=buttonNum;
        Window window = this.getWindow();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(R.layout.my_dailog_layout);
        initView(window);

    }

    //构造函数
    public MyDialog(Context context) {
        super(context, R.style.dialog);

        Window window = this.getWindow();

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(R.layout.my_dailog_layout);
        initView(window);

    }

    private void initView(Window window){
      //  okBtn = (Button) window.findViewById(R.id.ok_btn);
       // cancleBtn = (Button) window.findViewById(R.id.cancle_btn);
        if(buttonNum==1){
            cancleBtn.setVisibility(View.GONE);
        }
        mListView= (ListView) window.findViewById(R.id.id_dialog_list);
        mListView.setOnItemClickListener(this);
        mList=new ArrayList<String>();
        mList.add(0,"我的收藏");
        mList.add(1,"东方幻想");
        mList.add(2,"动漫收藏");
        mAdapter=new ArrayAdapter(window.getContext(),android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(mAdapter);
    }

    public void showDiglog() {
        show();
    }


    public void setBtnText(String leftText,String rightText){
        okBtn.setText(leftText);
        cancleBtn.setText(rightText);
    }

    public void setBtnText(String leftText){
        okBtn.setText(leftText);
    }
    public void setBtnClick(final OnBtnClickListemer onBtnClickListemer){


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClickListemer.OnOKBtnClick();//接口对象的方法，需要在实例化MyDialog是实现该回调方法
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onBtnClickListemer.OnCancleBtnClick();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
