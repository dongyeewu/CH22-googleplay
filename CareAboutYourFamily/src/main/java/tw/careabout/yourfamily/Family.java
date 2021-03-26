package tw.careabout.yourfamily;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import tw.careabout.yourfamily.CircleImgView;
import tw.careabout.yourfamily.Main;
import tw.careabout.yourfamily.MyAlertDialog;
import tw.careabout.yourfamily.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import tw.careabout.yourfamily.home.HomeDbHelper;


import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Family extends AppCompatActivity {


    private String TAG = "tcnr26=>";

    //資料表相關宣告()
    private String DB_Name = "CAYF.db";
    private int DBversion = 1;
    private HomeDbHelper home_dpHper;
    private FamilyDbHelper fam_dbHper;

    private Intent intent = new Intent();
    private TextView t002;
    private EditText e001,e002;
    private Menu menu;
    private int flag_button = 0;
    private String e001s = "";
    private ArrayList<String> mrecSet;
    private String s_mem_001="";

    private Switch switch01;
    private TextView t004;
    private String joinfamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //onclick要刪除一個
        initDB();
        mrecSet = fam_dbHper.getRecSet_m(); //重新載入SQLite
        String[] m_arr = mrecSet.get(0).split("#");

        if (!m_arr[6].equals("0")) {
            gofamily();
        }

        s_mem_001 = m_arr[0];

        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family);

        //================================
        setupViewConponent();
    }

    // -------------抓取遠端資料庫設定執行續------------------------------
    public static void enableStrictMode(Context context) {
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()
                        .penaltyLog()
                        .build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .build());
    }

    private void setupViewConponent() {
        this.setTitle(getString(R.string.f003_title01));
        switch01=(Switch)findViewById(R.id.f003_switch01);
        t002 = (TextView) findViewById(R.id.f003_t002);
        t002.setOnClickListener(b001ON);
        t004= (TextView) findViewById(R.id.f003_t004);
        t004.setOnClickListener(b001ON);
        e001 = (EditText) findViewById(R.id.f003_e001);
        e002 = (EditText) findViewById(R.id.f003_e002);

        switch01.setOnCheckedChangeListener(switchON);
    }

    private CompoundButton.OnCheckedChangeListener switchON=new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Family.this.setTitle(R.string.f003_title02);
                switch01.setText(getString(R.string.f003_switch02));
                findViewById(R.id.f003_l002).setVisibility(View.GONE);
                findViewById(R.id.f003_l003).setVisibility(View.VISIBLE);
                t002.setVisibility(View.GONE);
                t004.setVisibility(View.VISIBLE);
            }else{
                Family.this.setTitle(R.string.f003_title01);
                switch01.setText(getString(R.string.f003_switch01));
                findViewById(R.id.f003_l002).setVisibility(View.VISIBLE);
                findViewById(R.id.f003_l003).setVisibility(View.GONE);
                t002.setVisibility(View.VISIBLE);
                t004.setVisibility(View.GONE);
            }
    }
};

    private View.OnClickListener b001ON = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.f003_t002:
                    if (flag_button == 0) {
                        flag_button = 1;
                        final ProgressDialog progDlg = new ProgressDialog(Family.this);
                            progDlg.setTitle(getString(R.string.info_wait));
                            progDlg.setMessage(getString(R.string.info_load));
                            progDlg.setIcon(android.R.drawable.presence_away);
                            progDlg.setCancelable(false);
                            progDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progDlg.show();
                            new Thread(new Runnable() {
                                    public void run() {
                                   try {
                                       if (e001.getText().toString().equals(""))
                                           e001.setText(R.string.f000_t000);
                                       e001s = e001.getText().toString();
                                       f_m_c();
                                    }catch (Exception e){}
                                    handler.sendEmptyMessage(1);
                                    progDlg.cancel();
                                }
                            }).start();
                            gofamily();
                        }

//                        m_m_u();
                    break;
                case R.id.f003_t004:
                    if (flag_button == 0) {
                        flag_button = 1;
                        final ProgressDialog progDlg = new ProgressDialog(Family.this);
                        progDlg.setTitle(getString(R.string.info_wait));
                        progDlg.setMessage(getString(R.string.info_load));
                        progDlg.setIcon(android.R.drawable.presence_away);
                        progDlg.setCancelable(false);
                        progDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progDlg.show();
                        joinfamily = e002.getText().toString().trim();
//                        if(joinfamily.length()==9){
                        int a = joinfamily.length();
                        new Thread(new Runnable() {
                            public void run() {
                                try {

                                    if (m_m_u(joinfamily) == 1) {
                                        intent.setClass(Family.this, Family_Home.class);
                                        intent.putExtra("flag", "FAQ");
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        flag_button = 0;
                                    }
                                }catch (Exception e){
                                    flag_button = 0;
                                }
                                if(flag_button==0) {
                                    handler.sendEmptyMessage(0);
                                }
                                progDlg.cancel();
                            }
                        }).start();
                    }
//                    }
                    break;
//                case R.id.f003_t001:
//                    m_m_u("1");
//                    gofamily();
//                    break;
            }
        }
    };

    private final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(),getString(R.string.f003_alert01) , Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void gofamily() {
        //                intent.putExtra("familyname", e001n);
        intent.setClass(Family.this, Family_Home.class);
        startActivity(intent);
        finish();
    }

    private void initDB() {
        if (home_dpHper == null) {
            home_dpHper = new HomeDbHelper(this, DB_Name, null, DBversion);
            home_dpHper.createHomeTable();
        }
        if (fam_dbHper == null) {
            fam_dbHper = new FamilyDbHelper(this, DB_Name, null, DBversion);
            fam_dbHper.createTable();
        }
    }

    private void f_m_c() {
        // sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        // nameValuePairs.add(sqlctl);
        nameValuePairs.add(e001s);
        nameValuePairs.add(s_mem_001);
        try {
            Thread.sleep(100); // 延遲Thread 睡眠0.1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Family_DBConnector.fam_m_c(nameValuePairs);
        Log.d(TAG, result);
//-----------------------------------------------
    }

    private int m_m_u(String f05) {
        // sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        // nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_mem_001);
        nameValuePairs.add(f05);
        try {
            Thread.sleep(100); // 延遲Thread 睡眠0.1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Family_DBConnector.mem_m_u(nameValuePairs);
        Log.d(TAG, result);
        if(result.equals("hi ok2")) {
            return 1;
        }else{
            return 0;
        }
//-----------------------------------------------
    }

    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    //----------------------------------------menu---------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gohome, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent menu_it =new Intent();
        switch (item.getItemId()) {
            case R.id.gohome_btn:
                menu_it.setClass(getApplicationContext(), Main.class);
                startActivity(menu_it);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}