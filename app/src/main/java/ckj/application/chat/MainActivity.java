package ckj.application.chat;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    static JSONArray alluser=new JSONArray();
    static String host="http://39.106.34.156:8080";
    static String path="/WebSocketDemo/ser/";
    static String name="default";
    String savepath= Environment.getExternalStorageDirectory()+"/Chat/";
    static String password;
    TextView save;
    EditText hosttext;
    CheckBox autosave ;
    EditText nametext;
    EditText pass;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,OnlineUser.class);
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hosttext=(EditText)findViewById(R.id.host);
        nametext=(EditText)findViewById(R.id.name);
        pass=(EditText)findViewById(R.id.pass);
        save=(TextView)findViewById(R.id.save);
        autosave=(CheckBox)findViewById(R.id.autosave);
        try {
            File file=new File(savepath);
            File file1=new File(file,"config.txt");
            if(!file.exists()){
                file.mkdirs();
                file1.createNewFile();
            }else {
                if(file1.exists()){
                    FileReader reader=new FileReader(file1);
                    BufferedReader reader1=new BufferedReader(reader);
                    String line=null;
                    StringBuilder builder=new StringBuilder();
                    while ((line=reader1.readLine())!=null){
                        builder.append(line);
                    }
                    JSONObject conf=new JSONObject(builder.toString());
                    host=conf.optString("host");
                    if(host==null||host.equals("")){
                        host="http://39.106.34.156:8080";
                    }
                    name=conf.getString("admin");
                    nametext.setText(name);
                    password=conf.getString("pass");
                    pass.setText(password);
                }else {
                    file1.createNewFile();
                }
            }

        } catch (Exception e) {
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newhost=hosttext.getText().toString();
                if(newhost.equals("默认")){
                    host="http://39.106.34.156:8080";
                }
                name=nametext.getText().toString();
                password=pass.getText().toString();
                File file=new File(savepath);
                File file1=new File(file,"config.txt");
                boolean is=autosave.isSelected();
                if(autosave.isChecked()){
                    try {
                        FileWriter writer=new FileWriter(file1);
                        BufferedWriter bufferedWriter=new BufferedWriter(writer);
                        JSONObject jsonObject=new JSONObject();
                        if(!newhost.equals("默认")){
                            jsonObject.put("host",host);
                        }
                        jsonObject.put("admin",name);
                        jsonObject.put("pass",password);
                        bufferedWriter.write(jsonObject.toString());
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(file.exists()){
                        file.delete();
                    }
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String chencke= NetWorkTools.connect(host+"/WebSocketDemo/LogRegister?admine="+name+"&pass="+password);
                            JSONObject re=new JSONObject(chencke);
                            if(re.optBoolean("result")){
                                String res= NetWorkTools.connect(host+"/WebSocketDemo/getAllUser");
                                alluser= new JSONArray(res);
                                User.alluser=ArrayTools.getListFromJSONArray(alluser);
                                Message message=new Message();
                                message.what=0;
                                message.obj=re.optString("reason");
                                handler.sendMessage(message);
                            }else {
                                Message message=new Message();
                                message.what=-2;
                                message.obj=re.optString("reason");
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            Message message=new Message();
                            message.what=-1;
                            message.obj=e.getMessage();
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });

    }



}
