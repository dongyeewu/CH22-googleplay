package tw.careabout.yourfamily;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import tw.careabout.yourfamily.calendar.Calendar_DBConnector;
import tw.careabout.yourfamily.home.HomeDbHelper;

public class Elder_Update extends AppCompatActivity implements View.OnClickListener {

    private Spinner ea_sp001,ea_sp002;
    private Button b001,b002;
    private TextView t005,t007,t008;
    private EditText e001;
    private Elder_editDbHelper dbHper, G_dbHper , U_dbHper;
    private HomeDbHelper home_dpHper, G_home_dpHper, U_home_dpHper;
    private static String DB_FILE = "";
    private static final int DBversion=1;
    private String e_name,e_birth,e_buidler,e_sex,e_blod,e_id="";;
    private String[] mem=new String[14];
    private GoogleSignInAccount account;



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

    private void enableStrictMode(Elder_Update elder_update) {
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
//        Intent intentg=this.getIntent();
        Bundle bundle=this.getIntent().getExtras();
        String[] arrayop=bundle.getStringArray("key_oiclist");

        e_id = arrayop[0];

        e001.setText(arrayop[1]);
        if (arrayop[3].equals("女")){
            ea_sp001.setSelection(1);
        }else {
            ea_sp001.setSelection(0);
        }
        if (arrayop[4].equals("O")){
            ea_sp002.setSelection(0);
        }else if (arrayop[4].equals("A")){
            ea_sp002.setSelection(1);
        }else if (arrayop[4].equals("B")){
            ea_sp002.setSelection(2);
        }else {
            ea_sp002.setSelection(3);
        }
        t005.setText(arrayop[5]);
        t007.setText(arrayop[2]);







    }

    private AdapterView.OnItemSelectedListener ea_sp001ON =new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            sp001=parent.getSelectedItem().toString().trim();
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
        switch (v.getId()) {

            case R.id.elder_add_b001:
                this.finish();
                break;
            case R.id.elder_add_b002:

                mysql_update();
                if( Integer.valueOf(mem[0])!=0 ){
                    dbmysql();
                }
                this.finish();
//                String mem007testgroup=mem[6];
//                t008.setText(mem007testgroup);
//                t008.setText("1234");
//                e_group =t008.getText().toString().trim();
//                if (e_name.equals("")||e_birth.equals("選擇生日")){
//                    Toast.makeText(getApplicationContext(),"請不要空白，無法新增!!",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                long rowID=dbHper.insertRec(e_name,e_buidler,e_sex,e_blod,e_birth,e_group);
//                String msg;
//                if (rowID!=-1){
//                    msg="新增記錄成功";
//                }else{
//                    msg="新增記錄失敗";
//                }
//                mysql_insert();
//                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
//                this.finish();
//
                break;
            case R.id.elder_add_t005:
                DatePickerDialog datePicDig = new DatePickerDialog(
                        Elder_Update.this,
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

    private void mysql_update() {
        e_name="";
        e_birth="";
        e_buidler="";
        e_sex="";
        e_blod="";

        e_name = e001.getText().toString().trim();
        e_birth = t005.getText().toString().trim();
        e_buidler = t007.getText().toString().trim();
        e_sex = ea_sp001.getSelectedItem().toString().trim();
        e_blod = ea_sp002.getSelectedItem().toString().trim();
//        String t_id = edit005.getText().toString().trim();
//        String t_title = edit002.getText().toString().trim();
//        String t_content = edit003.getText().toString().trim();
//        String t_date = edit009.getText().toString().trim();
//        String t_time = edit010.getText().toString().trim();
//        String t_person = edit013.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(e_id);
        nameValuePairs.add(e_name);
        nameValuePairs.add(e_buidler);
        nameValuePairs.add(e_sex);
        nameValuePairs.add(e_blod);
        nameValuePairs.add(e_birth);

        try {
            Thread.sleep(1000); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if( Integer.valueOf(mem[0])!=0 ){
            String result = Elder_DBConnector.executeUpdate( nameValuePairs);
        }
        if( Integer.valueOf(mem[0])==0 ){
            dbHper.updateRec(
                    e_id,
                    e_name,
                    e_buidler,
                    e_sex,
                    e_blod,
                    e_birth
            );
        }
    }

    //日期監聽
    private DatePickerDialog.OnDateSetListener datapicdiglistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            java.util.Calendar now = java.util.Calendar.getInstance();
            if (now.get(Calendar.YEAR) >= year){
                if (now.get(Calendar.MONTH)>=month){
                    if (now.get(Calendar.DAY_OF_MONTH)>=dayOfMonth){
                        String set_date = year + "-" + (month + 1) + "-" + dayOfMonth ;
                        String set_data_date =""+ year + (month + 1) + dayOfMonth;
                        t005.setText(set_date);
                    }else {Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();}
                }else {Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();}
            }else{
                Toast.makeText(getApplicationContext(),"他是未來人嗎?",Toast.LENGTH_SHORT).show();
            }
//            String set_date = year + "-" + (month + 1) + "-" + dayOfMonth ;
//            String set_data_date =""+ year + (month + 1) + dayOfMonth;
//            t005.setText(set_date);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mem=dbHper.FindMEMBER();
        int a = 0;
    }

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
}
