package com.jying.rainbow.Module;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jying.rainbow.Adapter.FragmentAdapter;
import com.jying.rainbow.Fragment.FirstFragment;
import com.jying.rainbow.Fragment.InterFragment;
import com.jying.rainbow.Fragment.SecondFragment;
import com.jying.rainbow.Module.Tuling.TulingActivity;
import com.jying.rainbow.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    List<Fragment> lists=new ArrayList<>();
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    InterFragment interFragment;
    FragmentAdapter adapter;
    public static MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp =getSharedPreferences("isLogin", Activity.MODE_PRIVATE);
        int flag=sp.getInt("flag",0);
        if (flag==1){
            startActivity(new Intent(this,TulingActivity.class));
            finish();
        }
        instance=this;
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        init();
    }

    private void init() {
        firstFragment=FirstFragment.newInstance(1);
        secondFragment=SecondFragment.newInstance(2);
        interFragment = InterFragment.newInstance(3);
        lists.add(firstFragment);
        lists.add(secondFragment);
        lists.add(interFragment);
        adapter=new FragmentAdapter(getSupportFragmentManager(),lists);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }
}
