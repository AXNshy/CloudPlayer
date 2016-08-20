package com.axnshy.cloudmusic.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.axnshy.cloudmusic.Activity.LoginActivity;
import com.axnshy.cloudmusic.Activity.RegisterActivity;
import com.axnshy.cloudmusic.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by axnshy on 16/8/20.
 */
@ContentView(R.layout.drawer_header_sign_out)
public class DrawerHeaderSignOut extends BaseFragment {
    @ViewInject(R.id.btn_login)
    private Button signIn;
    @ViewInject(R.id.btn_register)
    private Button register;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_header_sign_out, container, false);
        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Event(value = {R.id.btn_login, R.id.btn_register})
    private void onListener(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Intent it = new Intent(view.getContext(), LoginActivity.class);
                startActivityForResult(it, 1);
                break;
            case R.id.btn_register:
                Intent reit = new Intent(view.getContext(), RegisterActivity.class);
                startActivityForResult(reit, 1);
                break;

        }
    }
}
