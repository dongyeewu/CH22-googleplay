package tw.careabout.yourfamily.board;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BoardDBconnector {

    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    public static int httpstate = 0;



    //------取得群組留言
    public static ArrayList< JSONObject > getBoardData(String familyID) {
        postUrl=connect_ip_r ;
        FormBody body = new FormBody.Builder()
                .add("boa007",familyID)
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
        ArrayList< JSONObject > boardList = new ArrayList<>();

        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            //解析 json
            // { }大括號為object，[ ]中括號為array
            // object裡可以包 key:value，也可以包 array(  { key:value } or { array[] }  )
            // array裡面包 object ( array[ {...},{...} ] )
            // 兩者可以一直循還，構成 json資料

            JSONObject board_r_api_echo = new JSONObject(response.body().string());
            JSONArray dataArray = board_r_api_echo.getJSONArray("data");
            for(int i=0; i<dataArray.length(); i++){
                JSONObject data = dataArray.getJSONObject(i);
                boardList.add(data);
//                ArrayList<String> item = new ArrayList<>();
//                item.add(data.getString("boa001"));
//                item.add(data.getString("boa002"));
//                item.add(data.getString("boa003"));
//                item.add(data.getString("boa004"));
//                item.add(data.getString("boa005"));
//                item.add(data.getString("boa006"));
//                item.add(data.getString("boa007"));
//                boardList.add(item);
            }

            String message = board_r_api_echo.getString("message");
            if( message.equals("資料查詢成功") ){
                Log.d("api_message",message);
            }
            else if( message.equals("沒有任何留言") ){
                Log.d("api_message",message);
            }

            int aa = 0;

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return boardList;
    }

    //------新增
    public static String executeInsert(String creater, String createTime, String title, String context, int familyID) {
        postUrl=connect_ip_c;

        FormBody body = new FormBody.Builder()  //body指 php帶入的參數 (POST給 php的參數)
                .add("boa002", creater)//創建者
                .add("boa003", createTime)//創建時間
                .add("boa004", title)//標題
                .add("boa005", context)//內容
                .add("boa006", "null") //編輯時間
                .add("boa007", Integer.toString(familyID))//群組
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
            JSONObject board_c_api_Msg = new JSONObject(response.body().string());
            String message = board_c_api_Msg.getString("message");
            return message;
        }catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return result;

    }

    //------更新
    public static int executeUpdate(int id, String title, String context, String editTime) {
        postUrl=connect_ip_u ;
        if( title.equals("") || context.equals("") ){
            return 0;
        }

        FormBody body = new FormBody.Builder()  //body指 php帶入的參數 (POST給 php的參數)
                .add("boa001",String.valueOf(id))
                .add("boa004", title)
                .add("boa005", context)
                .add("boa006", editTime)
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
            JSONObject board_u_api_Msg = new JSONObject(response.body().string());
            String message = board_u_api_Msg.getString("message");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 1;
    }

    //------刪除
    public static String executeDelete(int id) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip_d ;
        //--------------
        FormBody body = new FormBody.Builder()
                .add("boa001", String.valueOf(id))
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
            JSONObject board_d_api_Msg = new JSONObject(response.body().string());
            String message = board_d_api_Msg.getString("message");
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    // ====================Hosting=================
    //static String connect_ip = "https://aboutyfc.com/android_mysql_connect/android_connect_db_all.php";
    // ====================000webhost==============
    static String connect_ip_c = "https://aboutyfc.com/cayf_android_mysql_connect/board_c_api.php";
    static String connect_ip_r = "https://aboutyfc.com/cayf_android_mysql_connect/board_r_api.php";
    static String connect_ip_u= "https://aboutyfc.com/cayf_android_mysql_connect/board_u_api.php";
    static String connect_ip_d= "https://aboutyfc.com/cayf_android_mysql_connect/board_d_api.php";
}
