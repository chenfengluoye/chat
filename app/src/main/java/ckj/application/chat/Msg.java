package ckj.application.chat;

/**
 * Created by chengkaiju on 2018/3/10.
 */

public class Msg {
    String msg;
    String from;
    String to;
    Msg(String msg,String from,String to){
        this.from=from;
        this.msg=msg;
        this.to=to;
    }
}
