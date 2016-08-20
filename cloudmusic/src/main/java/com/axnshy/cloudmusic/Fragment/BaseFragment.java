package com.axnshy.cloudmusic.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;


/**
 * Created by axnshy on 16/8/9.
 */
public class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x.view().inject(this,inflater,container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
