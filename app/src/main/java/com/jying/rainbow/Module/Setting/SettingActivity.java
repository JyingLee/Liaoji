package com.jying.rainbow.Module.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jying.rainbow.Bean.MyUser;
import com.jying.rainbow.R;
import com.jying.rainbow.Utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by Jying on 2017/10/15.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    Context con;
    @BindView(R.id.layout_touxiang)
    CardView layout_touxiang;
    @BindView(R.id.layout_nickname)
    CardView layout_nickname;
    @BindView(R.id.layout_selfintro)
    CardView layout_selfintro;
    @BindView(R.id.iv_touxiang)
    CircleImageView iv_touxiang;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_selfintro)
    TextView tv_selfintro;
    private BmobFile bmobFile;
    String userObjectID;
    private String inputNickName;
    private String inputSelfIntro;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    MyUser user=new MyUser();
                    user.setUserIcon(bmobFile);
                    user.update(userObjectID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null) {
                                ToastUtils.showToast(con, "头像上传成功");
                                Glide.with(con).load(bmobFile.getUrl()).into(iv_touxiang);
                            }else {
                                ToastUtils.showToast(con,"没网了?!");
                            }
                        }
                    });
                    break;
                case 1:
                    MyUser user1=new MyUser();
                    user1.setNickName(inputNickName);
                    user1.update(userObjectID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                ToastUtils.showToast(con,"修改昵称成功");
                                tv_nickname.setText(inputNickName);
                            }else {
                                ToastUtils.showToast(con,"没网了?!");
                            }
                        }
                    });
                    break;
                case 2:
                    MyUser user2=new MyUser();
                    user2.setSelf(inputSelfIntro);
                    user2.update(userObjectID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                ToastUtils.showToast(con,"修改自我介绍成功");
                                tv_selfintro.setText(inputSelfIntro);
                            }else {
                                ToastUtils.showToast(con,"没网了?!");
                            }
                        }
                    });
                    break;
            }
        }
    };

    private void setData() {
        BmobQuery<MyUser>query=new BmobQuery<>();
        query.getObject(userObjectID, new QueryListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e==null) {
                    if (!user.getNickName().equals("") && !user.getSelf().equals("") && !user.getUserIcon().getUrl().equals("")) {
                        Glide.with(con).load(user.getUserIcon().getUrl()).into(iv_touxiang);
                        tv_nickname.setText(user.getNickName());
                        tv_selfintro.setText(user.getSelf());
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        con=SettingActivity.this;
        ButterKnife.bind((Activity) con);
        layout_touxiang.setOnClickListener((View.OnClickListener) con);
        layout_nickname.setOnClickListener((View.OnClickListener) con);
        layout_selfintro.setOnClickListener((View.OnClickListener) con);
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        userObjectID=user.getObjectId();
        setData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                String photo = photos.get(0);
                File file = new File(photo);
                bmobFile = new BmobFile(file);
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            handler.sendEmptyMessage(0);
                        }else {
                            ToastUtils.showToast(con,"没网了?!");
                        }

                    }
                });

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_touxiang:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(SettingActivity.this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.layout_nickname:
                new MaterialDialog.Builder(con)
                        .title("修改昵称")
                        .negativeText("取消")
                        .positiveText("确认")
                        .input("", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                inputNickName = input.toString();
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        handler.sendEmptyMessage(1);
                    }
                }).show();
                break;

            case R.id.layout_selfintro:
                new MaterialDialog.Builder(con)
                        .title("修改自我介绍")
                        .negativeText("取消")
                        .positiveText("确认")
                        .input("", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                inputSelfIntro = input.toString();
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        handler.sendEmptyMessage(2);
                    }
                }).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId()==android.R.id.home){
             onBackPressed();
         }
        return super.onOptionsItemSelected(item);
    }
}
