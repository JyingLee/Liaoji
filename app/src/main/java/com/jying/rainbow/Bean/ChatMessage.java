package com.jying.rainbow.Bean;

/**
 * Created by Jying on 2017/9/9.
 */

public class ChatMessage {
    private String name;
    private String text;
    private int type;
    public static final int ME = 1;
    public static final int TULING = 2;
    public static final int TULING_URL = 3;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ChatMessage(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public ChatMessage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
