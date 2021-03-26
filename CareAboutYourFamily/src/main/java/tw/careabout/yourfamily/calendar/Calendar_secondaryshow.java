package tw.careabout.yourfamily.calendar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import tw.careabout.yourfamily.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Calendar_secondaryshow extends AppCompatActivity implements View.OnClickListener {


    private Intent intent=new Intent();
    private TextView edit001,edit004;

    private Calendar_editDbHelper dbHper,U_dbHper,G_dbHper;

    private static String DB_Name = "";
    private static final String DB_TABLE = "cal100";
    private static final int DBversion=1;
    private Menu menu;
    private TextView edit005;
    private LinearLayout sv001;
    private TextView edit006;

    private TextView edit008;
    private TextView edit009;
    private TextView edit010;
    private TextView edit011;
    private Button b001;
    private Button b002;
    private TextView edit012;
    private TextView edit013;
    private MenuItem cmi001,cmi002,cmi003;
    private int servermsgcolor=0;
    private String ser_msg="";
    private EditText edit002;
    private EditText edit003;
    private String[] mem=new String[14];
    private GoogleSignInAccount account;
    private Handler handler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_secondaryshow);
        initdb();
        setupViewComponent();

    }
    private void enableStrictMode(Context context) {
        //-------------抓取遠端資料庫設定執行續這段是官方的------------------------------
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());
    }

    private void initdb() {
        if (account != null) {
            DB_Name = "CAYF.db";
            if (U_dbHper == null) {
                U_dbHper = new Calendar_editDbHelper(getApplicationContext(), DB_Name, null, DBversion);
                U_dbHper.creatfile();
            }
//            if (U_home_dpHper == null) {
//                U_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
//                U_home_dpHper.createHomeTable();
//            }
            dbHper = U_dbHper;
//            home_dpHper = U_home_dpHper;
        }
        if (account == null) {
            DB_Name = "guest.db";
            if (G_dbHper == null) {
                G_dbHper = new Calendar_editDbHelper(getApplicationContext(), DB_Name, null, DBversion);
                G_dbHper.creatfile();
            }
//            if (G_home_dpHper == null) {
//                G_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
//                G_home_dpHper.createHomeTable();
//            }
            dbHper = G_dbHper;
//            home_dpHper = G_home_dpHper;
        }
    }

    private void setupViewComponent() {
//        menuitem002=
        edit001=(TextView)findViewById(R.id.calendar_secondaryshow_t001);
        edit002=(EditText)findViewById(R.id.calendar_secondaryshow_t002);
        edit003=(EditText)findViewById(R.id.calendar_secondaryshow_t003);
        edit004=(TextView)findViewById(R.id.calendar_secondaryshow_t004);
        edit005=(TextView)findViewById(R.id.calendar_secondaryshow_t005);
        edit006=(TextView)findViewById(R.id.calendar_secondaryshow_t006);

        edit008=(TextView)findViewById(R.id.calendar_secondaryshow_t008);
        edit009=(TextView)findViewById(R.id.calendar_secondaryshow_t009);
        edit010=(TextView)findViewById(R.id.calendar_secondaryshow_t010);
        edit011=(TextView)findViewById(R.id.calendar_secondaryshow_t011);
        edit012=(TextView)findViewById(R.id.calendar_secondaryshow_t012);
        edit013=(TextView)findViewById(R.id.calendar_secondaryshow_t013);



        b001=(Button)findViewById(R.id.calendar_secondaryshow_b001);
        b002=(Button)findViewById(R.id.calendar_secondaryshow_b002);

        //-----設按鍵
        edit009.setOnClickListener(this);
        edit010.setOnClickListener(this);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        //==========
        buttonhidden1();

        sv001=(LinearLayout)findViewById(R.id.calendar_secondaryshow_linla001);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int newscrollheight = displayMetrics.heightPixels * 80 / 100; // 設定ScrollView使用尺寸的4/5
//        int newscrollwidth = displayMetrics.widthPixels * 100 / 100;
//        int newscrollRheight=displayMetrics.heightPixels * 60 / 100;
//        int newscrollRwidth=displayMetrics.widthPixels * 100 / 100;
//        sv001.getLayoutParams().height = newscrollheight;
//        sv001.getLayoutParams().width = newscrollwidth;
//        sv001.setLayoutParams(sv001.getLayoutParams());
//        edit003.getLayoutParams().height = newscrollRheight;
//        edit003.getLayoutParams().width = newscrollRwidth;
//        edit003.setLayoutParams(sv001.getLayoutParams());




        //-----設按鍵
//        edit001.setOnClickListener(this);
//        edit003.setOnClickListener(this);
//        edit004.setOnClickListener(this);
        //==========



//        String cst02=edit002.getText().toString();
//
//        SpannableString sp=new SpannableString(cst02);
//
//        edit002.setText(sp);
//        edit003.setText("ej/4"+Integer.toString(dbHper.RecCount()));

//        int a=0;
    }

    @Override
    public void onClick(View v) {
        Calendar loi =Calendar.getInstance();
        switch(v.getId()){
            //=============修改=========================
            case R.id.calendar_secondaryshow_b001:
//                String msg="";
//                String t_id = edit005.getText().toString().trim();
//                String t_title = edit002.getText().toString().trim();
//                String t_content = edit003.getText().toString().trim();
//                String t_date = edit009.getText().toString().trim();
//                String t_time = edit010.getText().toString().trim();
//                String t_person= edit013.getText().toString().trim();
                if (account!=null){
                    buttonhidden();
                    final ProgressDialog progDlg = new ProgressDialog(this);
                    progDlg.setTitle(getString(R.string.info_wait));
                    progDlg.setMessage(getString(R.string.info_load));
                    progDlg.setIcon(android.R.drawable.presence_away);
                    progDlg.setCancelable(false);
                    progDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progDlg.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(udpUIRunnable);
                            progDlg.cancel();
                        }

                    }).start();
                }
                if (account==null){
                    sql_update();
                    Intent intent001 = this.getIntent();
                    Bundle bundle=this.getIntent().getExtras();
                    String[] arrayop=bundle.getStringArray("key_oplist");
                    String cstitle=arrayop[1];
                    edit005.setText(arrayop[0]);
                    String[] backcldsec=dbHper.FindRec(edit005.getText().toString());
                    edit002.setText(backcldsec[1]);
                    edit003.setText(backcldsec[2]);
                    edit009.setText(backcldsec[3]);
                    edit010.setText(backcldsec[4]);
                    edit013.setText(mem[4]);
                    buttonhidden();
                }
//                dbmysql();
//                int rowsAffected=dbHper.updateRec(t_id,t_title,t_content,t_date,t_time);
//                if (rowsAffected==-1){
//                    msg="資料表已清空，無法修改!";
//                }else if (rowsAffected==0){
//                    msg="找不到想修改的紀錄，無法修改!";
//                }else{
//                    msg="紀錄已被修改!";
////                    recSet=dbHper.getRecSet();
////                    showRec(index);
//                }
//                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
//                finish();

                break;
            case R.id.calendar_secondaryshow_b002://取消修改
                if (account!=null){
                    cldsecresume();
                    buttonhidden();
                }
                if (account==null){
                    String[] backcldsec=dbHper.FindRec(edit005.getText().toString());
                    edit002.setText(backcldsec[1]);
                    edit003.setText(backcldsec[2]);
                    edit009.setText(backcldsec[3]);
                    edit010.setText(backcldsec[4]);
                    edit013.setText(mem[4]);
                    buttonhidden();
                }
                break;

            case R.id.calendar_secondaryshow_t009:

//                Toast.makeText(getApplicationContext(),"K",Toast.LENGTH_SHORT).show();
                int yy=loi.get(java.util.Calendar.YEAR);
                int mm=loi.get(java.util.Calendar.MONTH);
                int dd=loi.get(java.util.Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicDig = new DatePickerDialog(
                        this,
                        datapicdiglistener,loi.get(java.util.Calendar.YEAR)
                        ,
                        loi.get(java.util.Calendar.MONTH),
                        loi.get(Calendar.DAY_OF_MONTH)
                );
                datePicDig.setTitle(getString(R.string.calendar_edit_datetitle));
                datePicDig.setMessage(getString(R.string.calendar_edit_datemessage));
                datePicDig.setCancelable(false);
                datePicDig.show();
                break;
            case R.id.calendar_secondaryshow_t010:
                TimePickerDialog timePicDig = new TimePickerDialog(
                        this, timepicdiglistener,
                        loi.get(java.util.Calendar.HOUR_OF_DAY),
                        loi.get(java.util.Calendar.MINUTE),false
                );
                timePicDig.setTitle(getString(R.string.calendar_edit_timetitle));
                timePicDig.setMessage(getString(R.string.calendar_edit_timemessage));
                timePicDig.setCancelable(false);
                timePicDig.show();
                break;
        }
    }

    Runnable   udpUIRunnable=new  Runnable(){
        @Override
        public void run() {
            mysql_update();
            dbmysql();
            dbHper.RecCount();
            cldsecresume();
        }
    };

    //================================================================================

    private DialogInterface.OnClickListener aldBtListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String msg="";
            switch (which){
                case BUTTON_POSITIVE:
                    Toast.makeText(getApplicationContext(), "確認刪除", Toast.LENGTH_SHORT).show();
                    String deleteID=edit005.getText().toString().trim();
                    String a=deleteID;
                    if(account!=null){
                        mysql_del();
                    }
                    if (account==null){
                        sql_del();
                    }
                    finish();
                    break;
                case BUTTON_NEGATIVE:
                    Toast.makeText(getApplicationContext(), "取消刪除", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    //================================================================================
    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }
    //=========結束時關掉======================
    @Override
    protected void onStop() {
        super.onStop();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }
    //=========重啟時開啟======================
    @Override
    protected void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(this);

        initdb();
//        if (dbHper == null) {
//            dbHper = new Calendar_editDbHelper(this, DB_Name, null, DBversion);
//        }
        mem=dbHper.FindMEMBER();

        cldsecresume();


    }

    //日期監聽
    private DatePickerDialog.OnDateSetListener datapicdiglistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String set_date = year + "-" + (month + 1) + "-" + dayOfMonth;
            String set_data_date =""+ year + (month + 1) + dayOfMonth;
            edit009.setText(set_date);
        }
    };
    //時間監聽
    private TimePickerDialog.OnTimeSetListener timepicdiglistener= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String set_time = hourOfDay + ":" + minute ;
            String set_data_time = String.format("%02d",hourOfDay)+ ":" + String.format("%02d",minute) ;
            edit010.setText(set_data_time);


        }
    };

    private void cldsecresume() {
        Intent intent001 = this.getIntent();
        Bundle bundle=this.getIntent().getExtras();
        String[] arrayop=bundle.getStringArray("key_oplist");
        String cstitle=arrayop[1];
//        String calendar_secondaryshow_title=intent001.getExtras(bundle);
        edit002.setText(arrayop[1]);
        edit003.setText(arrayop[2]);
        edit005.setText(arrayop[0]);
        edit009.setText(arrayop[3]);
        edit010.setText(arrayop[4]);
        edit013.setText(mem[4]);
        if (account!=null){
             new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dbmysql();
                dbHper.RecCount();
                String[] backcldsec=dbHper.FindRec(edit005.getText().toString());
                edit002.setText(backcldsec[1]);
                edit003.setText(backcldsec[2]);
//      edit005.setText(arrayop[0]);
                edit009.setText(backcldsec[3]);
                edit010.setText(backcldsec[4]);
//        int backid=Integer.parseInt(backcldsec[5]);
                edit013.setText(mem[4]);
//                Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();

            }
        }, 500);
        }
        if (account==null){
            dbHper.RecCount();
            String[] backcldsec=dbHper.FindRec(edit005.getText().toString());
            edit002.setText(backcldsec[1]);
            edit003.setText(backcldsec[2]);
//      edit005.setText(arrayop[0]);
            edit009.setText(backcldsec[3]);
            edit010.setText(backcldsec[4]);
//        int backid=Integer.parseInt(backcldsec[5]);
            edit013.setText(mem[4]);
//            Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
        }


//        dbmysql();
    }

    //======================sql===============================================
    private void sql_update() {
        String t_id = edit005.getText().toString().trim();
        String t_title = edit002.getText().toString().trim();
        String t_content = edit003.getText().toString().trim();
        String t_date = edit009.getText().toString().trim();
        String t_time = edit010.getText().toString().trim();
        String t_person = mem[0];
        String msg="";

        int rowsAffected=dbHper.updateRec(t_id,t_title,t_content,t_date,t_time);
        if (rowsAffected==-1){
            msg="資料表已清空，無法修改!";
        }else if (rowsAffected==0){
            msg="找不到想修改的紀錄，無法修改!";
        }else{
            msg="紀錄已被修改!";
            dbHper.getRecSet();
        }
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }

    private void sql_del() {
        String t_id = edit005.getText().toString().trim();
        String msg ="";
        int rowsAffected=dbHper.deleteRec(t_id);
        if (rowsAffected==-1){
            msg="資料表已清空，無法刪除!";
        }else if (rowsAffected==0){
            msg="找不到想修改的紀錄，無法刪除!";
        }else{
            msg="紀錄已被刪除!";
            dbHper.getRecSet();
        }
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();

    }



    //    ====================匯入mysql==========================================
    private void dbmysql() {
        String sqlctl = "SELECT * FROM cal100 WHERE cal006 = "+mem[0]+" ORDER BY cal001 DESC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Calendar_DBConnector.executeQuery(nameValuePairs);

            //=================================================
            chk_httpstate();
            //=================================================

            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
//--------------------------------------------------------
                int rowsAffected = dbHper.clearRec();                 // 匯入前,刪除所有SQLite資料
//--------------------------------------------------------
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
                        // -------------------------------------------------------------------
                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_m(newRow);
//                    Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            } else {
//                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            dbHper.getRecSet();  //重新載入SQLites
//            index=0;

            // --------------------------------------------------------
        } catch (Exception e) {
//            Log.d(TAG, e.toString());
        }
    }

    //==============http相關的東東============================

    private void chk_httpstate() {
        ////-------------------------------
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)
//        String ser_msg="";
//        int servermsgcolor=0;
        if (Calendar_DBConnector.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + Calendar_DBConnector.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
//                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",
//                        Toast.LENGTH_SHORT).show();
        } else {
            int checkcode = Calendar_DBConnector.httpstate / 100;
            switch (checkcode) {
                case 1:
                    ser_msg = "資訊回應(code:" + Calendar_DBConnector.httpstate + ") ";
                    break;
                case 2:
                    ser_msg = "已經完成由伺服器會入資料(code:" + Calendar_DBConnector.httpstate + ") ";
                    break;
                case 3:
                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + Calendar_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + Calendar_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + Calendar_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
            }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }
        if (Calendar_DBConnector.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + Calendar_DBConnector.httpstate + ") ";
        }
//        b_servermsg.setText(ser_msg);
//        b_servermsg.setTextColor(servermsgcolor);

        //-------------------------------------------------------------------

    }

    private void mysql_del() {
        String t_id= edit005.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(t_id);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Calendar_DBConnector.executeDelet(nameValuePairs);
//-----------------------------------------------
    }

    private void mysql_update() {
        String t_id = edit005.getText().toString().trim();
        String t_title = edit002.getText().toString().trim();
        String t_content = edit003.getText().toString().trim();
        String t_date = edit009.getText().toString().trim();
        String t_time = edit010.getText().toString().trim();
        String t_person = mem[0];
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(t_id);
        nameValuePairs.add(t_title);
        nameValuePairs.add(t_content);
        nameValuePairs.add(t_date);
        nameValuePairs.add(t_time);
        nameValuePairs.add(t_person);

        try {
            Thread.sleep(1000); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Calendar_DBConnector.executeUpdate( nameValuePairs);
//-----------------------------------------------
    }

    //=============按鈕顯示===================

    private void buttonshow() {
        b001.setVisibility(View.VISIBLE);
        b002.setVisibility(View.VISIBLE);
        edit011.setVisibility(View.VISIBLE);
        cmi001.setVisible(false);
        cmi002.setVisible(false);
        cmi003.setVisible(false);
        b001.setClickable(true);
        b002.setClickable(true);
        edit009.setClickable(true);
        edit010.setClickable(true);
        edit002.setEnabled(true);
        edit003.setEnabled(true);
    }
    private void buttonhidden() {
        b001.setVisibility(View.INVISIBLE);
        b002.setVisibility(View.INVISIBLE);
        edit011.setVisibility(View.INVISIBLE);
        b001.setClickable(false);
        b002.setClickable(false);
        edit002.setEnabled(false);
        edit003.setEnabled(false);
        edit009.setClickable(false);
        edit010.setClickable(false);
        cmi001.setVisible(true);
        cmi002.setVisible(true);
        cmi003.setVisible(true);

    }
    private void buttonhidden1() {
        b001.setVisibility(View.INVISIBLE);
        b002.setVisibility(View.INVISIBLE);
        b001.setClickable(false);
        b002.setClickable(false);
        edit011.setVisibility(View.INVISIBLE);
        edit002.setEnabled(false);
        edit003.setEnabled(false);
        edit009.setClickable(false);
        edit010.setClickable(false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        this.menu=menu;
        cmi001=menu.findItem(R.id.calendar_menu_item001);
        cmi002=menu.findItem(R.id.calendar_menu_item002);
        cmi003=menu.findItem(R.id.calendar_menu_item003);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent menu_it =new Intent();
        switch (item.getItemId()) {
            case R.id.calendar_menu_item001://修改
////                Toast.makeText(getApplicationContext(),"還沒做出來喔!!!",Toast.LENGTH_SHORT).show();
//                Intent intent001 = this.getIntent();
//                Bundle bundle=this.getIntent().getExtras();
//                String[] arrayop=bundle.getStringArray("key_oplist");
//                String b=arrayop[1];
//
//                Intent intent002=new Intent();
//                        bundle.putStringArray("key_oplist2",arrayop);
////
//                intent002.putExtras(bundle);
////                int b=0;
//                intent002.setClass(Calendar_secondaryshow.this,Calendar_secondaryshowupdate.class);
//                startActivity(intent002);
                buttonshow();
                break;
            case R.id.calendar_menu_item002://刪除
//                Toast.makeText(getApplicationContext(),"還沒做出來喔!!!",Toast.LENGTH_SHORT).show();
                Calendar_MyAlertDialog aldDial = new Calendar_MyAlertDialog(Calendar_secondaryshow.this);
                aldDial.setTitle("刪除");
                aldDial.setMessage("您確定要刪除嗎?");
                aldDial.setIcon(android.R.drawable.ic_dialog_info);
                aldDial.setCancelable(false); //返回鍵關閉
                aldDial.setButton(BUTTON_POSITIVE, "確定刪除", aldBtListener);
                aldDial.setButton(BUTTON_NEGATIVE, "取消刪除", aldBtListener);
                aldDial.show();
//
                break;
            case R.id.calendar_menu_item003://返回


                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }
}
