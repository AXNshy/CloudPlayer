package com.axnshy.cloudmusic.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axnshy.cloudmusic.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by axnshy on 16/8/20.
 */
@ContentView(R.layout.change_pwd)
public class ChangePassWord extends BaseActivity {
    @ViewInject(R.id.tv_oldPwd)
    private EditText oldPwd;
    @ViewInject(R.id.tv_newPwd)
    private EditText newPwd;
    @ViewInject(R.id.btn_change)
    private Button changeBtn;
    @ViewInject(R.id.iv_back)
    private ImageView returnImg;
    @ViewInject(R.id.tv_toolbar_title)
    private TextView titleTx;
    @ViewInject(R.id.toolbar_top)
    private Toolbar toolbar;

    @Event(value = {R.id.btn_change, R.id.iv_back})
    private void onListener(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_change:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String oldpwd = oldPwd.getText().toString().trim();
                        String newpwd = newPwd.getText().toString().trim();
                        BmobUser.updateCurrentUserPassword(oldpwd, newpwd, new UpdateListener() {
                            Message msg = Message.obtain();

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    msg.arg1 = 1;
                                    handler.sendMessage(msg);

                                } else {
                                    msg.arg1 = 0;
                                    msg.obj = e;
                                    handler.sendMessage(msg);
                                }
                            }

                        });
                    }
                }).start();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        returnImg.setImageResource(R.drawable.ic_chevron_left_white_48);
        titleTx.setText("修改密码");
        Toast.makeText(ChangePassWord.this, "修改密码", Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0) {
                Toast.makeText(ChangePassWord.this, "失败:", Toast.LENGTH_SHORT).show();
            }
            if (msg.arg1 == 1) {
                Toast.makeText(ChangePassWord.this, "密码修改成功，可以用新密码进行登录啦", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
}
