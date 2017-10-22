package com.jying.rainbow.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jying.rainbow.Module.LoginActivity;
import com.jying.rainbow.Module.MainActivity;
import com.jying.rainbow.R;


/**
 * Created by Jying on 2017/9/8.
 */

public class InterFragment extends Fragment {
    ImageButton inter;

    public static InterFragment newInstance(int page) {
        InterFragment interFragment = new InterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        interFragment.setArguments(bundle);
        return interFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inter, container, false);
        inter= (ImageButton) view.findViewById(R.id.inter);
        inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                MainActivity.instance.finish();
            }
        });
        return view;
    }
}
