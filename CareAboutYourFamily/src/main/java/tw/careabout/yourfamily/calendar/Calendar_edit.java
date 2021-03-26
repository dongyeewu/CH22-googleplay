package tw.careabout.yourfamily.calendar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tw.careabout.yourfamily.R;
import tw.careabout.yourfamily.home.HomeDbHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;



public class Calendar_edit extends AppCompatActivity implements View.OnClickListener{

    private  String DB_Name = "";
    private static final String DB_TABLE = "cal100";
    private static final int DBversion=1;
    private Intent intent=new Intent();
    private Button add001;
    private Button cancel001;
    private Button add002;
    private Button add003;
    private Spinner sp001;
    private String sp001IS;
//    private java.util.Calendar now;
    private String set_date,set_time;
    private TextView t014;
    private TextView t015;
    private Calendar_editDbHelper dbHper,U_dbHper,G_dbHper;
    private EditText e001;
    private EditText e002;
    private EditText e003;
    private String tname;
    private String tcontent;
    private String tdate;
    private String ttime;
    private TextView t016;
    private TextView t017;
    private String[] mem=new String[14];
    private GoogleSignInAccount account;
    private Handler handler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_edit);
        setViewComponent();
        initdb();



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



    private void setViewComponent() {
        add001=(Button)findViewById(R.id.calendar_edit_b004);
        cancel001=(Button)findViewById(R.id.calendar_edit_b003);
        add002=(Button)findViewById(R.id.calendar_edit_b001);
        add003=(Button)findViewById(R.id.calendar_edit_b002);
        sp001 = (Spinner) findViewById(R.id.calendar_edit_sp001);
        e001=(EditText)findViewById(R.id.calendar_edit_e001);
        e002=(EditText)findViewById(R.id.calendar_edit_e002);
        e003=(EditText)findViewById(R.id.calendar_edit_e003);
        t014=(TextView)findViewById(R.id.calendar_edit_t014);
        t015=(TextView)findViewById(R.id.calendar_edit_t015);
        t016=(TextView)findViewById(R.id.calendar_edit_t016);
        t017=(TextView)findViewById(R.id.calendar_edit_t017);
        Intent intent004 = this.getIntent();
        Bundle bundle=this.getIntent().getExtras();
        String getdate001=bundle.getString("key_date1");
        if (getdate001.equals("")){
        }else{t014.setText(getdate001);}
        //設定spinner item 選項
        //做adapter有三個步驟
        //要有底下四行
        //1.設定arrayAdapter
        ArrayAdapter<CharSequence> adapList = ArrayAdapter.createFromResource(this,
                R.array.calendar_edit_sp001, android.R.layout.simple_spinner_item);
        //設定spinner 還沒跳出視窗的畫面
        adapList.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp001.setAdapter(adapList);
        sp001.setOnItemSelectedListener(sp001ON);//一定要寫這一段,要s001去監聽
        //--------設按鍵
        add001.setOnClickListener(this);
        cancel001.setOnClickListener(this);
        add002.setOnClickListener(this);
        add003.setOnClickListener(this);
        t017.setOnClickListener(this);
    }


    //=========     這裡是msq         ============================

//    private void initdb() {
//        if (dbHper==null)
//        {
//            dbHper=new Calendar_editDbHelper(this,DB_Name,null,DBversion);
//            dbHper.creatfile();
//        }
//
//    }
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
    
    //=====================================
    private AdapterView.OnItemSelectedListener sp001ON=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sp001IS=parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
        if (handler!=null){
            handler.removeCallbacks(udpUIRunnable);
        }
    }
    //=========重啟時開啟======================
    @Override
    protected void onResume() {
        super.onResume();
        initdb();
//        if (account != null) {
//            DB_Name = "CAYF.db";
//            if (U_dbHper == null) {
//                U_dbHper = new Calendar_editDbHelper(getApplicationContext(), DB_Name, null, DBversion);
//                U_dbHper.creatfile();
//            }
////            if (U_home_dpHper == null) {
////                U_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
////                U_home_dpHper.createHomeTable();
////            }
//            dbHper = U_dbHper;
////            home_dpHper = U_home_dpHper;
//        }
//        if (account == null) {
//            DB_Name = "guest.db";
//            if (G_dbHper == null) {
//                G_dbHper = new Calendar_editDbHelper(getApplicationContext(), DB_Name, null, DBversion);
//                G_dbHper.creatfile();
//            }
////            if (G_home_dpHper == null) {
////                G_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
////                G_home_dpHper.createHomeTable();
////            }
//            dbHper = G_dbHper;
//        }
        mem = dbHper.FindMEMBER();
    }


    @Override
    public void onClick(View v) {
        java.util.Calendar now = java.util.Calendar.getInstance();
        switch(v.getId()){
            case R.id.calendar_edit_b004:// 儲存按鈕
                tname=e001.getText().toString().trim();
                tcontent=e002.getText().toString().trim();
                tdate=t014.getText().toString().trim();
                ttime=t015.getText().toString().trim();

                if (tname.equals("")||tcontent.equals("")||tdate.equals("日期")||ttime.equals("時間")){
                    Toast.makeText(getApplicationContext(),"請不要空白，無法新增!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg =null;
                //這裡會跳到副程式喔!!
                if (account!=null){
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
                    long rowID=dbHper.insertRec(tname,tcontent,tdate,ttime);
//                    long a=rowID;
//                    int b=1;
                    //========================================
                    if (rowID!=-1){
                        msg="新增記錄成功"+"\n"+"目前資料共幾筆"+dbHper.RecCount()+"筆記錄!";
                    }else{
                        msg="新增記錄失敗";
                        e001.setHint("帽子給我好嗎?");
                        e001.setText("");
                        e002.setText("");
                        e003.setText("");
                    }
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
                this.finish();
                break;
            case R.id.calendar_edit_b003:// 取消按鈕
//                intent.setClass(Calendar_edit.this, Calendar.class);
//                startActivity(intent);
                this.finish();
                break;
            case R.id.calendar_edit_b001:// 選擇日期
                ScaleAnimation animation = (ScaleAnimation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.calendar_edit_zoom_out);
                animation.setRepeatCount(5);
                add002.startAnimation(animation);
                DatePickerDialog datePicDig = new DatePickerDialog(
                        Calendar_edit.this,
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
            case R.id.calendar_edit_b002:// 選擇時間
                ScaleAnimation animation01 = (ScaleAnimation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.calendar_edit_zoom_out);
                animation01.setRepeatCount(5);
                add003.startAnimation(animation01);

                TimePickerDialog timePicDig = new TimePickerDialog(
                        Calendar_edit.this, timepicdiglistener,
                        now.get(java.util.Calendar.HOUR_OF_DAY),
                        now.get(java.util.Calendar.MINUTE),false

                );
                timePicDig.setTitle(getString(R.string.calendar_edit_timetitle));
                timePicDig.setMessage(getString(R.string.calendar_edit_timemessage));
                timePicDig.setCancelable(false);
                timePicDig.show();

                break;
            case R.id.calendar_edit_t017:
//                if (t014.getText().toString().trim().equals("日期")){
//                    Toast.makeText(getApplicationContext(),"請不要空白，無法新增!!",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                dbHper.createTB(t014.getText().toString().trim());
//                Toast.makeText(getApplicationContext(),"新增10筆假資料成功!!",Toast.LENGTH_SHORT).show();
                break;
        }


    }
    //日期監聽
    private DatePickerDialog.OnDateSetListener datapicdiglistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            set_date=year+"-"+(month+1)+"-"+dayOfMonth;
            String set_data_date =""+ year + (month + 1) + dayOfMonth;
            t014.setText(set_date);
        }
    };
    //時間監聽
    private TimePickerDialog.OnTimeSetListener timepicdiglistener= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            set_time=String.format("%02d",hourOfDay)+":"+String.format("%02d",minute);
            String set_data_time = String.format("%02d",hourOfDay)+ ":" + String.format("%02d",minute) ;
            t015.setText(set_time);
//            t016.setText(set_data_time);

        }
    };

    private void mysql_insert() {
//        String sqlctl = " SELECT *  FROM cal100 ORDER BY id ASC ";
        ArrayList<String> nameValuePairs =new ArrayList<String>();

//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(tname);
        nameValuePairs.add(tcontent);
        nameValuePairs.add(tdate);
        nameValuePairs.add(ttime);
        nameValuePairs.add(mem[0]);
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //======真正執行mysql==================

        String result = Calendar_DBConnector.executeInsert(nameValuePairs);
        //-----------------------------------------------

    }

    @Override
    public void onBackPressed() {

    }


    Runnable   udpUIRunnable=new  Runnable(){
        @Override
        public void run() {
            mysql_insert();
        }
    };

}
// mBtnTimePicDlg = (Button) findViewById(R.id.m0901_b001);
//        mBtnDatePicDlg = (Button) findViewById(R.id.m0901_b002);
//        mTxtResult = (TextView) findViewById(R.id.m0901_t001);
//
//        mBtnTimePicDlg.setOnClickListener(btnOnClkLis);
//        mBtnDatePicDlg.setOnClickListener(btnOnClkLis);
//    }
//    private Button.OnClickListener btnOnClkLis = new Button.OnClickListener(){
//        @Override
//        public void onClick(View v) {
//            mTxtResult.setText("");  //
//            Calendar now = Calendar.getInstance();
//            //-------------------------------------------
//            switch (v.getId()){
//                case R.id.m0901_b001:
//                    DatePickerDialog datePicDlg = new DatePickerDialog(M0901.this,
//                            datePicDlgOnDateSelLis,
//
//                            now.get(Calendar.YEAR),
//                            now.get(Calendar.MONTH),
//                            now.get(Calendar.DAY_OF_MONTH));
//
//                    datePicDlg.setTitle(getString(R.string.m0901_datetitle));
//                    datePicDlg.setMessage(getString(R.string.m0901_datemessage));
//                    datePicDlg.setIcon(android.R.drawable.ic_dialog_info);
//                    datePicDlg.setCancelable(false);
//                    datePicDlg.show();
//                    break;
//                case R.id.m0901_b002:
//                    TimePickerDialog timePicDlg = new TimePickerDialog(M0901.this,
//                            timePicDlgOnTimeSelLis,
//                            now.get(Calendar.HOUR_OF_DAY),
//                            now.get(Calendar.MINUTE),
//                            true);
//                    timePicDlg.setTitle(getString(R.string.m0901_timetitle));
//                    timePicDlg.setMessage(getString(R.string.m0901_timemessage));
//                    timePicDlg.setIcon(android.R.drawable.ic_dialog_info);
//                    timePicDlg.setCancelable(false);
//                    timePicDlg.show();
//                    break;
//            }
//
//        }
//    };
//    private DatePickerDialog.OnDateSetListener datePicDlgOnDateSelLis = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            mTxtResult.setText(getString(R.string.m0901_datetitle)+
//                    Integer.toString(year) + getString(R.string.n_yy)+
//                    Integer.toString(monthOfYear + 1) + getString(R.string.n_mm) +
//                    Integer.toString(dayOfMonth) + getString(R.string.n_dd));
//        }
//    };
//
//    private TimePickerDialog.OnTimeSetListener timePicDlgOnTimeSelLis = new TimePickerDialog.OnTimeSetListener() {
//
//        @Override
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            mTxtResult.setText(getString(R.string.m0901_timetitle) +
//                    Integer.toString(hourOfDay) +getString(R.string.d_hh) +
//                    Integer.toString(minute) +getString(R.string.d_mm));
//        }