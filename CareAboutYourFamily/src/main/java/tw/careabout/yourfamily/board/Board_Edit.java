package tw.careabout.yourfamily.board;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import tw.careabout.yourfamily.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;



public class Board_Edit extends AppCompatActivity {

    //SQLite資料庫宣告
    private String DB_Name = "CAYF.db";
    private int DBversion = 1;
    private BoardDBHper boardDBHper;

    private float density,w,h;
    private TextView edit_btn;
    private String title, context, I;
    private int id,post_position;
    private EditText edit_title, edit_context;
    private Menu menu;
    private int tabselect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_edit);
        DisplayMetrics dm = new DisplayMetrics(); //找出使用者手機的寬高
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        w=dm.widthPixels;
        h=dm.heightPixels;
        initDB();
        setViewComponent();

    }

    private void setViewComponent() {
        Bundle bundle = this.getIntent().getExtras();
        int a=0;
        id = bundle.getInt("id");
        post_position = bundle.getInt("post_position");
        title = bundle.getString("title");
        context = bundle.getString("context");
        tabselect = bundle.getInt("tabselect");
        I = bundle.getString("I");

        edit_title = (EditText)findViewById(R.id.boardEdit_ed001);
        edit_context = (EditText)findViewById(R.id.boardEdit_ed002);
        edit_title.setText(title);
        edit_context.setText(context);

        edit_btn = (TextView)findViewById(R.id.boardEdit_editbtn);
        edit_btn.setOnClickListener(editON);
    }

    private void initDB() {
        if (boardDBHper == null) {
            boardDBHper = new BoardDBHper(this, DB_Name, null, DBversion);
            boardDBHper.createBoardTable();
        }
    }

    private View.OnClickListener editON = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SimpleDateFormat timeFormat= new SimpleDateFormat("yyyy/MM/dd E hh:mm:ss", Locale.TAIWAN);
            timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String editTime = timeFormat.format(new Date());

            int updatecode = BoardDBconnector.executeUpdate(
                    id,
                    edit_title.getText().toString(),
                    edit_context.getText().toString(),
                    editTime
            );

            if(updatecode==0){
                Toast.makeText(getApplicationContext(),getString(R.string.notNULLmsg),Toast.LENGTH_SHORT).show();
            }
            if(updatecode==1){
                boardDBHper.updateRec(
                        id,
                        edit_title.getText().toString(),
                        edit_context.getText().toString(),
                        editTime
                );
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("tabselect",tabselect);
                bundle.putInt("post_position",post_position);
                bundle.putString("I",I);
                intent.setClass(Board_Edit.this, Board_Show.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    };

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
                Bundle bundle = new Bundle();
                bundle.putInt("tabselect",tabselect);
                bundle.putInt("post_position",post_position);
                bundle.putString("I",I);
                menu_it.setClass(getApplicationContext(), Board_Show.class);
                menu_it.putExtras(bundle);
                startActivity(menu_it);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}