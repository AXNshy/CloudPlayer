package com.axnshy.cloudmusic.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.axnshy.cloudmusic.Config;
import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.User;
import com.axnshy.cloudmusic.Utils.HttpUtils;
import com.axnshy.cloudmusic.Utils.JsonUtils;

/**
 * Created by axnshy on 16/5/25.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView registerTx;
    private String username;
    private String password;
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int loginState = msg.arg1;
            System.out.println("msg.arg1     :     " + msg.arg1);
            //登录成功
            if (loginState == 1) {
                User.setmUser((User) msg.obj);
                Intent intent = new Intent(LoginActivity.this, Launch.class);
                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
//                MySharedPre.updateCurrentUser(context, User.getmUser().getUsername(), User.getmUser().get);
                startActivity(intent);
                finish();
            }
            //登录失败
            if (loginState == 0) {
                Toast.makeText(LoginActivity.this, "登录失败,用户名或密码错误", Toast.LENGTH_SHORT).show();
                et_password.setText("");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorDrawerBack));
        initView();
        initEvent();
        context = this;
    }


    private void initView() {

        et_username = (EditText) findViewById(R.id.et_input_username);
        et_password = (EditText) findViewById(R.id.et_input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        registerTx = (TextView) findViewById(R.id.tv_register);
    }

    private void initEvent() {
        btn_login.setOnClickListener(this);
        registerTx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login: {
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString();
                login(this, username, password);
                break;
            }
            case R.id.tv_register: {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            }

        }
    }

    private boolean login(Context context, String username, String password) {
        final String url = Config.WEB_SERVER_LOGIN + "username=" + username + "&password=" + password;
        if (HttpUtils.isServerConnection(context)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    String result = HttpUtils.doGet(url);
                    String loginState = JsonUtils.getLoginInfoFromJson(result);
                    System.out.println("loginState     :     " + loginState);
                    if (loginState.equals("successful")) {
                        msg.arg1 = 1;
                        msg.obj = JsonUtils.parseJsonToUser_Basic(result);
                    } else {
                        msg.arg1 = 0;
                    }
                    //发送消息给主线程
                    handler.sendMessage(msg);
                }
            }).start();
        }
        return false;
    }
}
