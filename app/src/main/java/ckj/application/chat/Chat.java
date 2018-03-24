package ckj.application.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class Chat extends AppCompatActivity {
    static ListView chatlist;
    String toUser;
    static List<Msg> ourmsg;
    static chatAdpter chatAdpter;
    EditText msgtext;
    TextView send;
    TextView tuichu;
    TextView toname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        send=(TextView)findViewById(R.id.send);
        msgtext=(EditText)findViewById(R.id.msg);
        chatlist=(ListView)findViewById(R.id.chatlist);
        toname=(TextView)findViewById(R.id.toname);
        tuichu=(TextView)findViewById(R.id.tuichu);
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toUser=getIntent().getStringExtra("user");
        ourmsg= User.chatrecord.get(toUser);
        toname.setText(toUser);
        if(ourmsg==null){
            ourmsg=new ArrayList<>();
            User.chatrecord.put(toUser,ourmsg);
        }
        chatAdpter=new chatAdpter(Chat.this,R.layout.msgitem,ourmsg);
        chatlist.setAdapter(chatAdpter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=msgtext.getText().toString();
                JSONObject object=new JSONObject();
                try {
                    object.put("fromuserId",MainActivity.name);
                    object.put("message",msg);
                    object.put("touserId",toUser);
                    Msg msg1=new Msg(msg,MainActivity.name,toUser);
                    ourmsg.add(msg1);
                    chatAdpter.notifyDataSetChanged();
                    chatlist.smoothScrollToPosition(ourmsg.size());
                    OnlineUser.client.send(object.toString());
                    msgtext.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatlist.setAdapter(chatAdpter);
    }
}
