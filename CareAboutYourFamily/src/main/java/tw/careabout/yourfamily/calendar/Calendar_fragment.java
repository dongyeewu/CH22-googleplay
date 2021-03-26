package tw.careabout.yourfamily.calendar;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import tw.careabout.yourfamily.R;
import tw.careabout.yourfamily.home.HomeDbHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;





public class Calendar_fragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private String DB_Name = "";
//    private String DB_Name = "CAYF.db";
//    private static final String DB_FILE = "u693479232_t24_test";
    private static final String DB_TABLE = "cal100";
    private static final int DBversion = 1;

    private Calendar_editDbHelper dbHper,U_dbHper,G_dbHper;
    private HomeDbHelper home_dpHper,U_home_dpHper,G_home_dpHper;


    private BottomNavigationView bnv;
    private TextView add1,add2,t004;
    private Intent intent=new Intent();
    private String[] list001;
    private ArrayList<Map<String, Object>> mList;
    private ArrayList<Map<String, Object>> mfList;
//    private String[] list002;
//    private CalendarView calendar001;
    private ListView tList;
    //    private String timearray[][]={
//            {"00:00","看病"},
//            {"02:00","運動"},
//            {"15:00","午睡"}
//    };
    private java.util.Calendar now;
    private DatePicker maincalendar;
    private String s,sss;
    private String ss;
    private TextView test004;
    private String[] list002,list003,list004,list005,list006;
    //    private String[] list004;
    private Menu menu;
    //    private String[] list005;
//    private String[] list006;
//    private TextView t004;
    private ListView listview;

    private float density,w,h;
    private RelativeLayout r1;
    private LinearLayout r2;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float range=150;
    private int ran=60;//
    private TextView image001;
    private int YYY;
    private int MMM;
    private int DDD;
    private String ser_msg="";
    private int servermsgcolor=0;
    private TextView t005;
    private String[] mem=new String[14];//
    private GoogleSignInAccount account;
    private Handler handler=new Handler();


    public Calendar_fragment() {
        // Required empty public constructor
    }
    public static Calendar_fragment newInstance(String param1, String param2) {
        Calendar_fragment fragment = new Calendar_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);
        enableStrictMode(getActivity());
        initDB();
        setupViewComponent(view);
        return view;
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

    private void setupViewComponent(View view) {
        add1=(TextView)view.findViewById(R.id.calendar_t003);
        add1.setOnClickListener(this);
        add1.setZ(20);
        t004=(TextView)view.findViewById(R.id.calendar_t004);
        t004.setZ(5);
        t005=(TextView)view.findViewById(R.id.calendar_t005);
        image001=(TextView)view.findViewById(R.id.calendar_imge001);
        image001.setOnClickListener(this);
        image001.setZ(30);
        image001.setVisibility(View.GONE);
        r1=(RelativeLayout)view.findViewById(R.id.calendar_rela001);
        r2=(LinearLayout)view.findViewById(R.id.calendar_lilo001);
        //=====================================================
        //匯入sql
        dbHper.RecCount();
        list001=(getResources().getStringArray(R.array.time));
        list002=(getResources().getStringArray(R.array.content));
        list003=(getResources().getStringArray(R.array.time01));
        list004=(getResources().getStringArray(R.array.content01));
        list005=(getResources().getStringArray(R.array.time02));
        list006=(getResources().getStringArray(R.array.content02));
        //=====================================================
        maincalendar=(DatePicker)view.findViewById(R.id.calendar_main);
        listview = (ListView)view.findViewById(R.id.calendar_list);
        now= java.util.Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        YYY=now.get(java.util.Calendar.YEAR);
        MMM=now.get(java.util.Calendar.MONTH);
        DDD=now.get(java.util.Calendar.DAY_OF_MONTH);
        //====================================================
//        showcalendar();

//        calendarpicker();
        //=====================================================================================
        //-------設按鍵
//        DisplayMetrics dm = new DisplayMetrics(); //找出使用者手機的寬高
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        density = dm.density;
//        w=dm.widthPixels;
//        h=dm.heightPixels;
        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 60 / 100; // 設定ScrollView使用尺寸的4/5
        int newscrollwidth = displayMetrics.widthPixels * 90 / 100;
        int newscrollRheight=displayMetrics.heightPixels * 80 / 100;
        int newscrollRwidth=displayMetrics.widthPixels * 60 / 100;
        int newscrollRLheight=displayMetrics.heightPixels * 50 / 100;
        int newscrollRLwidth=displayMetrics.widthPixels * 100 / 100;
    }
    //===========calendar的畫面呈現==========================================================

    private void showcalendar() {
//        executeQuery
        mfList=new ArrayList<Map<String,Object>>();
        sss=""+YYY+"-"+(MMM+1)+"-"+DDD;
//        Toast.makeText(getApplicationContext(),sss,Toast.LENGTH_LONG).show();
        //====================================================================================
        String result2 = "";//抓值 先設為0
        String[][] rec2=dbHper.FindDATE(sss);//設一個二微陣列去接收FindDATE的值
        int c=rec2.length;
        String e=""+c;
        int d=0;
//        Toast.makeText(getApplicationContext(),e,Toast.LENGTH_LONG).show();
        if (rec2.length != 0)
        {
            result2 = "找到的資料為 ：\n" + rec2;
            for (int i=0;i<rec2.length;i++)
            {
                Map<String,Object>item=new HashMap<String, Object>();
                item.put("time",rec2[i][4]);
                item.put("content",rec2[i][1]);
                mfList.add(item);
                t004.setZ(-1);
            }
        }
        else
        {
            result2= "找不到指定的編號";
            t004.setZ(5);
        }
        SimpleAdapter adapter1 = new SimpleAdapter(
                getActivity(),mfList,R.layout.calendar_secondarydemon,
                new String[]{"time","content"},new int[]{R.id.calendar_secondarydemon_t001,R.id.calendar_secondarydemon_t002}
        );
        listview.setAdapter(adapter1);
        listview.setOnItemClickListener(Calendar_adpter1);
        listview.setTextFilterEnabled(true);
    }

    private void calendarpicker() {
        sss=""+now.get(java.util.Calendar.YEAR)+"-"+(now.get(java.util.Calendar.MONTH)+1)+"-"+now.get(java.util.Calendar.DATE);
        ss=sss;
        maincalendar.init(YYY, MMM, DDD, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ss=""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                s=""+year+(monthOfYear+1)+dayOfMonth;
                YYY=year;
                MMM=monthOfYear;
                DDD=dayOfMonth;
                showcalendar();
            }
        });
    }

    AdapterView.OnItemClickListener Calendar_adpter1=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[][] rec=dbHper.FindDATE(sss);
        String[] oplist=new String[rec[0].length];
        oplist=rec[position];
        int a=0;
        Bundle bundle=new Bundle();
        bundle.putStringArray("key_oplist",oplist);
        intent.putExtras(bundle);
        int b=0;
        intent.setClass(getActivity(),Calendar_secondaryshow.class);
        startActivity(intent);
        }
    };
    //================sql===================================================
    private void initDB() {
        if(account!=null){
            DB_Name="CAYF.db";
            if (U_dbHper == null) {
                U_dbHper = new Calendar_editDbHelper(getActivity(), DB_Name, null, DBversion);
                U_dbHper.creatfile();
            }
            if (U_home_dpHper == null) {
                U_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                U_home_dpHper.createHomeTable();
            }
            dbHper = U_dbHper;
            home_dpHper = U_home_dpHper;
        }
        if(account==null){
            DB_Name = "guest.db";
            if (G_dbHper == null) {
                G_dbHper = new Calendar_editDbHelper(getActivity(), DB_Name, null, DBversion);
                G_dbHper.creatfile();
            }
            if (G_home_dpHper == null) {
                G_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                G_home_dpHper.createHomeTable();
            }
            dbHper = G_dbHper;
            home_dpHper = G_home_dpHper;
        }

    }
    //====================匯入mysql==========================================
    private void dbmysql() {
        String sqlctl = "SELECT * FROM cal100 WHERE cal006 = "+mem[0]+" ORDER BY cal001 DESC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Calendar_DBConnector.executeQuery(nameValuePairs);
            chk_httpstate();
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            int rowsAffected1 = dbHper.clearRec();
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
//--------------------------------------------------------
                int rowsAffected = dbHper.clearRec();                 // 匯入前,刪除所有SQLite資料
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

    private void chk_httpstate() {
        ////-------------------------------
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)

        if (Calendar_DBConnector.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + Calendar_DBConnector.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Navy);
//                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",Toast.LENGTH_SHORT).show();
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
                    servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + Calendar_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + Calendar_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Red);
                    break;
            }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }
        if (Calendar_DBConnector.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + Calendar_DBConnector.httpstate + ") ";
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calendar_t003://新增按鈕
//                if(account==null){
//                    Toast.makeText(getActivity(),"登入後才能新增",Toast.LENGTH_SHORT).show();
//                }
//                else {
                    Bundle bundle1=new Bundle();
                    String datechoose=sss;
                    bundle1.putString("key_date1",datechoose);
                    intent.putExtras(bundle1);
                    intent.setClass(getActivity(), Calendar_edit.class);
                    startActivity(intent);
//                }
                break;
            case R.id.calendar_imge001:
                maincalendar.setVisibility(View.VISIBLE);
                image001.setVisibility(View.GONE);
                break;
        }
    }

    Runnable   udpUIRunnable=new  Runnable(){
        @Override
        public void run() {
            dbmysql();
            dbHper.RecCount();
            calendarpicker();
            showcalendar();

        }
    };


    //================生命週期======================================================

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        initDB();
        if (account!=null){
            if (dbHper == null) {
                dbHper = new Calendar_editDbHelper(getActivity(), DB_Name, null, DBversion);
            }
            mem = dbHper.FindMEMBER();
            final ProgressDialog progDlg = new ProgressDialog(getActivity());
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
            dbHper.RecCount();
            calendarpicker();
            showcalendar();
        }

//        dbmysql();
//        dbHper.RecCount();
//        calendarpicker();
//        showcalendar();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dbmysql();
//                dbHper.RecCount();
//                calendarpicker();
//                showcalendar();
//            }
//        }, 50);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 開啟中的話就關掉
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        if (handler!=null){
            handler.removeCallbacks(udpUIRunnable);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 開啟中的話就關掉
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        if (handler!=null){
            handler.removeCallbacks(udpUIRunnable);
        }

    }
}