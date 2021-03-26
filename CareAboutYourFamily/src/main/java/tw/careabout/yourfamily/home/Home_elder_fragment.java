package tw.careabout.yourfamily.home;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tw.careabout.yourfamily.Elder_Add;
import tw.careabout.yourfamily.Elder_DBConnector;
import tw.careabout.yourfamily.Elder_Update;
import tw.careabout.yourfamily.Elder_editDbHelper;
import tw.careabout.yourfamily.Family;
import tw.careabout.yourfamily.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


public class Home_elder_fragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Elder_editDbHelper dbHper, G_dbHper, U_dbHper;
    private HomeDbHelper home_dpHper, G_home_dpHper, U_home_dpHper;
    private ArrayList<String> userRowData = new ArrayList<>();
    private String DB_FILE = "";

    private static final int DBversion = 1;
    private TextView createFamilyBtn;

    private SwipeRefreshLayout ls001;
    private ArrayList<Map<String, Object>> mList;
    private String ser_msg="";
    private int servermsgcolor=0;
    private RecyclerView rv001;
    private String[] mem=new String[14];
    private boolean createFamilyBtnflag=false;
    private boolean fabflag=false;
    private String mem00001;
    private HandlerThread mThread;
    private FloatingActionButton fab;
    private TextView t001, showText;
    private GoogleSignInAccount account;
    private LinearLayout noElder_layout;


    public Home_elder_fragment() {
        // Required empty public constructor
    }

    public static Home_elder_fragment newInstance(String param1, String param2) {
        Home_elder_fragment fragment = new Home_elder_fragment();
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
        // Inflate the layout for this fragment
        enableStrictMode(getActivity());
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        View root =inflater.inflate(R.layout.home_elder_fragment, container, false);
        View root1 =inflater.inflate(R.layout.elder_cell_post, container, false);

        fab=(FloatingActionButton)root.findViewById(R.id.Home_elder_fragment_t001);
        createFamilyBtn=(TextView)root.findViewById(R.id.Home_elder_fragment_t099);
        ls001=(SwipeRefreshLayout)root.findViewById(R.id.Home_elder_fragment_ls001);
        rv001=(RecyclerView)root.findViewById(R.id.Home_elder_fragment_rv001);
        t001=(TextView)root1.findViewById(R.id.elder_cell_post_t001);

        noElder_layout = (LinearLayout)root.findViewById(R.id.noElder_layout);
        showText = (TextView)root.findViewById(R.id.Home_elder_fragment_t002);

        initdb();
        userRowData = home_dpHper.getUser();
        setShowElderView();
        return root;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab.setOnClickListener(fabON);
        fab.setZ(10);
        createFamilyBtn.setOnClickListener(this);
        createFamilyBtn.setZ(10);
        rv001.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
        });
        ls001.setOnRefreshListener(onSwipeToRefresh001);
        ls001.setSize(SwipeRefreshLayout.LARGE);
        ls001.setDistanceToTriggerSync(50);
        ls001.setProgressBackgroundColorSchemeColor(getActivity().getColor(android.R.color.background_light));
        ls001.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark);
        /*setProgressViewOffset : 設置進度圓圈的偏移量。
        第一個參數表示進度圈是否縮放，
        第二個參數表示進度圈開始出現時距頂端的偏移，
        第三個參數表示進度圈拉到最大時距頂端的偏移。*/
        ls001.setProgressViewOffset(true, 0, 10);

        //=====================
        onSwipeToRefresh001.onRefresh();  //開始轉圈下載資料
    }

    private View.OnClickListener fabON =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(getActivity(), Elder_Add.class);
            startActivity(intent);
        }
    };

    private  SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh001 =new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if( Integer.valueOf(userRowData.get(0))!=0 ){
                        dbmysql();
                    }

                    handler.sendEmptyMessage(0);
                    ls001.setRefreshing(false);
                }
            },500);

        }
    };

    private final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setShowElderView();
                    break;
            }
        }
    };


    private void setShowElderView(){
        int userID = Integer.valueOf(userRowData.get(0));
        int familyID = Integer.valueOf(userRowData.get(6));
        ArrayList<String> elder_list = new ArrayList<String>();
        elder_list=dbHper.getRecSet();

        if ( userID==0 ){

            if( elder_list.size()==0 ){
                noElder_layout.setVisibility(View.VISIBLE);
                fab.setVisibility(View.VISIBLE);
                createFamilyBtn.setVisibility(View.GONE);
                setDatatolist();
            }
            if( elder_list.size()!=0 ){
                noElder_layout.setVisibility(View.INVISIBLE);
                fabflag=true;
                fab.setVisibility(View.VISIBLE);
                createFamilyBtnflag=false;
                createFamilyBtn.setVisibility(View.GONE);
                setDatatolist();
            }

        }
        if ( userID!=0 ){
            //dbmysql();
            if (familyID==0){
                noElder_layout.setVisibility(View.VISIBLE);
                showText.setText(getString(R.string.home_bd_t004));

                fabflag=false;
                fab.setVisibility(View.GONE);
                createFamilyBtnflag=true;
                createFamilyBtn.setVisibility(View.VISIBLE);
            }else {

                if( elder_list.size()==0 ){
                    noElder_layout.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    createFamilyBtn.setVisibility(View.GONE);
                }
                if( elder_list.size()!=0 ){
                    noElder_layout.setVisibility(View.INVISIBLE);
                    fabflag=true;
                    fab.setVisibility(View.VISIBLE);
                    createFamilyBtnflag=false;
                    createFamilyBtn.setVisibility(View.GONE);
                    setDatatolist();
                }

            }

        }
    }

    private void setDatatolist() {
        mList = new ArrayList<Map<String, Object>>();
        ArrayList<String> mmlist = new ArrayList<String>();
        final ArrayList<Post24> mData = new ArrayList<>();
        mmlist=dbHper.getRecSet();
        for (int i = 0; i < mmlist.size(); i++) {
            String[] fld=mmlist.get(i).split("###");
            String mem0001=fld[0];
            String mem0002=fld[1];
            String mem0003=fld[2];
            String mem0004=fld[3];
            String mem0005=fld[4];
            String mem0006=fld[5];
            String mem0007=fld[6];

            Map<String, Object> item = new HashMap<String, Object>();
            item.put("mem001", mem0001);
            item.put("mem002", mem0002);
            item.put("mem003", mem0003);
            item.put("mem004", mem0004);
            item.put("mem005", mem0005);
            item.put("mem006", mem0006);
            item.put("mem007", mem0007);
            mList.add(item);
//            mData.add(new Post24(Name, Picture1, Add, Description, Zipcode));

        }
        for (Map<String, Object> m : mList) {
            if (m != null) {
                String mem0001 = m.get("mem001").toString().trim();
                String mem0002 = m.get("mem002").toString().trim();
                String mem0003 = m.get("mem003").toString().trim();
                String mem0004 = m.get("mem004").toString().trim();
                String mem0005 = m.get("mem005").toString().trim();
                String mem0006 = m.get("mem006").toString().trim();
                String mem0007 = m.get("mem007").toString().trim();
//************************************************************
                mData.add(new Post24(mem0001,mem0002, mem0003, mem0004, mem0005, mem0006,mem0007));
//************************************************************
            } else {
                return;
            }
        }
        HOME_elder_RecyclerAdapter adapter = new HOME_elder_RecyclerAdapter(getActivity(), mData);
        rv001.setLayoutManager(new LinearLayoutManager(getActivity()));

//
//        HOME_elder_RecyclerAdapter.ViewHolder viewHolder=new HOME_elder_RecyclerAdapter.ViewHolder();
// ************************************
        adapter.setOnItemClickListener(new HOME_elder_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch(view.getId()){

                    case R.id.elder_cell_post_t009:

                        t001.setText("123");
                        Bundle bundle=new Bundle();
                        Intent intent=new Intent();
                        String[] oiclist=new String[7];
                        oiclist[0]=mData.get(position).mem0001;
                        oiclist[1]=mData.get(position).mem0002;
                        oiclist[2]=mData.get(position).mem0003;
                        oiclist[3]=mData.get(position).mem0004;
                        oiclist[4]=mData.get(position).mem0005;
                        oiclist[5]=mData.get(position).mem0006;
                        oiclist[6]=mData.get(position).mem0007;

                        bundle.putStringArray("key_oiclist",oiclist);
                        intent.putExtras(bundle);
                        int b=0;
                        intent.setClass(getActivity(), Elder_Update.class);
                        startActivity(intent);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onSwipeToRefresh001.onRefresh();
                            }
                        }, 2000);
                        break;
                    case R.id.elder_cell_post_t010:
                        mem00001="";
                        mem00001=mData.get(position).mem0001;
//                        Toast.makeText(getActivity(),"123456",Toast.LENGTH_SHORT).show();
                        Elder_MyAlertDialog aldDial = new Elder_MyAlertDialog(getActivity());
                        aldDial.setTitle("刪除");
                        aldDial.setMessage("您確定要刪除嗎?");
                        aldDial.setIcon(android.R.drawable.ic_dialog_info);
                        aldDial.setCancelable(false); //返回鍵關閉
                        aldDial.setButton(BUTTON_POSITIVE, "確定刪除", aldBtListener);
                        aldDial.setButton(BUTTON_NEGATIVE, "取消刪除", aldBtListener);
                        aldDial.show();
                        break;
                }

//                Toast.makeText(getActivity(),"123",Toast.LENGTH_SHORT).show();
//                Bundle bundle=new Bundle();
//                Intent intent=new Intent();
//                String[] oiclist=new String[7];
//                oiclist[0]=mData.get(position).mem0001;
//                oiclist[1]=mData.get(position).mem0002;
//                oiclist[2]=mData.get(position).mem0003;
//                oiclist[3]=mData.get(position).mem0004;
//                oiclist[4]=mData.get(position).mem0005;
//                oiclist[5]=mData.get(position).mem0006;
//                oiclist[6]=mData.get(position).mem0007;
//
//                bundle.putStringArray("key_oiclist",oiclist);
//                intent.putExtras(bundle);
//                int b=0;
//                intent.setClass(getActivity(), Elder_Update.class);
//                startActivity(intent);
//                Toast.makeText(getActivity(), "onclick" + mData.get(position).mem0002, Toast.LENGTH_SHORT).show();
            }
        });
//********************************* ****
        rv001.setAdapter(adapter);
    }

    private DialogInterface.OnClickListener aldBtListener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String msg="";
            switch (which){
                case BUTTON_POSITIVE:
                    Toast.makeText(getActivity(), "確認刪除", Toast.LENGTH_SHORT).show();
                    mysql_del();
                    onSwipeToRefresh001.onRefresh();  //開始轉圈下載資料
                    break;
                case BUTTON_NEGATIVE:
                    Toast.makeText(getActivity(), "取消刪除", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //=========     這裡是msqli         ============================

    private void initdb() {
        if(account!=null){
            DB_FILE = "CAYF.db";
            if (U_dbHper==null)
            {
                U_dbHper=new Elder_editDbHelper(getActivity(),DB_FILE,null,DBversion);
                U_dbHper.creatfile();
            }
            if (U_home_dpHper==null)
            {
                U_home_dpHper=new HomeDbHelper(getActivity(),DB_FILE,null,DBversion);
                U_home_dpHper.createHomeTable();
            }

            dbHper = U_dbHper;
            home_dpHper = U_home_dpHper;
        }
        if(account==null){
            DB_FILE = "guest.db";
            if (G_dbHper==null)
            {
                G_dbHper=new Elder_editDbHelper(getActivity(),DB_FILE,null,DBversion);
                G_dbHper.creatfile();
            }
            if (G_home_dpHper==null)
            {
                G_home_dpHper=new HomeDbHelper(getActivity(),DB_FILE,null,DBversion);
                G_home_dpHper.createHomeTable();
            }

            dbHper = G_dbHper;
            home_dpHper = G_home_dpHper;
        }
    }
    private void dbmysql() {

        String family_id = userRowData.get(6);
        String sqlctl = "SELECT * FROM elder WHERE eld007 = "+family_id+" ORDER BY eld001 DESC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Elder_DBConnector.executeQuery(nameValuePairs);
            int c=0;

            //=================================================
            chk_httpstate();
            //=================================================

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
//                int rowsAffected = dbHper.clearRec();                 // 匯入前,刪除所有SQLite資料
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
    private void mysql_del() {
        String t_id= mem00001.trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(t_id);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------
        if( Integer.valueOf(userRowData.get(0))!=0 ){
            String result = Elder_DBConnector.executeDelet(nameValuePairs);
        }
        if( Integer.valueOf(userRowData.get(0))==0 ){
            dbHper.deleteRec(t_id);
        }

    }



    //=====================================================
    private void chk_httpstate() {
        ////-------------------------------
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)

        if (Elder_DBConnector.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + Elder_DBConnector.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Navy);
//                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",Toast.LENGTH_SHORT).show();
        } else {
            int checkcode = Elder_DBConnector.httpstate / 100;
            switch (checkcode) {
                case 1:
                    ser_msg = "資訊回應(code:" + Elder_DBConnector.httpstate + ") ";
                    break;
                case 2:
                    ser_msg = "已經完成由伺服器會入資料(code:" + Elder_DBConnector.httpstate + ") ";
                    break;
                case 3:
                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + Elder_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + Elder_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + Elder_DBConnector.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(getActivity(), R.color.Red);
                    break;
            }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }
        if (Elder_DBConnector.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + Elder_DBConnector.httpstate + ") ";
        }
    }

    private DialogInterface.OnClickListener altDlgOnClkNeutBtnLis =new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ls001.setRefreshing(false);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 100);  //10秒 跑10秒
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Home_elder_fragment_t099:
                Intent intent=new Intent();
                intent.setClass(getActivity(), Family.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //setdbrefresh();
        onSwipeToRefresh001.onRefresh();
        mem = dbHper.FindMEMBER();
//       initdb();
//
//        if (createFamilyBtnflag){
//            createFamilyBtn.setVisibility(View.VISIBLE);
//        }else {
//            createFamilyBtn.setVisibility(View.GONE);
//        }
//        if (fabflag){
//            fab.setVisibility(View.VISIBLE);
//        }else {
//            fab.setVisibility(View.GONE);
//        }
//        onSwipeToRefresh001.onRefresh();
//
//
//        if (dbHper == null) {
//            dbHper = new Elder_editDbHelper(getActivity(), DB_FILE, null, DBversion);
//        }


    }

    private void setdbrefresh() {
        mem = dbHper.FindMEMBER();
        if (mem[0] == null) {
            createFamilyBtnflag = false;
            createFamilyBtn.setVisibility(View.GONE);
            fabflag = false;
            fab.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "訪客洗洗睡!", Toast.LENGTH_SHORT).show();

            ls001.setRefreshing(false);
        } else {
//            dbmysql();
            String e = mem[6];
            int f = 0;
            if (mem[6].equals("0")) {
                fabflag = false;
                fab.setVisibility(View.GONE);
                createFamilyBtnflag = true;
                createFamilyBtn.setVisibility(View.VISIBLE);
            } else {
                fabflag = true;
                fab.setVisibility(View.VISIBLE);
                createFamilyBtnflag = false;
                createFamilyBtn.setVisibility(View.GONE);
            }
            String a = mem[6];
            int b = 0;
            ls001.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    =================================
                    setDatatolist();
//                  =================================
                    //----------SwipeLayout 結束 --------
                    //可改放到最終位置 u_importopendata()

                    ls001.setRefreshing(false);
                    Toast.makeText(getActivity(), "讀取完畢", Toast.LENGTH_SHORT).show();
                }
            }, 2000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}