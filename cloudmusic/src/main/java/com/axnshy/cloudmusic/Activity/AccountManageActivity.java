package com.axnshy.cloudmusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;

/**
 * Created by axnshy on 16/8/19.
 */
@ContentView(R.layout.account_manage)
public class AccountManageActivity extends BaseActivity {

    @ViewInject(R.id.iv_back)
    private ImageView returnImg;
    @ViewInject(R.id.tv_toolbar_title)
    private TextView toolbarTitle;

    private TextView toolbarRight;
    @ViewInject(R.id.toolbar_top)
    private Toolbar toolbar;
    @ViewInject(R.id.tv_basicInfo)
    private TextView basicInfo;
    @ViewInject(R.id.tv_changePwd)
    private TextView changePwd;
    @ViewInject(R.id.tv_sign_out)
    private TextView signOut;

    private final int[] ids = {R.id.iv_back, R.id.tv_basicInfo, R.id.tv_changePwd, R.id.tv_sign_out};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Event(value = {R.id.iv_back, R.id.tv_basicInfo, R.id.tv_changePwd, R.id.tv_sign_out})
    private void OnListener(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_out:
                BmobUser.logOut();   //清除缓存用户对象
                onBackPressed();
//                User currentUser =BmobUser.getCurrentUser(); // 现在的currentUser是null了
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_basicInfo:
                if (BmobUser.getCurrentUser(User.class) != null) {
                    Intent intent2 = new Intent(AccountManageActivity.this, User_InfoShowActivity.class);
                    startActivity(intent2);
                } else
                    Toast.makeText(AccountManageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_changePwd:
                if (BmobUser.getCurrentUser(User.class) != null) {
                    Intent intent1 = new Intent(AccountManageActivity.this, ChangePassWord.class);
                    startActivity(intent1);
                } else {
                    Toast.makeText(AccountManageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void initView() {
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        returnImg.setImageResource(R.drawable.ic_chevron_left_white_48);
        toolbarTitle.setText(R.string.account_manage);

    }
}
