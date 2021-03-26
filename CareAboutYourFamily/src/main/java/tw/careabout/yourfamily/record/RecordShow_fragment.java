package tw.careabout.yourfamily.record;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import tw.careabout.yourfamily.CircleImgView;
import tw.careabout.yourfamily.Elder_editDbHelper;
import tw.careabout.yourfamily.board.BoardDBHper;
import tw.careabout.yourfamily.FamilyDbHelper;
import tw.careabout.yourfamily.home.HomeDbHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import tw.careabout.yourfamily.R;

public class RecordShow_fragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private GoogleSignInAccount account;
    private static final int DBversion = 1;
    private ArrayList<String> recSet16, eldSet16, recJoinSet16;
    private ArrayList<String> id;
    private float x1, y1, x2, y2;
    private float range = 150;
    private int ran = 65;

    private RelativeLayout rtable00, rtable01;


    //資料表相關宣告()
    private String DB_Name = "CAYF.db";
    private HomeDbHelper home_dpHper, U_home_dpHper, G_home_dpHper;
    private Elder_editDbHelper elder_dbHper, G_elder_dbHper, U_elder_dbHper;
    private Record_DbHelper dbHper, U_record_dbHelper, G_record_dbHelper;
    private int groupID = 0;
    private String guest = "guest";
    private String lastLoginTime = "";

    private TextView edtime01, eddate01, reshwriter, edtime00, eddate00, edsex00, edsex01, reshwriter01;
    private Button b001;
    private Intent intent = new Intent();

    private TextView edname00, edname01;
    final private int sAFRrecordedit = 0;
    private Dialog eldDial;
    private EditText reshaddname;
    private String resh_eldname, resh_eldid;
    private int eldCount16, recCount16, recJoinCount16;
    private Bundle bundle_eldid;
    private TextView resh_id00, resh_id01;
    private Spinner spin;
    private String resh_email;
    private int up_item = 0;
    private int num;
    private int resh_page;
    private ImageView recordshow_note, recordshow_note01;
    private TextView Alert;

    public RecordShow_fragment() {
        // Required empty public constructor
    }

    public static RecordShow_fragment newInstance(String param1, String param2) {
        RecordShow_fragment fragment = new RecordShow_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableStrictMode();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static void enableStrictMode() {
        StrictMode.setThreadPolicy(
//                -------------抓取遠端資料庫設定執行續------------------------------
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recordshow_fragment, container, false);
        setupViewComponent(view);


        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) { //down  up  move
                        case MotionEvent.ACTION_DOWN:
                            x1 = event.getX(); // 觸控按下的 X 軸位置
                            y1 = event.getY(); // 觸控按下的 Y 軸位置
                            break;

                        case MotionEvent.ACTION_MOVE:
                            break;

                        case MotionEvent.ACTION_UP:
                            x2 = event.getX(); // 觸控放開的 X 軸位置
                            y2 = event.getY(); // 觸控放開的 Y 軸位置
                            // 判斷左右的方法，因為屏幕的左上角是：0，0 點右下角是max,max
                            // 並且移動距離需大於 > range
                            float xbar = Math.abs(x2 - x1);
                            float ybar = Math.abs(y2 - y1);
                            double z = Math.sqrt(xbar * xbar + ybar * ybar);
                            int angle = Math.round((float) (Math.asin(ybar / z) / Math.PI * 180));// 角度
                            //----------------------------------------------------------------以下為判斷
                            if (x1 != 0 && y1 != 0) {
                                if (x1 - x2 > range) { // 向左滑動
                                    ctlNext();
                                }
                                if (x2 - x1 > range) { // 向右滑動
                                    ctlPrev();
                                    // t001.setText("向右滑動\n" + "滑動參值x1=" + x1 + " x2=" + x2 + "
                                    // r=" + (x2 - x1)+"\n"+"ang="+angle);
                                }
                                if (y2 - y1 > range && angle > ran) { // 向下滑動
                                    // 往下角度需大於65(ran變數)
                                    // 最後一筆
                                    ctlLast();
                                    Toast.makeText(getActivity(), R.string.resh_lastpage, Toast.LENGTH_LONG).show();
                                }
                                if (y1 - y2 > range && angle > ran) { // 向上滑動
                                    // 往上角度需大於65(ran)
                                    // 第一筆
                                    up_item = 0;
                                    Resh_spin_iSelect();
                                    spin.setSelection(0, true); //spinner 小窗跳到第幾筆
                                    Toast.makeText(getActivity(), R.string.resh_firpage, Toast.LENGTH_LONG).show();
                                }
                            }
                            break;
                    }
                    return true;
                }
            });
        }


        return view;
    }

    private void setupViewComponent(View view) {
        Alert = (TextView) view.findViewById(R.id.recordshow_myname);
        b001 = (Button) view.findViewById(R.id.recordshow_b001);
        spin = (Spinner) view.findViewById(R.id.resh_spi);
        rtable00 = (RelativeLayout) view.findViewById(R.id.rtable00);
        rtable01 = (RelativeLayout) view.findViewById(R.id.rtable01);
        resh_id00 = (TextView) view.findViewById(R.id.resh_id00);
        resh_id01 = (TextView) view.findViewById(R.id.resh_id01);
        resh_id00.setVisibility(View.INVISIBLE);
        resh_id01.setVisibility(View.INVISIBLE);

        edname00 = (TextView) view.findViewById(R.id.recordshow_edname00);
        edsex00 = (TextView) view.findViewById(R.id.recordshow_edsex00);
        reshwriter = (TextView) view.findViewById(R.id.recordshow_edwriter00);
        edtime00 = (TextView) view.findViewById(R.id.recordshow_edtime00);
        eddate00 = (TextView) view.findViewById(R.id.recordshow_eddate00);
        edname01 = (TextView) view.findViewById(R.id.recordshow_edname01);
        edsex01 = (TextView) view.findViewById(R.id.recordshow_edsex01);
        reshwriter01 = (TextView) view.findViewById(R.id.recordshow_edwriter01);
        edtime01 = (TextView) view.findViewById(R.id.recordshow_edtime01);
        eddate01 = (TextView) view.findViewById(R.id.recordshow_eddate01);
        recordshow_note = (ImageView) view.findViewById(R.id.recordshow_note);
        recordshow_note01 = (ImageView) view.findViewById(R.id.recordshow_note01);

        edname00.setText(null);
        edname01.setText(null);
        edname00.setContentDescription("");
        edname01.setContentDescription("");

        b001.setOnClickListener(this);
        recordshow_note.setOnClickListener(this);
        recordshow_note01.setOnClickListener(this);
        spin.setOnItemSelectedListener(Resh_spin);

        //account.getEmail();     登入後取得email

        //動畫
        rtable00.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.recordshow_move_in_from_bottom));
        rtable01.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.recordshow_move_in_from_bottom02));

        //account = GoogleSignIn.getLastSignedInAccount(getActivity());
        //recList();
        spin.setOnItemSelectedListener(Resh_spin);

    }

    private void initDB() {
        if (account != null) {
            DB_Name = "CAYF.db";
            if (U_home_dpHper == null) {
                U_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                U_home_dpHper.createHomeTable();
            }
            if (U_elder_dbHper == null) {
                U_elder_dbHper = new Elder_editDbHelper(getActivity(), DB_Name, null, DBversion);
                U_elder_dbHper.creatfile();
            }
            if (U_record_dbHelper == null) {
                U_record_dbHelper = new Record_DbHelper(getActivity(), DB_Name, DBversion);
                U_record_dbHelper.createTable();
            }
            home_dpHper = U_home_dpHper;
            elder_dbHper = U_elder_dbHper;
            dbHper = U_record_dbHelper;
        }
        if (account == null) {
            DB_Name = "guest.db";
            if (G_home_dpHper == null) {
                G_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                G_home_dpHper.createHomeTable();
            }
            if (G_elder_dbHper == null) {
                G_elder_dbHper = new Elder_editDbHelper(getActivity(), DB_Name, null, DBversion);
                G_elder_dbHper.creatfile();
            }
            if (G_record_dbHelper == null) {
                G_record_dbHelper = new Record_DbHelper(getActivity(), DB_Name, DBversion);
                G_record_dbHelper.createTable();
            }

            home_dpHper = G_home_dpHper;
            elder_dbHper = G_elder_dbHper;
            dbHper = G_record_dbHelper;
        }

    }

    private void setElderData() {
        ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = CM.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {

            ArrayList<String> userRowData = new ArrayList<>();
            userRowData = home_dpHper.getUser();

            int rowsAffected = dbHper.clearRESHjoin_rs();

            ContentValues newRow = new ContentValues();
            ArrayList<String> elderRowData = new ArrayList<>();
            elderRowData = elder_dbHper.FindRec(userRowData.get(6));
            int elderCount = elder_dbHper.RecCount();
            for (int i = 0; i < elderCount; i++) {
                String[] e_colum = elderRowData.get(i).split("###");
                int a = 0;
                newRow.put("mem001", userRowData.get(0));
                newRow.put("mem004", userRowData.get(3));
                newRow.put("mem007", userRowData.get(6));
                newRow.put("eld007", e_colum[6]);
                newRow.put("eld001", e_colum[0]);
                newRow.put("eld002", e_colum[1]);
                newRow.put("eld004", e_colum[3]);
                newRow.put("eld005", e_colum[4]);
                newRow.put("eld006", e_colum[5]);
                newRow.put("eld003", e_colum[2]);
                long rowID = dbHper.insertRec_j_rs(newRow);

            }


            //dbmysql();
            recJoinSet16 = dbHper.getrecJoinSet_rs();  //重新載入SQLite
            recJoinCount16 = dbHper.reshJoinCount_rs();
            recList();
            resh_PageArray();
        } else {
            // display error
            recJoinCount16 = 0;
            resh_PageArray();
            setNull();
            Alert.setText(R.string.resh_internet);
            edname00.setContentDescription("");
            edname01.setContentDescription("");
        }
    }

    private void resh_PageArray() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item);
        if (recJoinCount16 > 2) {
            num = recJoinCount16;//總數
            resh_page = (num / 2) + (num % 2);//總頁數 (每頁2筆資料  故除以2)
            for (int i = 1; i <= resh_page; i++) {
                adapter.add("第 " + i + " 頁");
            }
        } else adapter.add("第 1 頁");

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(Resh_spin);
    }

    private Spinner.OnItemSelectedListener Resh_spin = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //position0開始(第一頁 ==0)
            int iSelect = spin.getSelectedItemPosition();//找到是按下哪一個  與position相同
            up_item = iSelect;
            //recJoinCount16 = dbHper.reshJoinCount();
            ConnectivityManager CM = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = CM.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                Resh_spin_iSelect();
            } else {
                recJoinCount16 = 0;
                spin.setSelection(0);
                up_item = 0;
                Resh_spin_iSelect();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void Resh_spin_iSelect() {
        setNull();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        //if (account != null) {

        if (recJoinCount16 != 0) {

            String[] recJoinfld = recJoinSet16.get(recJoinSet16.size() - 1 - (2 * up_item)).split("#");
            edsex00.setText(recJoinfld[6]);//性別
            resh_id00.setText(recJoinfld[4]);//被照顧者id
            edname00.setText(recJoinfld[5]);//被照顧者姓名

            //String[] time00 = recJoinfld[11].split(" ");//編輯時間
            eddate00.setText(recJoinfld[7]);//日期
            edtime00.setText(recJoinfld[8]);//時間
            reshwriter.setText(recJoinfld[9]);//建立者
            edname00.setContentDescription(recJoinfld[4]);// eld_id
            //---------------------------------------------------------------
            //if (recJoinSet16.size() - 1 > 0)
            if (recJoinSet16.size() - 2 - (2 * up_item) >= 0) {
                String[] recJoinfld01 = recJoinSet16.get(recJoinSet16.size() - 2 - (2 * up_item)).split("#");
                edsex01.setText(recJoinfld01[6]);//性別
                resh_id01.setText(recJoinfld01[4]);//被照顧者id
                edname01.setText(recJoinfld01[5]);//被照顧者姓名

                //String[] time02 = recJoinfld01[11].split(" ");//編輯時間
                eddate01.setText(recJoinfld01[7]);//日期
                edtime01.setText(recJoinfld01[8]);//時間
                reshwriter01.setText(recJoinfld01[9]);//建立者
                edname01.setContentDescription(recJoinfld01[4]);// eld_id
            } else {
                resh_id01.setText("");//被照顧者id
                edname01.setText(R.string.resh_nodata);//被照顧者姓名
                edname01.setContentDescription("");// eld_id
            }
        } else {
            spin.setSelection(0);
            resh_id01.setText("");//被照顧者id
            edname01.setText("");//被照顧者姓名
            edname01.setContentDescription("");// eld_id
            Alert.setText(R.string.resh_nodata);
        }

//        } else {
//            Toast.makeText(getActivity(), R.string.resh_login, Toast.LENGTH_SHORT).show();
//        }
    }


    private void recList() {
        setNull();
        //account = GoogleSignIn.getLastSignedInAccount(getActivity());
        //if (account != null) {
        int c = recJoinCount16;
        if (recJoinCount16 > 0)  //recJoinCount16 為0  > join無資料(用elder join)  >無被照顧者
        {
            String[] recJoinfld = recJoinSet16.get(recJoinSet16.size() - 1).split("#");
            edsex00.setText(recJoinfld[6]);//性別
            resh_id00.setText(recJoinfld[4]);//被照顧者id
            edname00.setText(recJoinfld[5]);//被照顧者姓名
            //if (recJoinfld[9] != null){// rec_id

            //String[] time00 = recJoinfld[11].split(" ");//建立時間
            eddate00.setText(recJoinfld[7]);//血型
            edtime00.setText(recJoinfld[8]);//生日
            reshwriter.setText(recJoinfld[9]);//建立者
            edname00.setContentDescription(recJoinfld[4]);// eld_id
            //---------------------------------------------------------------
            if (recJoinSet16.size() - 1 > 0) {
                String[] recJoinfld01 = recJoinSet16.get(recJoinSet16.size() - 2).split("#");
                edsex01.setText(recJoinfld01[6]);//性別
                resh_id01.setText(recJoinfld01[4]);//被照顧者id
                edname01.setText(recJoinfld01[5]);//被照顧者姓名

                //String[] time02 = recJoinfld01[11].split(" ");//建立時間
                eddate01.setText(recJoinfld01[7]);//血型
                edtime01.setText(recJoinfld01[8]);//生日
                reshwriter01.setText(recJoinfld01[9]);//建立者
                edname01.setContentDescription(recJoinfld01[4]);// eld_id
            } else {
                resh_id01.setText("");//被照顧者id
                edname01.setText(R.string.resh_nodata);//被照顧者姓名
                edname01.setContentDescription("");// eld_id 無健康紀錄
            }
        } else {
            edname00.setContentDescription("");
            edname01.setContentDescription("");
            Toast.makeText(getActivity(), R.string.resh_addelder, Toast.LENGTH_SHORT).show();
        }
        //} else {
        //Toast.makeText(getActivity(), R.string.resh_login, Toast.LENGTH_SHORT).show();
        //}
    }

    private void setNull()//先將表格清空
    {
        resh_id00.setText("");
        resh_id01.setText("");
        edname00.setContentDescription("");
        edname01.setContentDescription("");
        edname00.setText("");
        edsex00.setText("");
        reshwriter.setText("");
        edtime00.setText("");
        eddate00.setText("");
        edname01.setText("");
        edsex01.setText("");
        reshwriter01.setText("");
        edtime01.setText("");
        eddate01.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recordshow_note:
                //resh_eldid =edname00.setContentDescription(recJoinfld[5]);// eld_id
                resh_eldid = edname00.getContentDescription().toString();

                if (edname00.getContentDescription().toString() != "") {
                    String recID = edname00.getContentDescription().toString();

                    intent.setClass(getActivity(), RecordEdit.class);
                    bundle_eldid = new Bundle();
                    bundle_eldid.putString("key_eldid", recID);
                    intent.putExtras(bundle_eldid);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), R.string.resh_addelder, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.recordshow_note01:
                //-----------------------------------------------------------------
                if (edname01.getContentDescription().toString() != "") {
                    String recID = edname01.getContentDescription().toString();

                    intent.setClass(getActivity(), RecordEdit.class);
                    bundle_eldid = new Bundle();
                    bundle_eldid.putString("key_eldid", recID);
                    intent.putExtras(bundle_eldid);
                    startActivity(intent);
                } else {
                    //此無資料 intent到recordedit要.......
                    Toast.makeText(getActivity(), R.string.resh_addelder, Toast.LENGTH_SHORT).show();
                }
                //-----------------------------------------------------------------
                break;
        }
    }

    private void dbmysql() {  //用 email取member的資料
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            int a = 0;
            resh_email = account.getEmail().toString(); //登入後取得email
            if (resh_email != null) {
                ArrayList<String> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(resh_email); //資料包含之前join的東西

                try {
                    Thread.sleep(100); //  延遲Thread 睡眠0.1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String result = Record_DBConnector.executejoin_rs(nameValuePairs);
                    JSONArray jsonArray = new JSONArray(result);
                    int c = jsonArray.length();

                    if (jsonArray.length() > 0) {
                        int rowsAffected = dbHper.clearRESHjoin_rs(); // 匯入前,刪除所有SQLite資料

                        // 處理JASON 傳回來的每筆資料
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonData = jsonArray.getJSONObject(i);
                            ContentValues newRow = new ContentValues();
                            // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
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
                            long rowID = dbHper.insertRec_j_rs(newRow);
                        }
                        recJoinSet16 = dbHper.getrecJoinSet_rs();  //重新載入SQLite
                        recJoinCount16 = dbHper.reshJoinCount_rs();
                        //resh_PageArray();

                    } else {
                        Toast.makeText(getActivity(), R.string.resh_dbnodata, Toast.LENGTH_LONG).show();
                        recJoinCount16 = 0;
                    }
                    // --------------------------------------------------------
                } catch (Exception e) {
                    //Log.d(TAG, e.toString());
                }
            } else {
                Toast.makeText(getActivity(), R.string.resh_noemail, Toast.LENGTH_LONG).show();
                recJoinCount16 = 0;
            }
        } else {
            recJoinCount16 = 0;
        }
        resh_PageArray();
    }

    //----------------------------------------生命週期---------------------------------------------------------------------
    @Override
    public void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        initDB();
        setElderData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    //----------------------------------------movement--------------------------------------------------------------------
    private void ctlNext() {
        up_item++;
        if (up_item < resh_page) {
            Resh_spin_iSelect();
            spin.setSelection(up_item, true); //spinner 小窗跳到第幾筆
            //spin.setAdapter();
        } else {
            up_item = resh_page - 1;
            Toast.makeText(getActivity(), R.string.resh_lastpage, Toast.LENGTH_LONG).show();
        }
    }

    private void ctlPrev() {
        up_item--;
        if (up_item >= 0) {
            Resh_spin_iSelect();
            spin.setSelection(up_item, true); //spinner 小窗跳到第幾筆
        } else {
            up_item = 0;
            Toast.makeText(getActivity(), R.string.resh_firpage, Toast.LENGTH_LONG).show();
        }
    }

    private void ctlLast() {
        up_item = resh_page - 1;
        if (up_item < 0) {
            up_item = 0;
            Toast.makeText(getActivity(), R.string.resh_lastpage, Toast.LENGTH_LONG).show();
        }
        spin.setSelection(up_item, true); //spinner 小窗跳到第幾筆
        Resh_spin_iSelect();
    }


}