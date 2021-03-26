package tw.careabout.yourfamily;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Family_DBConnector {
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    public static int httpstate=0;
    //---------------------------------------------------------
    //home// 192.168.43.134
//        static String connect_ip = "http://192.168.43.134/android_mysql_connect/family_db_all.php";

// ------000webhost

    //    static String connect_ip = "https://shinnyteemo0001.000webhostapp.com/android_mysql_connect/family_db_all.php";
    //-----local
//    static String connect_ip = "localhost/android_mysql_connect/android_connect_db_all.php";
    // -------HOSTING-------
static String connect_ip ="https://aboutyfc.com/cayf_android_mysql_connect/family_db_all.php";
    //--------oldpa88---------
//    static String connect_ip = "https://oldpa88.com/android_mysql_connect/android_connect_db_all.php";

    //班長
//    static String connect_ip = "https://mad-muscle.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//副班長
//    static String connect_ip = "https://monachal-bandage.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//    static String connect_ip = "https://bklifetw.com/android_mysql_connect/android_connect_db_all.php";
//08松逸第一組
//        static String connect_ip = "https://shashipage.com/android_mysql_connect/android_connect_db_all_n.php";
//    static String connect_ip = "https://iron61700.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//12其軒
//    static String connect_ip = "https://kartg0203.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
    //26小石第三組
//    static String connect_ip = "https://aboutyfc.com/android_mysql_connect/android_connect_db_all.php";
//    static String connect_ip = "https://volitionary-blocks.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//23皓文
//static String connect_ip = "https://bklifetw.com/android_mysql_connect/android_connect_db_all.php";


    public static String fam_m_r(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------get的值
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
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
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //------------------新增---------------------
    public static String fam_m_c(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);//fam002
        String query_1 = query_string.get(1);//fam004

        //--------------傳送值(?...)
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("fam002", query_0)
                .add("fam004", query_1)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            //---------執行http命令----------
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String mem_m_r(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------get的值
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","mquery")
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
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //------------------更改群組---------------------
    public static String mem_m_u(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------

        //--------------傳送值(?...)
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","change")
                .add("mem001", query_string.get(0))//mem001
                .add("fam005", query_string.get(1))//
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            //---------執行http命令----------
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
    //------------------離開群組---------------------
    public static String mem_m_l(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------

        //--------------傳送值(?...)
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","leave")
                .add("mem001", query_string.get(0))//mem001
                .add("mem005", query_string.get(1))//
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            //---------執行http命令----------
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }


    public static String executeUpdate(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);//tname
        String query_2 = query_string.get(2);//tgrp
        String query_3 = query_string.get(3);//taddr

        //--------------傳送值(?之後)
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update")
                .add("id", query_0)
                .add("name", query_1)
                .add("grp", query_2)
                .add("address", query_3)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            //---------執行http命令----------
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //---刪除資料--------------------------------------------------------------
    public static String executeDelet(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","delete")
                .add("id", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
