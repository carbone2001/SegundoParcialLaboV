package com.example.segundoparcial;

import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class HttpThread extends Thread {
    public final static int TEXT = 1;
    private Handler handler;
    private String url;
    private String method;
    private int type;
    public HttpThread(String  url, String method, Handler handler,int type){
        this.handler = handler;
        this.url = url;
        this.method = method;
        this.type = type;
    }

    @Override
    public void run() {
        Message msg = new Message();
        if(this.type == TEXT){
            String response =  HttpConexion.request(this.url,this.method);
            msg.obj = response;
            msg.arg1 = TEXT;
        }
        this.handler.sendMessage(msg);
    }

}
