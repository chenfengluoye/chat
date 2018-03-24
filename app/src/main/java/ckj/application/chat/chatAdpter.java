package ckj.application.chat;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chengkaiju on 2018/3/10.
 */

public class chatAdpter extends ArrayAdapter {
    List<Msg> chatmsg;
    int res;
    public chatAdpter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Msg> msgs) {
        super(context, resource, msgs);
        this.chatmsg=msgs;
        this.res=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View view= LayoutInflater.from(parent.getContext()).inflate(res,null);
            Msg msg=chatmsg.get(position);
            if(msg.from.equals(MainActivity.name)){
                LinearLayout send=(LinearLayout)view.findViewById(R.id.send);
                send.setVisibility(View.VISIBLE);
                TextView sendmsg=(TextView)view.findViewById(R.id.sendmsg);
                sendmsg.setText(msg.msg);
            }else {
                LinearLayout rec=(LinearLayout)view.findViewById(R.id.rec);
                rec.setVisibility(View.VISIBLE);
                TextView recmsg=(TextView)view.findViewById(R.id.recmsg);
                recmsg.setText(msg.msg);
            }
        return view;

    }
}
