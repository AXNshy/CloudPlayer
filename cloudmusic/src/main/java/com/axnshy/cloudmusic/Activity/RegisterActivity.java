package com.axnshy.cloudmusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.axnshy.cloudmusic.Config;
import com.axnshy.cloudmusic.MySharedPre;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.User;
import com.axnshy.cloudmusic.Utils.HttpUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by axnshy on 16/5/28.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button register;
    private String userName;
    private String Password;
    private String registerUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorDrawerBack));
        initView();
        initListener();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.et_input_username);
        password = (EditText) findViewById(R.id.et_input_password);
        register = (Button) findViewById(R.id.btn_register);
    }

    private void initListener() {
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_register: {
                userName = username.getText().toString();
                Password = password.getText().toString();
//                registerUser(userName, Password);
                User bu = new User();
                bu.setUsername("sendi");
                bu.setPassword("123456");
                bu.setEmail("sendi@163.com");
//注意：不能用save方法进行注册
                bu.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功:" + s.toString(), Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
//                            loge(e);
                        }
                    }
                });
            }
        }
    }

    private void registerUser(String UserName, String password) {
        registerUrl = Config.WEB_SERVER_REGISTER + "userName=" + UserName + "&password=" + password;
        if (HttpUtils.isServerConnection(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = HttpUtils.doGet(registerUrl);
                    System.out.println("result:      " + result);
                    Message msg = Message.obtain();
                    //给Message中的obj属性赋值
                    msg.obj = result;
                    //发送消息给主线程
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.equals("ok")) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                System.out.println("register succesful");
                MySharedPre.updateCurrentUser(RegisterActivity.this, userName, Password);

                Intent intent = new Intent(RegisterActivity.this, Launch.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
            }
        }
    };
}
