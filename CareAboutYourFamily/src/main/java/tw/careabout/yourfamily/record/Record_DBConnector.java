package tw.careabout.yourfamily.record;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Record_DBConnector {
    public static int httpstate = 0;
    //--------------------------------------------------------
    private static String postUrl;
    //    private static String myResponse;
    static String result = null;
    private static final OkHttpClient client = new OkHttpClient();
    //---------------------------------------------------------

//    static String connect_ip_re = "https://frankhsieh925.000webhostapp.com/cayf_android_mysql_connect/android_connect_cayf_all_17.php";

//     -------HOSTING-------

    static String connect_ip_rs = "https://aboutyfc.com/websiteGina/android_connect_db_join_m_e.php";
    static String connect_ip_re = "https://aboutyfc.com/cayf_android_mysql_connect/android_connect_cayf_all_17.php";

    public static String executejoin_rs(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_rs;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "join_m_e")
                .add("mem004", query_0)
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
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeQuery_re(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_re;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "query")
                .add("query_string", query_0)
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
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //    新增
    public static void executeInsert_re(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_re;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);
//        String query_5 = query_string.get(5);
        String query_6 = query_string.get(6);
        String query_7 = query_string.get(7);
        String query_8 = query_string.get(8);
        String query_9 = query_string.get(9);
        String query_10 = query_string.get(10);
        String query_11 = query_string.get(11);
        String query_12 = query_string.get(12);
        String query_13 = query_string.get(13);
        String query_14 = query_string.get(14);
        String query_15 = query_string.get(15);
        String query_16 = query_string.get(16);
        String query_17 = query_string.get(17);
        String query_18 = query_string.get(18);
        String query_19 = query_string.get(19);
        String query_20 = query_string.get(20);
        String query_21 = query_string.get(21);
        String query_22 = query_string.get(22);

        FormBody body = new FormBody.Builder()// body指 php 問號後面代入的參數
                .add("selefunc_string", "insert")// api 裡面的 $selefunc=$_REQUEST['selefunc_string'];

                .add("eld001", query_0)
                .add("eld002", query_1)
                .add("eld004", query_2)
                .add("eld005", query_3)
                .add("eld006", query_4)

//                跳過 query_5 新增不用 ID
                .add("rec002", query_6)
                .add("rec003", query_7)
                .add("rec004", query_8)
                .add("rec005", query_9)
                .add("rec006", query_10)
                .add("rec007", query_11)
                .add("rec008", query_12)
                .add("rec009", query_13)
                .add("rec010", query_14)
                .add("rec011", query_15)
                .add("rec012", query_16)
                .add("rec013", query_17)
                .add("rec014", query_18)
                .add("rec015", query_19)
                .add("rec016", query_20)
                .add("rec017", query_21)
                .add("rec018", query_22)

                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)// 回值用 post
                .build();

        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值

        try (Response response = client.newCall(request).execute()) {// 執行 https 命令 ( php api )
            httpstate = response.code();
            Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---更新資料--------------------------------------------------------------
    public static void executeUpdate_re(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_re;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);
        String query_5 = query_string.get(5);
        String query_6 = query_string.get(6);
        String query_7 = query_string.get(7);
        String query_8 = query_string.get(8);
        String query_9 = query_string.get(9);
        String query_10 = query_string.get(10);
        String query_11 = query_string.get(11);
        String query_12 = query_string.get(12);
        String query_13 = query_string.get(13);
        String query_14 = query_string.get(14);
        String query_15 = query_string.get(15);
        String query_16 = query_string.get(16);
        String query_17 = query_string.get(17);
        String query_18 = query_string.get(18);
        String query_19 = query_string.get(19);
        String query_20 = query_string.get(20);
        String query_21 = query_string.get(21);
        String query_22 = query_string.get(22);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "update")

                .add("eld001", query_0)
                .add("eld002", query_1)
                .add("eld004", query_2)
                .add("eld005", query_3)
                .add("eld006", query_4)

                .add("rec001", query_5)
                .add("rec002", query_6)
                .add("rec003", query_7)
                .add("rec004", query_8)
                .add("rec005", query_9)
                .add("rec006", query_10)
                .add("rec007", query_11)
                .add("rec008", query_12)
                .add("rec009", query_13)
                .add("rec010", query_14)
                .add("rec011", query_15)
                .add("rec012", query_16)
                .add("rec013", query_17)
                .add("rec014", query_18)
                .add("rec015", query_19)
                .add("rec016", query_20)
                .add("rec017", query_21)
                .add("rec018", query_22)

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
            Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---刪除資料--------------------------------------------------------------
    public static void executeDelete_re(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl = connect_ip_re;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string", "delete")
                .add("rec001", query_0)
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
            Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
