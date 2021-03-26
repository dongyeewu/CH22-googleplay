package tw.careabout.yourfamily;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;




import tw.careabout.yourfamily.board.BoardDBHper;
import tw.careabout.yourfamily.board.BoardDBconnector;
import tw.careabout.yourfamily.home.HomeDbHelper;
import tw.careabout.yourfamily.record.Record_DBConnector;
import tw.careabout.yourfamily.record.Record_DbHelper;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Family_Home extends AppCompatActivity implements View.OnClickListener {

    //member資料表相關宣告()
    private String DB_Name = "CAYF.db";
    private int DBversion = 1;
    private HomeDbHelper home_dpHper;
    private int groupID = 0;
    private String guest = "guest";
    private String lastLoginTime = "";
    private String TAG = "tcnr26=>";

    private BottomNavigationView bnv;

    private FamilyDbHelper family_dbHper;
    private static final String DB_FILE = "CAYF.db";
    private static final String DB_TABLE = "family";
    private Elder_editDbHelper elder_dbHper;
    private BoardDBHper boardDBHper;
    private Intent intent =new Intent();
    private Handler handler = new Handler();
    private TextView t006;
    private TextView b000;
    private TextView t000;
    private TextView t099;
    private Dialog dfe;
    private String f3title;
    private EditText fee001;
    private EditText fee002;
    private TextView t005;
    private Dialog dfa;
    private String[] list;
    private Integer[] imgArr;
    private ArrayList<Map<String, Object>> mList;
    private String fct;
    private String fnt;
    private TextView fet005;
    private String fet;
    private TextView t008;
    private TextView t010;
    private TextView b001;
    private Dialog dfac;
    private TextView face001;
    private Menu menu;
    private int faccnt = 0;
    private TextView t020;

    //-------------
    private String sqlctl;
    private Object ser_msg = "";
    private ArrayList<String> recSet=new ArrayList<String>();
    private ArrayList<String> recSet_m=new ArrayList<String>();;
    private ArrayList<String> recSet_fm= new ArrayList<String>();
    private String s_mem_007 = "";
    private String s_mem_001 = "";
    private String[] m_arr;
    private String[] f_arr;
    private String[] fm_arr;
    private String s_fam_002;
    private ArrayList<String> fm_gmail= new ArrayList<String>();
    private ArrayList<String> fm_imgurl= new ArrayList<String>();
    private ArrayList<String> fm_nickname= new ArrayList<String>();
    private ArrayList<String> fm_time= new ArrayList<String>();
    private ArrayList<String> recSet_e= new ArrayList<String>();;
    private CircleImgView i101;
    private TextView fname;
    private String s_fam_005;
    private TextView t021;
    private LinearLayout l020;
    private String[] e_arr;
    private String s_eld_002;
    private Record_DbHelper record_dbHelper;
    private RequestOptions options;
    private GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_home);
        initDB();
        dbmysql_m();
        dbmysql();
        dbmysql_fm();
        refresh_all();
        setupViewConponent();
    }

    private void refresh_all() {
        Intent intent_get = this.getIntent();
        //取得傳遞過來的資料
        String flag = intent_get.getStringExtra("flag");
        if(flag!=null){
//            Toast.makeText(getApplicationContext(), "要抓新的喔", Toast.LENGTH_LONG).show();
            //等資料喔喔喔喔喔喔喔喔-------------------------------------------------------------------------------------
                    createSQLite();

        }
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
        f_arr = recSet.get(0).split("#");
        s_fam_002 = f_arr[1];
        s_fam_005 = f_arr[4];
        this.setTitle(getString(R.string.f000_title)+"--"+s_fam_002);
        gggggg();
        //--------------------------family-------------------------------------------
//        Dialog dfc = new Dialog(Family_Home.this);
//        dfc.setContentView(R.layout.family);
//        TextView fct000 = (TextView)dfc. findViewById(R.id.f003_t002);
//        TextView fct003 = (TextView) dfc.findViewById(R.id.f003_t003);
//        fct000.setOnClickListener(this);
//        EditText fce001 = (EditText) dfc.findViewById(R.id.f003_e001);
//        dfc.show();
        //--------------------------------------------------------------------
        t006 = (TextView) findViewById(R.id.f000_t006);
        t006.setOnClickListener(this);
        b000 = (TextView) findViewById(R.id.f000_b000);
        b000.setOnClickListener(this);
        b001 = (TextView) findViewById(R.id.f000_b001);
        b001.setOnClickListener(this);

//        t000 = (TextView) findViewById(R.id.f000_t000);
//        t000.setText(s_fam_002);
//        t005 = (TextView) findViewById(R.id.f000_t005);
//        t008 = (TextView) findViewById(R.id.f000_t008);
        t010 = (TextView) findViewById(R.id.f000_t010);
        t020 = (TextView) findViewById(R.id.f000_t020);
        t020.setOnClickListener(this);
        t021 = (TextView) findViewById(R.id.f000_t021);
        t021.setOnClickListener(this);

        l020=(LinearLayout)findViewById(R.id.f000_l020);
        //l020.setOnClickListener(this);
        i101=(CircleImgView) findViewById(R.id.f000_i101);

        t010.setText(s_fam_005);

        recSet_e=family_dbHper.getRecSet_e();
        for(int i=0;i<recSet_e.size();i++){
            e_arr=recSet_e.get(i).split("#");
            s_eld_002=e_arr[1];
            TextView tv = new TextView(this);
            tv.setText(s_eld_002);
            tv.setTextSize(18);
            tv.setPadding(0, 15, 0, 15);
            tv.setGravity(Gravity.CENTER);
            l020.addView(tv);
        }

        ListView listView = (ListView) findViewById(R.id.f000_lv001);
        listView.setLayoutParams(listView.getLayoutParams()); // 重定ScrollView大小
        imgArr = new Integer[]{
                R.drawable.sensei1, R.drawable.sensei2,
                R.drawable.sensei3, R.drawable.sensei4,
                R.drawable.sensei5, R.drawable.sensei6};
        mList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < fm_imgurl.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("f000_i101", getBitmapFromURL(fm_imgurl.get(i)));
//            item.put("f000_i101", fm_imgurl.get(i));
            item.put("f000_t101", fm_nickname.get(i));
            item.put("textView2", fm_time.get(i));
//            Glide.with(getApplicationContext()).load(fm_imgurl.get(i)).into(i101);
            mList.add(item);
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    mList,//data
                    R.layout.family_list1,//resource
                    new String[]{"f000_i101", "f000_t101","textView2"},//from
                    new int[]{R.id.f000_i101, R.id.f000_t101, R.id.f000_t102});//to


            listView.setAdapter(adapter);

            listView.setTextFilterEnabled(true);
            listView.setOnItemClickListener(l101ON);
        }
    }

    private void gggggg(){
        for(int i=0;i<recSet_fm.size();i++) {
            fm_arr = recSet_fm.get(i).split("#");
            fm_gmail.add(fm_arr[3]);
            fm_nickname.add(fm_arr[4]);
            fm_imgurl.add(fm_arr[5]);
            fm_time.add(fm_arr[7]);
            int a = 1;
        }
        int a = 0;
//        Glide.with(mContext)
//                .load("")
////                .skipMemoryCache(true)
////                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .override(100, 75)
//                .transition(withCrossFade())
//                .error(
//                        Glide.with(mContext)
//                                .load("https://aboutyfc.com/android_mysql_connect/nopic1.jpg"))
//                .into(holder.img);
//
//        //將position保存在itemView的Tag中，以便點擊時進行獲取
//        holder.itemView.setTag(position);
////        Log.d(TAG,account.getEmail());
    }

    final AdapterView.OnItemClickListener l101ON = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Dialog dfm = new Dialog(Family_Home.this);
            dfm.setContentView(R.layout.family_member);
            LinearLayout dgml = (LinearLayout) dfm.findViewById(R.id.f005_l001);
            ImageView iv = new ImageView(Family_Home.this);

            options = new RequestOptions()
                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(100)))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .priority(Priority.NORMAL);

            Glide.with(getApplicationContext())
                    .load(fm_imgurl.get(position))
                    .apply(options)
                    .override(200, 200)
                    .error(
                            Glide.with(getApplicationContext())
                                    .load("https://bklifetw.com/img/nopic1.jpg"))
                    .into(iv);


            //Glide.with(getApplicationContext()).load(fm_imgurl.get(position)).into(iv);
            dgml.addView(iv);
            TextView fm011 = (TextView) dfm.findViewById(R.id.f005_t011);
            TextView fm012 = (TextView) dfm.findViewById(R.id.f005_t012);
            fm011.setText(fm_nickname.get(position));
            fm012.setText(fm_gmail.get(position));
//            fat002.setOnClickListener(Family_Home.this);
            dfm.show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.f000_t006:
                dfe = new Dialog(Family_Home.this);
//            mld.setTitle(getText(R.string.login));
//            dfe.setCancelable(false);
                dfe.setContentView(R.layout.family_edit);
                fee001 = (EditText) dfe.findViewById(R.id.f001_e001);
                TextView feb001 = (TextView) dfe.findViewById(R.id.f001_t006);


                feb001.setOnClickListener(this);
                dfe.show();
                break;

            case R.id.f000_b000:
                dfa = new Dialog(Family_Home.this);
                dfa.setContentView(R.layout.family_add);
                TextView fat002 = (TextView) dfa.findViewById(R.id.f002_t002);
                fat002.setOnClickListener(this);
                dfa.show();
                break;

            case R.id.f001_t006:
                t000.setText(fee001.getText().toString());
                t005.setText(fee002.getText().toString());
                t008.setText(fet);
//                Family_DBConnector.familyCreate(fee001.getText().toString(), fee002.getText().toString(), fnt);
                dfe.cancel();
                break;
            case R.id.f002_t002:
                dfa.cancel();
                break;
            case R.id.f000_t020:

                MyAlertDialog myAltDlg = new MyAlertDialog(Family_Home.this);
                myAltDlg.setTitle(getString(R.string.AskExitDialog_title));
                myAltDlg.setMessage(getString(R.string.AskExitDialog_MSG));
                myAltDlg.setIcon(android.R.drawable.ic_popup_reminder);
                myAltDlg.setCancelable(false);
                myAltDlg.setButton(BUTTON_POSITIVE,"確定" , AskExit);
                myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", AskExit);
                myAltDlg.show();

                break;
            case R.id.f000_t021:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("line://msg/text/"+s_fam_005));
                startActivity(intent);
//                finish();
                break;

        }
    }

    //--退出詢問視窗監聽
    private DialogInterface.OnClickListener AskExit = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case BUTTON_POSITIVE:
                                boardDBHper.clearBoardRec();
                                elder_dbHper.clearelderRec();
                                family_dbHper.clearRec();
                                family_dbHper.clearRec_fm();
                                record_dbHelper.recClear_re();
                                record_dbHelper.eldClear_re();
                                record_dbHelper.clearRESHjoin_rs();
                                m_m_u("0");
                                dbmysql_m();
                                intent.setClass(Family_Home.this, Main.class);
                                startActivity(intent);
                                finish();
                    break;
                case BUTTON_NEGATIVE:
                    dialog.cancel();
            }
        }
    };

    private void dbmysql_m() {
        recSet_m = family_dbHper.getRecSet_m(); //重新載入SQLite
        m_arr = recSet_m.get(0).split("#");
        s_mem_007 = m_arr[6];
        s_mem_001 = m_arr[0];

//        sqlctl = "SELECT * FROM family ORDER BY fam001 ASC";
        sqlctl = "SELECT * FROM member WHERE mem001='" + s_mem_001 + "'";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Family_DBConnector.mem_m_r(nameValuePairs);
////==========================================
//            chk_httpstate(); //檢查 連結狀態
////==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
//--------------------------------------------------------
                int rowsAffected = family_dbHper.clearRec_m(); // 匯入前,刪除所有SQLite資料
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
                    ContentValues hh = newRow;
                    // -------------------加入SQLite---------------------------------------
                    long rowID = family_dbHper.mem_s_m(newRow);
                    int aa = 0;
                }
                // ---------------------------
            } else {
                ser_msg = "主機資料庫無資料(code:" + Family_DBConnector.httpstate + ") ";
                return;
            }
            recSet_m = family_dbHper.getRecSet_m(); //重新載入SQLite
            m_arr = recSet_m.get(0).split("#");
            s_mem_007 = m_arr[6];
            s_mem_001 = m_arr[0];
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void dbmysql_fm() {
//        sqlctl = "SELECT * FROM family ORDER BY fam001 ASC";
        sqlctl = "SELECT * FROM member WHERE mem007='" + s_mem_007 + "'";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Family_DBConnector.mem_m_r(nameValuePairs);
////==========================================
//            chk_httpstate(); //檢查 連結狀態
////==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
//--------------------------------------------------------
                int rowsAffected = family_dbHper.clearRec_fm(); // 匯入前,刪除所有SQLite資料
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
                    ContentValues hh = newRow;
                    // -------------------加入SQLite---------------------------------------
                    long rowID = family_dbHper.fme_s_m(newRow);
                    int aa = 0;
                }
                // ---------------------------
            } else {
                ser_msg = "主機資料庫無資料(code:" + Family_DBConnector.httpstate + ") ";
                return;
            }
            recSet_fm = family_dbHper.getRecSet_fm(); //重新載入SQLite
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    // 讀取MySQL 資料
    private void dbmysql() {
//        sqlctl = "SELECT * FROM family ORDER BY fam001 ASC";
        sqlctl = "SELECT * FROM family WHERE fam001='" + s_mem_007 + "'";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Family_DBConnector.fam_m_r(nameValuePairs);
////==========================================
//            chk_httpstate(); //檢查 連結狀態
////==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
//--------------------------------------------------------
                int rowsAffected = family_dbHper.clearRec(); // 匯入前,刪除所有SQLite資料
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
                    ContentValues hh = newRow;
                    // -------------------加入SQLite---------------------------------------
                    long rowID = family_dbHper.fam_s_m(newRow);

                }
                // ---------------------------
            } else {
                ser_msg = "主機資料庫無資料(code:" + Family_DBConnector.httpstate + ") ";
                return;
            }
            recSet = family_dbHper.getRecSet(); //重新載入SQLite
            int aa = 0;
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void m_m_u(String m07) {
        // sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
// nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_mem_001);
        nameValuePairs.add(m07);
        try {
            Thread.sleep(100); // 延遲Thread 睡眠0.1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Family_DBConnector.mem_m_l(nameValuePairs);
        Log.d(TAG, result);
//-----------------------------------------------
    }

    private void initDB() {
        if (home_dpHper == null) {
            home_dpHper = new HomeDbHelper(this, DB_Name, null, DBversion);
            home_dpHper.createHomeTable();
        }
        if (elder_dbHper == null) {
            elder_dbHper = new Elder_editDbHelper(this, DB_Name, null, DBversion);
            elder_dbHper.creatfile();
        }
        if (boardDBHper == null) {
            boardDBHper = new BoardDBHper(this, DB_Name, null, DBversion);
            boardDBHper.createBoardTable();
        }
        if (family_dbHper == null) {
            family_dbHper = new FamilyDbHelper(this, DB_Name, null, DBversion);
            family_dbHper.createTable();
            family_dbHper.createTable_fm();
        }
        if (record_dbHelper == null) {
            record_dbHelper = new Record_DbHelper(this);
            record_dbHelper.createTable();
        }
    }

    //----------------------------------生命週期--------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(this);
        Log.d(TAG, "onResume");
    }

    private void createSQLite() {

        try{
            //--board SQLite資料表 建立
            String familytID = String.valueOf(s_mem_007);
            ArrayList<JSONObject> board_list = BoardDBconnector.getBoardData(familytID);
            for(int i=0; i< board_list.size(); i++){
                boardDBHper.insertBoardRec(
                        board_list.get(i).getInt("boa001"),
                        board_list.get(i).getString("boa002"),//創建者
                        board_list.get(i).getString("boa003"),//創建時間
                        board_list.get(i).getString("boa004"),//標題
                        board_list.get(i).getString("boa005"),
                        board_list.get(i).getString("boa006"),
                        board_list.get(i).getInt("boa007"),
                        board_list.get(i).getString("boa008"),
                        board_list.get(i).getString("boa009"),
                        board_list.get(i).getString("boa010"),
                        board_list.get(i).getString("boa011"),
                        board_list.get(i).getString("boa012"),
                        board_list.get(i).getString("boa013")
                );
                int a=0;
            }

            //--elder SQLite資料表 建立
            String sqlctl = "SELECT * FROM elder WHERE eld007 = "+familytID+" ORDER BY eld001 DESC";
            ArrayList<String> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(sqlctl);
            String result = Elder_DBConnector.executeQuery(nameValuePairs);
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); //取出欄位的值
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
                    long rowID = elder_dbHper.insertRec_m(newRow);
                    //Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                    int a=0;
                }
            }

            //--record SQLite資料表 建立
            String  DB_TABLE_REC = "recordedit",
                    DB_COLUMN_REC_recID = "rec001",
                    DB_COLUMN_RECs = "rec001, rec002, rec003, rec004, rec005, rec006, " +
                            "rec007, rec008, rec009, rec010, rec011, rec012, " +
                            "rec013, rec014, rec015, rec016, rec017, rec018";
            String sql_findRecord = " SELECT " + DB_COLUMN_RECs + " FROM " + DB_TABLE_REC +
                    " WHERE rec018 = " + familytID +
                    " ORDER BY " + DB_COLUMN_REC_recID + " DESC";
            ArrayList<String> nameValuePairs_record = new ArrayList<>();
            nameValuePairs_record.add(sql_findRecord);
            String result_record = Record_DBConnector.executeQuery_re(nameValuePairs_record);

            JSONArray jsonArray_record = new JSONArray(result_record);
            if (jsonArray_record.length() > 0) { // MySQL 連結成功有資料

                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray_record.length(); i++) {
                    JSONObject jsonData = jsonArray_record.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    Iterator<String> itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value.trim().equals("")) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value); // 動態找出有幾個欄位
                        // -------------------------------------------------------------------
                    }
                    record_dbHelper.recInsert_m_re(newRow);
                }
            }

            //resh_join_rs資料表建立( 用於 RecordShow )
            ArrayList<String> email_rs = new ArrayList<>();
            email_rs.add(account.getEmail());
            String result_resh_join_rs = Record_DBConnector.executejoin_rs(email_rs);
            JSONArray jsonArray_resh_join_rs = new JSONArray(result_resh_join_rs);
            int c = jsonArray_resh_join_rs.length();

            if (jsonArray_resh_join_rs.length() > 0) {

                for (int i = 0; i < jsonArray_resh_join_rs.length(); i++) {
                    JSONObject jsonData = jsonArray_resh_join_rs.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    Iterator<String> itt = jsonData.keys();
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
                    long rowID = record_dbHelper.insertRec_j_rs(newRow);
                }

            }




        }catch (Exception e){

        }

    }

    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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