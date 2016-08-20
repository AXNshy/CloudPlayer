package com.axnshy.cloudmusic.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
//                login(this, username, password);
                User bu2 = new User();
                bu2.setUsername(username);
                bu2.setPassword(password);
                bu2.login(new SaveListener<User>() {

                    @Override
                    public void done(User user, BmobException e) {
                        if(e==null){
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                        }else{
//                            loge(e);
                        }
                    }
                });

                break;
            }
            case R.id.tv_register: {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            }

        }
    }
}
