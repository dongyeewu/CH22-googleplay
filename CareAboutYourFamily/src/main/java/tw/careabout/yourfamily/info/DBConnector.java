package tw.careabout.yourfamily.info;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBConnector {
    private static String postUrl;
    private static OkHttpClient client = new OkHttpClient();
    static String result = null;
    public static int httpstate =0;

    static String connect_ip = "https://aboutyfc.com/cayf_android_mysql_connect/android_connect_cayf_all_17.php";

    public static String executeQuery(String nameValuePairs) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip;
        //--------------
        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string", nameValuePairs)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值

        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

