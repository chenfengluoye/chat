package ckj.application.chat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkaiju on 2018/3/10.
 */

public class ArrayTools {

    public static List getListFromJSONArray(JSONArray array){
        List<String> list=new ArrayList<>();
        for(int i=0;i<array.length();i++){
            try {
                list.add( array.getString(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
