package com.jying.rainbow.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jying.rainbow.R;


/**
 * Created by Jying on 2017/9/8.
 */

public class FirstFragment extends Fragment {


    public static FirstFragment newInstance(int page) {
        FirstFragment firstFragment = new FirstFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        firstFragment.setArguments(bundle);
        return firstFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }
}
