package tw.careabout.yourfamily.home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeDBconnector {

    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    public static int httpstate = 0;
    private static int accountState;
    private static JSONObject data = new JSONObject();
    private static String userEmail;


    //------檢查會員是否已存在
    public static int checkMemberTable(String email) {
        userEmail = email;
        postUrl=connect_ip_r ;
        FormBody body = new FormBody.Builder()
                .add("mem004",email)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        //==============================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        //==============================================

        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            //解析 json
            // { }大括號為object，[ ]中括號為array
            // object裡可以包 key:value，也可以包 array(  { key:value } or { array[] }  )
            // array裡面包 object ( array[ {...},{...} ] )
            // 兩者可以一直循還，構成 json資料
            //String member_r_api_echo =  response.body().string();
            JSONObject member_r_api_echo = new JSONObject(response.body().string());
            String message = member_r_api_echo.getString("message");
            if( message.equals("資料查詢成功") ){
                JSONArray dataArray = member_r_api_echo.getJSONArray("data");
                data = dataArray.getJSONObject(0);
                accountState = 1;
            }
            else if( message.equals("查無此人") ){
                accountState = 0;
            }

            int aa = 0;
            return accountState;
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        accountState = -1;
        return accountState;
    }

    //------JsonObeject會員資料回傳
    public static JSONObject getMemberData(String email) {
        postUrl=connect_ip_r ;
        FormBody body = new FormBody.Builder()
                .add("mem004",email)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        //==============================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        //==============================================

        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            //解析 json
            // { }大括號為object，[ ]中括號為array
            // object裡可以包 key:value，也可以包 array(  { key:value } or { array[] }  )
            // array裡面包 object ( array[ {...},{...} ] )
            // 兩者可以一直循還，構成 json資料
            //String member_r_api_echo =  response.body().string();
            JSONObject member_r_api_echo = new JSONObject(response.body().string());
            String message = member_r_api_echo.getString("message");
            if( message.equals("資料查詢成功") ){
                JSONArray dataArray = member_r_api_echo.getJSONArray("data");
                data = dataArray.getJSONObject(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    //------新增
    public static String executeInsert(ArrayList<String> query_string, int mem007) {
        postUrl=connect_ip_c;

        for(int i=0; i < query_string.size(); i++){
            if(query_string.get(i)==null){
               query_string.set(i,"guest");
            }
        }

        FormBody body = new FormBody.Builder()  //body指 php帶入的參數 (POST給 php的參數)
                .add("mem002",query_string.get(0))
                .add("mem003", query_string.get(1))
                .add("mem004", query_string.get(2))
                .add("mem005", query_string.get(3))
                .add("mem006", query_string.get(4))
                .add("mem007", Integer.toString(mem007))
                .add("mem008", query_string.get(5))
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        //==============================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        //==============================================

        try (Response response = client.newCall(request).execute()) {  //執行 https的命令
            httpstate = response.code();
            JSONObject member_c_api_Msg = new JSONObject(response.body().string());
            String message = member_c_api_Msg.getString("message");
            return message;
        }catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        int bb = 0;
        return result;

    }

    //------更新
    public static String executeUpdate(ArrayList<String> query_string) {
        postUrl=connect_ip_u ;

        for(int i=0; i < query_string.size(); i++){
            if(query_string.get(i)==null){
                query_string.set(i,"guest");
            }
        }

        FormBody body = new FormBody.Builder()  //body指 php帶入的參數 (POST給 php的參數)
                .add("mem002",query_string.get(0))
                .add("mem003", query_string.get(1))
                .add("mem004", query_string.get(2))
                .add("mem005", query_string.get(3))
                .add("mem006", query_string.get(4))
                .add("mem008", query_string.get(5))
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        //==============================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        //==============================================

        try (Response response = client.newCall(request).execute()) {  //執行 https的命令
            httpstate = response.code();
            JSONObject member_u_api_Msg = new JSONObject(response.body().string());
            String message = member_u_api_Msg.getString("message");
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //------刪除
//    public static String executeDelet(ArrayList<String> query_string) {
//        //        OkHttpClient client = new OkHttpClient();
//        postUrl=connect_ip ;
//        //--------------
//        String query_0 = query_string.get(0);
//        FormBody body = new FormBody.Builder()
//                .add("selefunc_string","delete")
//                .add("id", query_0)
//                .build();
//        //--------------
//        Request request = new Request.Builder()
//                .url(postUrl)
//                .post(body)
//                .build();
//        //==============================================
//        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
//        httpstate = 0;   //設 httpcode初始值
//        //==============================================
//
//        try (Response response = client.newCall(request).execute()) {
//            httpstate = response.code();
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }


    // ====================Hosting=================
    //static String connect_ip = "https://aboutyfc.com/android_mysql_connect/android_connect_db_all.php";
    // ====================000webhost==============
    static String connect_ip_c = "https://aboutyfc.com/cayf_android_mysql_connect/member_c_api.php";
    static String connect_ip_r = "https://aboutyfc.com/cayf_android_mysql_connect/member_r_api.php";
    static String connect_ip_u= "https://aboutyfc.com/cayf_android_mysql_connect/member_u_api.php";



}
