package com.axnshy.cloudmusic.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axnshy.cloudmusic.R;
import com.axnshy.cloudmusic.User;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by axnshy on 16/8/5.
 */
@ContentView(R.layout.personinfo)
public class User_InfoShowActivity extends BaseActivity {

    @ViewInject(R.id.iv_setAvatar)
    private CircleImageView avatar;
    @ViewInject(R.id.iv_userInfo_return)
    private ImageView returnImg;
    @ViewInject(R.id.tv_personInfo_edit)
    private TextView editTx;
    @ViewInject(R.id.tv_personInfo_nickName)
    private TextView nickNameTx;
    @ViewInject(R.id.tv_personInfo_gender)
    private TextView genderTx;
    @ViewInject(R.id.tv_personInfo_age)
    private TextView ageTx;
    @ViewInject(R.id.tv_personInfo_birthday)
    private TextView birthdayTx;
    @ViewInject(R.id.tv_personInfo_address)
    private TextView addressTx;
    @ViewInject(R.id.tv_personInfo_email)
    private TextView emailTx;
    @ViewInject(R.id.tv_personInfo_QQ)
    private TextView qqTx;
    @ViewInject(R.id.tv_personInfo_telephone)
    private TextView telephoneTx;
    @ViewInject(R.id.tv_title)
    private TextView titleTx;

    @Event(value = {R.id.iv_setAvatar, R.id.tv_personInfo_edit, R.id.iv_userInfo_return})
    private void initOnClickListener(View v) {
        switch (v.getId()) {
            case R.id.iv_userInfo_return:
                onBackPressed();
                break;
            case R.id.tv_personInfo_edit:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //updateUI();
        titleTx.setText("個人情報");
    }

    private void updateUI() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            nickNameTx.setText(user.getNickName());
            int gender = user.getGender();
            if (gender == 0) {
                genderTx.setText("女");
            }
            if (gender == 1) {
                genderTx.setText("男");
            }
//            Log.e("TAG_AVATAR","    avatar ur  :  "+user.getAvatar().getFileUrl());
//            avatar.setImageURI(Uri.parse(user.getAvatar().getFileUrl()));
            ageTx.setText(user.getAge());
            addressTx.setText(user.getAddress());
            emailTx.setText(user.getEmail());
            qqTx.setText(user.getQq());
            telephoneTx.setText(user.getMobilePhoneNumber());
        }

    }
}

