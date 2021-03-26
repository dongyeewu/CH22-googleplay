package tw.careabout.yourfamily.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tw.careabout.yourfamily.Family;
import tw.careabout.yourfamily.Main;
import tw.careabout.yourfamily.MyAlertDialog;
import tw.careabout.yourfamily.R;
import tw.careabout.yourfamily.home.HomeDbHelper;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;



import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Board extends AppCompatActivity {

    //SQLite資料庫宣告
    private String DB_Name = "CAYF.db";
    private int DBversion = 1;
    private HomeDbHelper home_dpHper;
    private BoardDBHper boardDBHper;

    private Intent intent=new Intent();
    private Handler handler = new Handler();
    private Menu menu;
    private GoogleSignInAccount account;
    private ArrayList<String> userRowData = new ArrayList<>();
    private RecyclerView board_recycleview;
    private TabLayout board_Tabs;
    private FloatingActionButton board_fab;
    private int tabselect ;
    private SwipeRefreshLayout boardSwipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStricMode(); //檢查，有連結MySQL一定要有的方法
        initDB();
        userRowData = home_dpHper.getUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        checkFamily();
        setupViewConponent();
        int boardstate = boardDBHper.checkBoardTable();
        if(boardstate==0){
            MySQL_to_SQLite();
        }
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

    private void checkFamily() {
        int familytID = Integer.valueOf(userRowData.get(6));
        if (familytID==0){
            MyAlertDialog myAltDlg = new MyAlertDialog(Board.this);

            myAltDlg.setTitle(getString(R.string.board_checkFamilyDlg_title));
            myAltDlg.setMessage(getString(R.string.board_checkFamilyDlg_Msg));
            myAltDlg.setIcon(android.R.drawable.ic_popup_reminder);
            myAltDlg.setCancelable(false);

            myAltDlg.setButton(BUTTON_POSITIVE,"確定" , goFamily);
            myAltDlg.show();
        }
    }

    private DialogInterface.OnClickListener goFamily = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case BUTTON_POSITIVE:
                    Intent goFamily_it = new Intent();
                    goFamily_it.setClass(Board.this, Family.class);
                    startActivity(goFamily_it);
                    finish();
                    break;
            }
        }
    };

    private void setupViewConponent() {

        board_Tabs = (TabLayout)findViewById(R.id.board_tabs);
        board_Tabs.addOnTabSelectedListener(switchMsg);
        board_fab = findViewById(R.id.board_fab);
        board_fab.setImageResource(android.R.drawable.ic_menu_add);
        board_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Board.this, Board_Add.class);
                startActivity(intent);
            }
        });
        
        //========================set Recyclerview==================================================
        board_recycleview = (RecyclerView)findViewById(R.id.board_recyclerView);
        
        //========================set SwipeRefresh==================================================
        boardSwipe = (SwipeRefreshLayout)findViewById(R.id.boardSwipe);
        boardSwipe.setOnRefreshListener(onSwipeToRefresh);
        boardSwipe.setSize(SwipeRefreshLayout.LARGE);
        // 設置下拉多少距離之後開始刷新數據
        boardSwipe.setDistanceToTriggerSync(100);
        // 設置進度條背景顏色
        boardSwipe.setProgressBackgroundColorSchemeColor(getColor(android.R.color.background_light));
        // 設置刷新動畫的顏色，可以設置1或者更多
        boardSwipe.setColorSchemeResources(
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
        boardSwipe.setProgressViewOffset(true, 0, 50);

    }

    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    boardDBHper.clearBoardRec();
                    MySQL_to_SQLite();
                    if(tabselect==0){
                        ArrayList<SimpleBoardData> allRec = boardDBHper.findAllRec_simple();
                        setBoard(allRec);
                    }
                    if(tabselect==1){
                        ArrayList<SimpleBoardData> myRec = boardDBHper.findmyRec_simple(userRowData.get(4));
                        setBoard(myRec);
                    }
                    boardSwipe.setRefreshing(false);
                }
            });
        }
    };

    private void initDB() {
        if (home_dpHper == null) {
            home_dpHper = new HomeDbHelper(this, DB_Name, null, DBversion);
            home_dpHper.createHomeTable();
        }
        if (boardDBHper == null) {
            boardDBHper = new BoardDBHper(this, DB_Name, null, DBversion);
            boardDBHper.createBoardTable();
        }
    }

    private TabLayout.OnTabSelectedListener switchMsg = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()){
                case 0:
                    ArrayList<SimpleBoardData> allRec = boardDBHper.findAllRec_simple();
                    setBoard(allRec);
                    tabselect = 0;
                    break;
                case 1:
                    ArrayList<SimpleBoardData> myRec = boardDBHper.findmyRec_simple(userRowData.get(4));
                    setBoard(myRec);
                    tabselect = 1;
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            switch (tab.getPosition()){
                case 0:
                    ArrayList<SimpleBoardData> allRec = boardDBHper.findAllRec_simple();
                    setBoard(allRec);
                    break;
                case 1:
                    ArrayList<SimpleBoardData> myRec = boardDBHper.findmyRec_simple(userRowData.get(4));
                    setBoard(myRec);
                    break;
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            switch (tab.getPosition()){
                case 0:
                    ArrayList<SimpleBoardData> allRec = boardDBHper.findAllRec_simple();
                    setBoard(allRec);
                    break;
                case 1:
                    ArrayList<SimpleBoardData> myRec = boardDBHper.findmyRec_simple(userRowData.get(4));
                    setBoard(myRec);
                    break;
            }
        }
    };

    //---------------------------------------生命週期-------------------------------------------------------------------
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(this);
        initDB();
        userRowData = home_dpHper.getUser();
        int boardstate = boardDBHper.checkBoardTable();
        board_Tabs.getTabAt(0).select();
        if(boardstate==0){
            MySQL_to_SQLite();
        }
    }

    private void MySQL_to_SQLite() {
        String familytID = userRowData.get(6);
        ArrayList<JSONObject> board_list = BoardDBconnector.getBoardData(familytID);
        ArrayList<SimpleBoardData> recycleData = new ArrayList<>();
        for(int i=0; i< board_list.size(); i++){
            try {
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

                SimpleBoardData item = new SimpleBoardData(
                        board_list.get(i).getString("boa004"),
                        board_list.get(i).getString("boa002"),
                        board_list.get(i).getString("boa003")
                );
                recycleData.add(item);

            }catch (Exception e){

            }
        }

        setBoard(recycleData);
    }

    private void setBoard(final ArrayList<SimpleBoardData> recycleData) {

        final Board_listAdapter board_listAdapter= new Board_listAdapter(this,recycleData);
        board_recycleview.setAdapter(board_listAdapter);
        board_recycleview.setHasFixedSize(true);
        // 設置RecyclerView為列表型態
        board_recycleview.setLayoutManager(new LinearLayoutManager(this));
        // 設置格線
        board_recycleview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        board_listAdapter.setOnItemClickListener(new Board_listAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("tabselect",tabselect);
                bundle.putInt("post_position",position);
                bundle.putString("I",recycleData.get(position).getCreater());
                intent.setClass(Board.this, Board_Show.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        
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