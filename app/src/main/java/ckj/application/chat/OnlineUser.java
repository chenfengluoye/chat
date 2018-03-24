package ckj.application.chat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.net.URI;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class OnlineUser extends AppCompatActivity {

    static MyWebClient client;
    static ListView userlist;
    TextView qhyf;
    TextView myself;

   Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                try {
                    JSONObject ob=new JSONObject((String)msg.obj);
                    String from=ob.optString("fromuserId");
                    String to=ob.optString("touserId");
                    List<Msg> ourchat=User.chatrecord.get(from);
                    String message=ob.optString("message");
                    if(ourchat==null){
                        ourchat=new ArrayList<Msg>();
                        User.chatrecord.put(from,ourchat);
                    }
                    ourchat.add(new Msg(message,from,to));
                    Chat.chatAdpter.notifyDataSetChanged();
                    Chat.chatlist.smoothScrollToPosition(Chat.ourmsg.size());
                    Toast.makeText(OnlineUser.this,from+"的新消息",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if(msg.what==0){
                Toast.makeText(OnlineUser.this,"连接成功",Toast.LENGTH_SHORT).show();
            }else if(msg.what==-1){
                Toast.makeText(OnlineUser.this,"连接关闭",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(OnlineUser.this,"连接异常",Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_user);
        getSupportActionBar().hide();
        userlist=(ListView)findViewById(R.id.userlist);
        qhyf=(TextView)findViewById(R.id.tcdl);
        myself=(TextView)findViewById(R.id.myself);
        myself.setText(MainActivity.name);
        qhyf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayAdapter adapter=new ArrayAdapter(OnlineUser.this,simple_list_item_1,User.alluser);
        userlist.setAdapter(adapter);
        try {
            client=new MyWebClient(new URI(MainActivity.host+MainActivity.path+MainActivity.name),handler);
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(OnlineUser.this,Chat.class);
                intent.putExtra("user",User.alluser.get(position));
                startActivity(intent);
            }
        });
    }

//    实现一键返回到桌面功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
