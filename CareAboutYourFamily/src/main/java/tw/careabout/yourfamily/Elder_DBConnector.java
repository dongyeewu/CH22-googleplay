package tw.careabout.yourfamily;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Elder_DBConnector {

    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    public static int httpstate=0;
    //---------------------------------------------------------

    static String connect_ip = "https://aboutyfc.com/cayf_android_mysql_connect/elder24_android_connect_db_all.php";
   //=====================查詢==================================
    public static String executeQuery(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_00 = query_string.get(0);
        //?後面的東西在這是用這個寫法
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_00)
                .build();
    //-----------------------------------------------------------------------------------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        //==================================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        //==================================================
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
//        return null;
    }
//==========================新增=======================================
    public static String executeInsert(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        //將main寫的陣列抓出來
//        String query_00 = query_string.get(0);
        String query_01 = query_string.get(0);
        String query_02 = query_string.get(1);
        String query_03 = query_string.get(2);
        String query_04 = query_string.get(3);
        String query_05 = query_string.get(4);
        String query_06 = query_string.get(5);
//        String query_06 = query_string.get(6);
//        String query_07 = query_string.get(7);
//        String query_08 = query_string.get(8);
//        String query_09 = query_string.get(9);
//        String query_10 = query_string.get(10);
//        String query_11 = query_string.get(11);
//        String query_12 = query_string.get(12);
//        String query_13 = query_string.get(13);
//        String query_14 = query_string.get(14);


//================okhttp的寫法======
// =https://.................................php?(.........................);      body是指php?後代的參數==========
//        insert試紙PHP的switch case
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("eld002", query_01)
                .add("eld003", query_02)
                .add("eld004", query_03)
                .add("eld005", query_04)
                .add("eld006", query_05)
                .add("eld007", query_06)
//                .add("cal006", query_05)
//                .add("cal007", query_06)
//                .add("cal008", query_07)
//                .add("cal009", query_08)
//                .add("cal010", query_09)
//                .add("cal011", query_10)
//                .add("cal012", query_11)
//                .add("cal013", query_12)
//                .add("cal014", query_13)
//                .add("cal015", query_14)
                .build();
//--------------用post是要回職
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        //==================================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        //==================================================
        //=============執行http命令 就是執行連接的 php=====================
        try (Response response = client.newCall(request).execute()) {
            httpstate=response.code();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
//    public static String executeUpdate(ArrayList<String> query_string) {
////        OkHttpClient client = new OkHttpClient();
//        postUrl=connect_ip ;
//        //--------------
//        //將main寫的陣列抓出來
//        String query_0 = query_string.get(0);
//        String query_1 = query_string.get(1);
//        String query_2 = query_string.get(2);
//        String query_3= query_string.get(3);
//
////================okhttp的寫法======
//// =https://.................................php?(.........................);      body是指php?後代的參數==========
////        insert試紙PHP的switch case
//        FormBody body = new FormBody.Builder()
//                .add("selefunc_string","update")
//                .add("id", query_0)
//                .add("name", query_1)
//                .add("grp", query_2)
//                .add("address", query_3)
//                .build();
////--------------用post是要回職
//        Request request = new Request.Builder()
//                .url(postUrl)
//                .post(body)
//                .build();
//        //=============執行http命令 就是執行連接的 php=====================
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//
//    }
//---更新資料--------------------------------------------------------------
public static String executeUpdate(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
    postUrl=connect_ip ;

    //--------------資料新增要改
    String query_00 = query_string.get(0);
    String query_01 = query_string.get(1);
    String query_02 = query_string.get(2);
    String query_03 = query_string.get(3);
    String query_04 = query_string.get(4);
    String query_05 = query_string.get(5);
//    String query_06 = query_string.get(6);
//    String query_07 = query_string.get(7);
//    String query_08 = query_string.get(8);
//    String query_09 = query_string.get(9);
//    String query_10 = query_string.get(10);
//    String query_11 = query_string.get(11);
//    String query_12 = query_string.get(12);
//    String query_13 = query_string.get(13);
//    String query_14 = query_string.get(14);

    FormBody body = new FormBody.Builder()
            .add("selefunc_string","update")
            .add("eld001", query_00)
            .add("eld002", query_01)
            .add("eld003", query_02)
            .add("eld004", query_03)
            .add("eld005", query_04)
            .add("eld006", query_05)
//            .add("eld007", query_06)
//            .add("cal006", query_05)
//            .add("cal007", query_06)
//            .add("cal008", query_07)
//            .add("cal009", query_08)
//            .add("cal010", query_09)
//            .add("cal011", query_10)
//            .add("cal012", query_11)
//            .add("cal013", query_12)
//            .add("cal014", query_13)
//            .add("cal015", query_14)
            .build();
//--------------
    Request request = new Request.Builder()
            .url(postUrl)
            .post(body)
            .build();
    //==================================================
    // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
    httpstate = 0;   //設 httpcode初始值
    //==================================================
    try (Response response = client.newCall(request).execute()) {
        httpstate=response.code();
        return response.body().string();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return result;
}
//=========================================================
//---刪除資料--------------------------------------------------------------
public static String executeDelet(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
    postUrl=connect_ip ;
    //--------------
    String query_00 = query_string.get(0);

    FormBody body = new FormBody.Builder()
            .add("selefunc_string","delete")
            .add("eld001", query_00)
            .build();
//--------------
    Request request = new Request.Builder()
            .url(postUrl)
            .post(body)
            .build();
    //==================================================
    // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
    httpstate = 0;   //設 httpcode初始值
    //==================================================
    try (Response response = client.newCall(request).execute()) {
        httpstate=response.code();
        return response.body().string();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return result;
}



}
