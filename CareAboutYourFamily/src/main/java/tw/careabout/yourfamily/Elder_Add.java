package tw.careabout.yourfamily;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import tw.careabout.yourfamily.calendar.Calendar_DBConnector;
import tw.careabout.yourfamily.calendar.Calendar_edit;
import tw.careabout.yourfamily.calendar.Calendar_editDbHelper;
import tw.careabout.yourfamily.home.HomeDbHelper;

public class Elder_Add extends AppCompatActivity implements View.OnClickListener {
    private static String DB_FILE = "";
    private static final int DBversion=1;
    private Elder_editDbHelper dbHper, G_dbHper, U_dbHper;
    private HomeDbHelper home_dpHper, G_home_dpHper, U_home_dpHper;
    private Spinner ea_sp001,ea_sp002;
    private Menu menu;
    private Button b001,b002;
    private TextView t005,t007;
    private EditText e001;
    private String sp001;
    private String e_name,e_buidler,e_sex,e_birth,e_blod,e_group;
    private String[] mem=new String[14];
    private GoogleSignInAccount account;

//    private int ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        account = GoogleSignIn.getLastSignedInAccount(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elder_add);
//        setupCAYFbarConponent();
//        setupSignView();
        setupViewComponent();
        initdb();
    }


    private void enableStrictMode(Elder_Add elder_add) {
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

    private void setupViewComponent() {
        ea_sp001=(Spinner)findViewById(R.id.elder_add_sp001);
        ea_sp002=(Spinner)findViewById(R.id.elder_add_sp002);
        ArrayAdapter<CharSequence> adapSexList = ArrayAdapter.createFromResource(this, R.array.elder_add_sp0001, android.R.layout.simple_spinner_item);
        //設定spinner 還沒跳出視窗的畫面
        adapSexList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ea_sp001.setAdapter(adapSexList);
        ea_sp001.setOnItemSelectedListener(ea_sp001ON);
        ArrayAdapter<CharSequence> adapBoldList = ArrayAdapter.createFromResource(this, R.array.elder_add_sp0002, android.R.layout.simple_spinner_item);
        adapBoldList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ea_sp002.setAdapter(adapBoldList);
        ea_sp002.setOnItemSelectedListener(ea_sp002ON);
        b001=(Button)findViewById(R.id.elder_add_b001);
        b002=(Button)findViewById(R.id.elder_add_b002);
        t005=(TextView)findViewById(R.id.elder_add_t005);
        t007=(TextView)findViewById(R.id.elder_add_t007);
        e001=(EditText)findViewById(R.id.elder_add_e001);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        t005.setOnClickListener(this);




    }
    private AdapterView.OnItemSelectedListener ea_sp001ON =new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp001=parent.getSelectedItem().toString().trim();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener ea_sp002ON =new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(View v) {
        java.util.Calendar now = java.util.Calendar.getInstance();
        switch (v.getId()){

            case R.id.elder_add_b001:
                this.finish();
                break;
            case R.id.elder_add_b002:
                 e_name = e001.getText().toString().trim();
                 e_birth = t005.getText().toString().trim();
                 e_buidler = t007.getText().toString().trim();
                 e_sex = ea_sp001.getSelectedItem().toString().trim();
                 e_blod = ea_sp002.getSelectedItem().toString().trim();

                if (e_name.equals("")||e_birth.equals("選擇生日")){
                    Toast.makeText(getApplicationContext(),"請不要空白，無法新增!!",Toast.LENGTH_SHORT).show();
                    return;
                }
//                long rowID=dbHper.insertRec(e_name,e_buidler,e_sex,e_blod,e_birth,e_group);
//                String msg;
//                if (rowID!=-1){
//                    msg="新增記錄成功";
//                }else{
//                    msg="新增記錄失敗";
//                }
                if( Integer.valueOf(mem[0])!=0 ){
                    mysql_insert();
                    dbmysql();
                }
                if( Integer.valueOf(mem[0])==0 ){

                    int elderNumber = dbHper.RecCount();
                    ContentValues newRow = new ContentValues();
                    e_group = mem[6];

                    newRow.put("eld001",elderNumber+1); // 動態找出有幾個欄位
                    newRow.put("eld002",e_name);
                    newRow.put("eld003",e_buidler);
                    newRow.put("eld004",e_sex);
                    newRow.put("eld005",e_blod);
                    newRow.put("eld006",e_birth);
                    newRow.put("eld007",e_group);
                    long rowID = dbHper.insertRec_m(newRow);
                    dbHper.getRecSet();  //重新載入SQLites
                }

                this.finish();
                break;
            case R.id.elder_add_t005:
                DatePickerDialog datePicDig = new DatePickerDialog(
                        Elder_Add.this,
                        datapicdiglistener,
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH)
                );
                datePicDig.setTitle(getString(R.string.calendar_edit_datetitle));
                datePicDig.setMessage(getString(R.string.calendar_edit_datemessage));
                datePicDig.setCancelable(false);
                datePicDig.show();
                break;
        }

    }
    //日期監聽
    private DatePickerDialog.OnDateSetListener datapicdiglistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            java.util.Calendar now = java.util.Calendar.getInstance();
            int nowtime = now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH);
            int setttime =month;
            int a=now.get(Calendar.YEAR);
            int b=year;
            int c=0;

            if (now.get(Calendar.YEAR) < year){
                Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();

            }else if (now.get(Calendar.YEAR) == year &&now.get(Calendar.DAY_OF_MONTH) < dayOfMonth){
                Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();
            }else if (now.get(Calendar.DAY_OF_MONTH) < dayOfMonth && now.get(Calendar.MONTH) == month&&now.get(Calendar.YEAR) == year)
            {
                Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();
            }else {
                String set_date = year + "-" + (month + 1) + "-" + dayOfMonth ;
                String set_data_date =""+ year + (month + 1) + dayOfMonth;
                t005.setText(set_date);
            }

//            if (now.get(Calendar.YEAR) >= year){
//                if (now.get(Calendar.MONTH)>=month){
//                    if (now.get(Calendar.DAY_OF_MONTH)>=dayOfMonth){
//                        String set_date = year + "-" + (month + 1) + "-" + dayOfMonth ;
//                        String set_data_date =""+ year + (month + 1) + dayOfMonth;
//                        t005.setText(set_date);
//                    }else {Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();}
//                }else {Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();}
//            }else{
//                Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();
//            }
        }
    }; 

    //=========     這裡是sqli         ============================

    private void initdb() {
        if(account!=null){
            DB_FILE = "CAYF.db";
            if (U_dbHper==null)
            {
                U_dbHper=new Elder_editDbHelper(this,DB_FILE,null,DBversion);
                U_dbHper.creatfile();
            }
            if (U_home_dpHper==null)
            {
                U_home_dpHper=new HomeDbHelper(this,DB_FILE,null,DBversion);
                U_home_dpHper.createHomeTable();
            }

            dbHper = U_dbHper;
            home_dpHper = U_home_dpHper;
        }
        if(account==null){
            DB_FILE = "guest.db";
            if (G_dbHper==null)
            {
                G_dbHper=new Elder_editDbHelper(this,DB_FILE,null,DBversion);
                G_dbHper.creatfile();
            }
            if (G_home_dpHper==null)
            {
                G_home_dpHper=new HomeDbHelper(this,DB_FILE,null,DBversion);
                G_home_dpHper.createHomeTable();
            }

            dbHper = G_dbHper;
            home_dpHper = G_home_dpHper;
        }

    }
    //=======================mysql=====================================
    private void mysql_insert() {
//        String sqlctl = " SELECT *  FROM cal100 ORDER BY id ASC ";
        ArrayList<String> nameValuePairs =new ArrayList<String>();

        e_group = mem[6];

        nameValuePairs.add(e_name);
        nameValuePairs.add(e_buidler);
        nameValuePairs.add(e_sex);
        nameValuePairs.add(e_blod);
        nameValuePairs.add(e_birth);
        nameValuePairs.add(e_group);
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //======真正執行mysql==================

        String result = Elder_DBConnector.executeInsert(nameValuePairs);
//        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
        //-----------------------------------------------

    }
    private void dbmysql() {
//        mem[6]="0";
        String a=mem[6];
        int b=0;
        String sqlctl = "SELECT * FROM elder WHERE eld007 = "+mem[6]+" ORDER BY eld001 DESC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Elder_DBConnector.executeQuery(nameValuePairs);
            int c=0;
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
        } catch (Exception e) {
//            Log.d(TAG, e.toString());
        }
    }
    //================生命週期========================================

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
        initdb();
//        if (dbHper == null) {
//            dbHper = new Elder_editDbHelper(this, DB_FILE, null, DBversion);
//        }
        mem=dbHper.FindMEMBER();
        t007.setText(mem[4]);
    }

    //==================menu=================

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mededit_menu, menu);
        this.menu=menu;
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()) {

            case R.id.action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
