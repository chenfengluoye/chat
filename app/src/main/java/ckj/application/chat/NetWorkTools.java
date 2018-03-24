package ckj.application.chat;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by chengkaiju on 2018/3/10.
 */

public class NetWorkTools {

    static OkHttpClient client = new OkHttpClient();

   static String connect(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
