package tw.careabout.yourfamily.board;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import tw.careabout.yourfamily.MyAlertDialog;
import tw.careabout.yourfamily.R;
import tw.careabout.yourfamily.home.HomeDbHelper;

import java.util.ArrayList;
import java.util.Map;


import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Board_Show extends AppCompatActivity {

    //SQLite資料庫宣告
    private String DB_Name = "CAYF.db";
    private int DBversion = 1;
    private HomeDbHelper home_dpHper;
    private BoardDBHper boardDBHper;
    private ArrayList<String> userRowData = new ArrayList<>();

    private Intent intent=new Intent();
    private float density,w,h;
    private TextView post_title, post_creater, post_createTime, post_context, post_editbtn, post_deletebtn;
    private TableRow post_editRow;
    private int post_position;
    private Menu menu;
    private String title, creater, createTime, context ,I;
    private Integer id, tabselect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_show);
        DisplayMetrics dm = new DisplayMetrics(); //找出使用者手機的寬高
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        w=dm.widthPixels;
        h=dm.heightPixels;

        initDB();
        setViewComponent();
    }

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

    private void setViewComponent() {
        Bundle bundle = this.getIntent().getExtras();
        post_position = bundle.getInt("post_position");
        tabselect = bundle.getInt("tabselect");
        I = bundle.getString("I");

        post_title = (TextView)findViewById(R.id.boardShow_title);
        post_creater = (TextView)findViewById(R.id.boardShow_creater);
        post_createTime = (TextView)findViewById(R.id.boardShow_createTime);
        post_context = (TextView)findViewById(R.id.boardShow_context);

        post_editRow = (TableRow)findViewById(R.id.boardShow_editRow);
        post_editbtn = (TextView)findViewById(R.id.boardShow_editbtn);
        post_deletebtn = (TextView)findViewById(R.id.boardShow_deletebtn);
        setPost(post_position);

    }

    private void setPost(int position) {

        ArrayList<Map<String,Object>> boardRec = new ArrayList<>();
        if( tabselect == 0 ){
            boardRec = boardDBHper.findAllRec();
        }
        if( tabselect == 1 ){
            boardRec = boardDBHper.findmyRec(I);
        }

        id =  (int)boardRec.get(position).get("boa001");
        title = String.valueOf(boardRec.get(position).get("boa004"));
        creater = String.valueOf(boardRec.get(position).get("boa002"));
        if( String.valueOf(boardRec.get(position).get("boa006")).equals("null") ){
            createTime = String.valueOf(boardRec.get(position).get("boa003"));
        }
        if( !String.valueOf(boardRec.get(position).get("boa006")).equals("null") ){
            createTime = String.valueOf(boardRec.get(position).get("boa006"));
        }
        context = String.valueOf(boardRec.get(position).get("boa005"));

        post_title.setText(getString(R.string.boardShow_title)+title);
        post_creater.setText(getString(R.string.boardShow_creater)+creater);
        post_createTime.setText(getString(R.string.boardShow_createTime)+"\n"+createTime);
        post_context.setText(context);

        userRowData = home_dpHper.getUser();
        if( userRowData.get(4).equals(boardRec.get(position).get("boa002")) ){
            post_editRow.setVisibility(View.VISIBLE);
            post_editRow.setMinimumWidth((int)w/2);

            post_editbtn.setWidth((int)(w/4));
            post_deletebtn.setWidth((int)w/4);
            post_editbtn.setOnClickListener(goEdit);
            post_deletebtn.setOnClickListener(deletePost);
        }

    }

    private View.OnClickListener goEdit= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent goEditIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            bundle.putInt("tabselect",tabselect);
            bundle.putInt("post_position",post_position);
            bundle.putString("I",I);
            bundle.putString("title",title);
            bundle.putString("context",context);
            goEditIntent.putExtras(bundle);
            goEditIntent.setClass(Board_Show.this, Board_Edit.class);
            startActivity(goEditIntent);
            finish();
        }
    };

    private View.OnClickListener deletePost= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyAlertDialog myAltDlg = new MyAlertDialog(Board_Show.this);

            myAltDlg.setTitle(getString(R.string.boardShow_deleteDlgTitle));
            myAltDlg.setMessage(getString(R.string.boardShow_deleteDlgMsg));
            myAltDlg.setIcon(android.R.drawable.ic_popup_reminder);
            myAltDlg.setCancelable(false);

            myAltDlg.setButton(BUTTON_POSITIVE,"確定" , AskDelete);
            myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", AskDelete);

            myAltDlg.show();
        }
    };

    private DialogInterface.OnClickListener AskDelete = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case BUTTON_POSITIVE:
                    boardDBHper.deleteBoardRec(id);
                    String result = BoardDBconnector.executeDelete(id);
                    Intent delet_it = new Intent();
                    delet_it.setClass(Board_Show.this, Board.class);
                    startActivity(delet_it);
                    finish();
                    break;
                case BUTTON_NEGATIVE:
                    dialog.cancel();
            }
        }
    };

    //----------------------------------------生命週期-----------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        setViewComponent();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    //----------------------------------------menu---------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.goback_menu, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent menu_it =new Intent();
        switch (item.getItemId()) {
            case R.id.goback_btn:
                menu_it.setClass(getApplicationContext(), Board.class);
                startActivity(menu_it);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}