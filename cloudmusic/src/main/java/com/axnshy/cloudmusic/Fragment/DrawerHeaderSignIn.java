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
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by axnshy on 16/8/20.
 */
@ContentView(R.layout.drawer_header_sign_in)
public class DrawerHeaderSignIn extends BaseFragment {

    @ViewInject(R.id.iv_user_avatar)
    private CircleImageView avatarImg;
    @ViewInject(R.id.tv_username)
    private TextView usernameTx;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected void initView() {
        /*Observer<User> observer = new Observer<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(User user) {
                usernameTx.setText(user.getNickName() != null ? user.getNickName() : user.getUsername());
            }
        };*/
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, User>() {

                    @Override
                    public User call(String s) {
                        return BmobUser.getCurrentUser(User.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        usernameTx.setText(user.getNickName() != null ? user.getNickName() : user.getUsername());
                    }
                });


//                        usernameTx.setText(
//                (BmobUser.getCurrentUser(User.class).getNickName()) != null
//                        ? BmobUser.getCurrentUser(User.class).getNickName()
//                        : BmobUser.getCurrentUser(User.class).getUsername());
    }
}
