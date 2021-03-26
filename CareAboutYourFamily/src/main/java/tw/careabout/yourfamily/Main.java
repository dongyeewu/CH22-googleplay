package tw.careabout.yourfamily;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import tw.careabout.yourfamily.board.Board;
import tw.careabout.yourfamily.board.BoardDBHper;
import tw.careabout.yourfamily.board.BoardDBconnector;
import tw.careabout.yourfamily.home.HomeDBconnector;
import tw.careabout.yourfamily.home.HomeDbHelper;
import tw.careabout.yourfamily.record.Record_DBConnector;
import tw.careabout.yourfamily.record.Record_DbHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Main extends AppCompatActivity {


    //SQLite資料庫宣告
    private String DB_Name = "";
    private int DBversion = 1;
    private int groupID = 0;
    private String guest = "guest";
    private String lastLoginTime = "";
    private HomeDbHelper home_dpHper, G_home_dpHper, U_home_dpHper;
    private Record_DbHelper record_dbHelper, G_record_dbHelper, U_record_dbHelper;
    private Elder_editDbHelper elder_dbHper, G_elder_dbHper, U_elder_dbHper;
    private BoardDBHper boardDBHper;
    private FamilyDbHelper family_dbHper;


    //--登入登出 宣告
    private static final int RC_SIGN_IN = 9001;  //15-7的intent code
    private static final int RC_GET_TOKEN = 9002; //15-9的intent code
    private TextView mStatusTextView;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "tcnr26=>";
    private GoogleSignInAccount account;
    private MenuItem m_logout,m_login;

    //--Main宣告
    private Intent intent = new Intent();
    private Menu menu;
    private BottomNavigationView bnv;
    private NavController navController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStricMode(); //檢查，有連結MySQL一定要有的方法
        setupSignView();
        initDB();
        super.onCreate(savedInstanceState);
        createGuestSQLite();
        setContentView(R.layout.main);
        setupCAYFbarConponent();
    }

    private void enableStricMode() {
        //-------------抓取遠端資料庫設定執行續------------------------------
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new StrictMode
                        .VmPolicy
                        .Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .penaltyDeath()
                        .build());
    }

    private void setupSignView() {
        mStatusTextView = findViewById(R.id.status);

        // For sample only: make sure there is a valid server client ID.
        validateServerClientID();

        // --START configure_signin--
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        //--END configure_signin---

        // --START build_client--
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //--END build_client--
    }

    private void initDB() {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            DB_Name = "CAYF.db";
            if (U_home_dpHper == null) {
                U_home_dpHper = new HomeDbHelper(this, DB_Name, null, DBversion);
                U_home_dpHper.createHomeTable();
            }
            if (U_elder_dbHper == null) {
                U_elder_dbHper = new Elder_editDbHelper(this, DB_Name, null, DBversion);
                U_elder_dbHper.creatfile();
            }
            if (U_record_dbHelper == null) {
                U_record_dbHelper = new Record_DbHelper(this);
                U_record_dbHelper.createTable();
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

            home_dpHper = U_home_dpHper;
            elder_dbHper = U_elder_dbHper;
            record_dbHelper = U_record_dbHelper;
        }
        if(account==null){
            DB_Name = "guest.db";
            if (G_home_dpHper == null) {
                G_home_dpHper = new HomeDbHelper(this, DB_Name, null, DBversion);
                G_home_dpHper.createHomeTable();
            }
            if (G_elder_dbHper == null) {
                G_elder_dbHper = new Elder_editDbHelper(this, DB_Name, null, DBversion);
                G_elder_dbHper.creatfile();
            }
            if (G_record_dbHelper == null) {
                G_record_dbHelper = new Record_DbHelper(this);
                G_record_dbHelper.createTable();
            }

            home_dpHper = G_home_dpHper;
            elder_dbHper = G_elder_dbHper;
            record_dbHelper = G_record_dbHelper;
        }
    }

    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in google_sign.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    private void setupCAYFbarConponent() {
        bnv = (BottomNavigationView) findViewById(R.id.cayf_bnv);
        bnv.setSelectedItemId(R.id.nav_item01);
        //bnv.setOnNavigationItemSelectedListener(bnvON);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_item01, R.id.nav_item02, R.id.nav_item03, R.id.nav_item04, R.id.nav_item05)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bnv, navController);

        //getMenuInflater().inflate(R.menu.bottom_nav_menu,);

    }

    //----------------------------------------生命週期---------------------------------------------------------------------
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(this);
        initDB();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //----------------------------------------登出登入---------------------------------------------------------------------
    private void signIn() {
        navController.navigate(R.id.nav_item01);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_TOKEN);

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(account == null) {
                            main_menu_login();
                            Toast.makeText(getApplicationContext(),"登出成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        try{
            home_dpHper.clearMemberRec();
            boardDBHper.clearBoardRec();
            elder_dbHper.clearelderRec();
            record_dbHelper.recClear_re();
            record_dbHelper.eldClear_re();
            record_dbHelper.clearRESHjoin_rs();
            family_dbHper.clearRec_fm();
            family_dbHper.clearRec();

        }catch (Exception e){

        }

        home_dpHper.close();
        boardDBHper.close();
        elder_dbHper.close();
        family_dbHper.close();
        family_dbHper.close();
        record_dbHelper.close();
        record_dbHelper.close();
        record_dbHelper.close();

        initDB();
        navController.navigate(R.id.nav_item01);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_TOKEN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    //--END onActivityResult--

    // --START handleSignInResult--
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.

            if( account != null ){
                main_menu_logout();
                Toast.makeText(getApplicationContext(),"登入成功",Toast.LENGTH_SHORT).show();
                //int accountState = home_dpHper.checkMemberTable(account.getEmail());
                int accountState = HomeDBconnector.checkMemberTable(account.getEmail());
                if( accountState==0 ){
                    //--建立會員資料
                    SimpleDateFormat timeFormat= new SimpleDateFormat("yyyy/MM/dd E hh:mm:ss", Locale.TAIWAN);
                    timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    lastLoginTime = timeFormat.format(new Date());
                    ArrayList<String> newMember = new ArrayList<>();
                    newMember.add(account.getGivenName());    //mem002
                    newMember.add(account.getFamilyName());   //mem003
                    newMember.add(account.getEmail());        //mem004
                    newMember.add(account.getDisplayName());  //mem005
                    newMember.add(account.getPhotoUrl().toString());     //mem006
                    newMember.add(lastLoginTime);             //mem008
                    String member_c_api_Msg = HomeDBconnector.executeInsert(newMember,groupID);
                    String msg = "";
                    if( member_c_api_Msg.equals("新增會員成功") ){
                        msg = "歡迎您第一次使用台中好照護";
                    }
                    else{
                        msg = member_c_api_Msg;
                    }
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
                else if ( accountState==1 ){
                    SimpleDateFormat timeFormat= new SimpleDateFormat("yyyy/MM/dd E hh:mm:ss", Locale.TAIWAN);
                    timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    lastLoginTime = timeFormat.format(new Date());
                    ArrayList<String> newMember = new ArrayList<>();
                    newMember.add(account.getGivenName());    //mem002
                    newMember.add(account.getFamilyName());   //mem003
                    newMember.add(account.getEmail());        //mem004
                    newMember.add(account.getDisplayName());  //mem005
                    newMember.add(account.getPhotoUrl().toString());     //mem006
                    newMember.add(lastLoginTime);             //mem008
                    String member_u_api_Msg = HomeDBconnector.executeUpdate(newMember);
                }
                MySQL_to_SQLite();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void createGuestSQLite() {
        int guestTableState =  home_dpHper.checkMemberTable();
        if( guestTableState == 0) {
            home_dpHper.insertMemberRec(
                    0,//id
                    guest, //GivenName
                    guest, //FamilyName
                    guest, //Email
                    guest, //DisplayName
                    guest, //照片URL
                    0, //群組ID
                    guest, //最後登入時間
                    guest, //預備欄位
                    guest, //預備欄位
                    guest, //預備欄位
                    guest, //預備欄位
                    guest, //預備欄位
                    guest  //預備欄位

            );
        }
    }

    private void MySQL_to_SQLite() {
        initDB();
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
                        JSONObject data = HomeDBconnector.getMemberData(account.getEmail());
                        try{
                            //--member SQLite資料表 建立
                            home_dpHper.insertMemberRec(
                                    data.getInt("mem001"),
                                    data.getString("mem002"),
                                    data.getString("mem003"),
                                    data.getString("mem004"),
                                    data.getString("mem005"),
                                    data.getString("mem006"),
                                    data.getInt("mem007"),
                                    data.getString("mem008"),
                                    data.getString("mem009"),
                                    data.getString("mem010"),
                                    data.getString("mem011"),
                                    data.getString("mem012"),
                                    data.getString("mem013"),
                                    data.getString("mem014")
                            );

                            //--board SQLite資料表 建立
                            String familytID = String.valueOf(data.getInt("mem007"));
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
                                }
                            }

                            //--family  SQLite資料表 建立
                            sqlctl = "SELECT * FROM family WHERE fam001='" + familytID + "'";
                            ArrayList<String> nameValuePairs_family = new ArrayList<>();
                            nameValuePairs_family.add(sqlctl);
                            String result_family = Family_DBConnector.fam_m_r(nameValuePairs_family);

                            JSONArray jsonArray_family = new JSONArray(result_family);
                            // -------------------------------------------------------
                            if (jsonArray_family.length() > 0) { // MySQL 連結成功有資料
//--------------------------------------------------------

                                for (int i = 0; i < jsonArray_family.length(); i++) {
                                    JSONObject jsonData = jsonArray_family.getJSONObject(i);
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

                                    // -------------------加入SQLite---------------------------------------
                                    long rowID = family_dbHper.fam_s_m(newRow);

                                }
                                // ---------------------------
                            } else {
                                return;
                            }

                            //--family member SQLite資料表 建立
                            sqlctl = "SELECT * FROM member WHERE mem007='" + familytID + "'";
                            ArrayList<String> nameValuePairs_family_mb = new ArrayList<>();
                            nameValuePairs_family_mb.add(sqlctl);
                            String result_family_mb = Family_DBConnector.mem_m_r(nameValuePairs_family_mb);
                            JSONArray jsonArray_family_mb = new JSONArray(result_family_mb);
                            if (jsonArray_family_mb.length() > 0) { // MySQL 連結成功有資料
                                for (int i = 0; i < jsonArray_family_mb.length(); i++) {
                                    JSONObject jsonData = jsonArray_family_mb.getJSONObject(i);
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
                                    // -------------------加入SQLite---------------------------------------
                                    long rowID = family_dbHper.fme_s_m(newRow);
                                }
                            } else {
                                return;
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
                        handler.sendEmptyMessage(0);
                        progDlg.cancel();
                    }
                }).start();

    }

    private final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    navController.navigate(R.id.nav_item01);
                    break;
            }
        }
    };

    //----------------------------------------menu---------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu=menu;
        m_logout = menu.findItem(R.id.m_logout);
        m_login = menu.findItem(R.id.m_login);
        if(  account!=null){
            main_menu_logout();
        }
        if( account==null){
            main_menu_login();
        }
        return true;
    }

    private void main_menu_login() {

        m_logout.setVisible(false);
        m_login.setVisible(true);
    }

    private void main_menu_logout() {

        m_logout.setVisible(true);
        m_login.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent menu_it =new Intent();
        switch (item.getItemId()) {
            case R.id.m_family:
                if(account==null){
                    String DialogMsg = getString(R.string.AskLoginDialog_familyMSG);
                    showAskLoginDialog(DialogMsg);
                    //Toast.makeText(getApplicationContext(),"要使用家庭群組功能，請先登入喔!",Toast.LENGTH_SHORT).show();
                }
                else{
                    menu_it.setClass(getApplicationContext(), Family.class);
                    startActivity(menu_it);
                    this.finish();
                }
                break;
            case R.id.m_board:
                if(account==null){
                    String DialogMsg = getString(R.string.AskLoginDialog_boardMSG);
                    showAskLoginDialog(DialogMsg);
                    //Toast.makeText(getApplicationContext(),"要使用留言板功能，請先登入喔!",Toast.LENGTH_SHORT).show();
                }
                else {
                    menu_it.setClass(getApplicationContext(), Board.class);
                    startActivity(menu_it);
                    this.finish();
                }
                break;
            case R.id.m_login:
                signIn();
                break;
            case R.id.m_logout:
                MyAlertDialog myAltDlg = new MyAlertDialog(Main.this);

                myAltDlg.setTitle(getString(R.string.AskLogoutDialog_title));
                myAltDlg.setMessage(getString(R.string.AskLogoutDialog_MSG));
                myAltDlg.setIcon(android.R.drawable.ic_popup_reminder);
                myAltDlg.setCancelable(false);

                myAltDlg.setButton(BUTTON_POSITIVE,"確定" , AskLogout);
                myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", AskLogout);


                myAltDlg.show();
                break;
            case R.id.action_settings:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAskLoginDialog(String DialogMsg) {
        MyAlertDialog AskLoginDialog = new MyAlertDialog(Main.this);

        AskLoginDialog.setTitle(getString(R.string.AskLoginDialog_title));
        AskLoginDialog.setMessage(DialogMsg);
        AskLoginDialog.setIcon(android.R.drawable.ic_popup_reminder);
        AskLoginDialog.setCancelable(false);

        AskLoginDialog.setButton(BUTTON_POSITIVE,"登入" , AskLogin);
        AskLoginDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", AskLogin);
        AskLoginDialog.show();
    }

    //--登入詢問視窗監聽
    private DialogInterface.OnClickListener AskLogin = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case BUTTON_POSITIVE:
                    signIn();
                    break;
                case BUTTON_NEGATIVE:
                    dialog.cancel();
            }
        }
    };

    //--登出詢問視窗監聽
    private DialogInterface.OnClickListener AskLogout = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which)
            {
                case BUTTON_POSITIVE:
                    signOut();
                    onResume();
                    break;
                case BUTTON_NEGATIVE:
                    dialog.cancel();
            }
        }
    };




}