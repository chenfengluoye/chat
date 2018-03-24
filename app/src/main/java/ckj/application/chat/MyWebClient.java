package ckj.application.chat;

import android.os.Handler;
import android.os.Message;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by chengkaiju on 2018/3/10.
 */

public class MyWebClient extends WebSocketClient {
    Handler handler;

    public MyWebClient(URI serverUri) {
        super(serverUri);
    }

    public MyWebClient(URI serverUri, Handler handler){
        super(serverUri);
        this.handler=handler;
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Message msg=new Message();
        msg.what=0;
        msg.obj=handshakedata;
        handler.sendMessage(msg);
    }

    @Override
    public void onMessage(String message) {
        Message msg=new Message();
        msg.what=1;
        msg.obj=message;
        handler.sendMessage(msg);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Message msg=new Message();
        msg.what=-1;
        msg.obj=reason;
        handler.sendMessage(msg);
    }

    @Override
    public void onError(Exception ex) {
        Message msg=new Message();
        msg.what=-2;
        msg.obj=ex.getMessage();
        handler.sendMessage(msg);
    }
}
