package com.jying.rainbow.Bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Jying on 2017/10/15.
 */

public class MyUser extends BmobUser {
    private BmobFile userIcon;
    private String self;
    private String nickName;


    public BmobFile getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(BmobFile userIcon) {
        this.userIcon = userIcon;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
