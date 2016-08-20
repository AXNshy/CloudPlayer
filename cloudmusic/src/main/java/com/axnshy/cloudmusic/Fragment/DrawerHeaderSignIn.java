package com.axnshy.cloudmusic.Fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by axnshy on 16/8/20.
 */
@ContentView(R.layout.drawer_header_sign_in)
public class DrawerHeaderSignIn extends BaseFragment{

    @ViewInject(R.id.iv_user_avatar)
    private CircleImageView avatarImg;
    @ViewInject(R.id.tv_username)
    private TextView usernameTx;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        usernameTx.setText(BmobUser.getCurrentUser(User.class).getNickName());
    }
}
