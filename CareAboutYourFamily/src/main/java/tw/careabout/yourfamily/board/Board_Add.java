package tw.careabout.yourfamily.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import tw.careabout.yourfamily.R;
import tw.careabout.yourfamily.home.HomeDbHelper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;



public class Board_Add extends AppCompatActivity {


    //SQLite資料庫宣告
    private String DB_Name = "CAYF.db";
    private int DBversion = 1;
    private BoardDBHper boardDBHper;
    private HomeDbHelper home_dpHper;

    private EditText addTitle, addContext;
    private TextView addBtn;
    private ArrayList<String> userRowData = new ArrayList<>();
    private Integer familyID;
    private Menu menu;

    private static final int FAST_CLICK_DELAY_TIME = 5000;//間格時間
    private static long lastClickTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_add);
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
        addTitle = (EditText)findViewById(R.id.boardAdd_ed001);
        addContext = (EditText)findViewById(R.id.boardAdd_ed002);
        addBtn = (TextView)findViewById(R.id.boardAdd_addbtn);
        addBtn.setOnClickListener(addPost);
    }

    private View.OnClickListener addPost= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if( noFastClick() ){


            userRowData = home_dpHper.getUser();
            String creater = userRowData.get(4);

            SimpleDateFormat timeFormat= new SimpleDateFormat("yyyy/MM/dd E hh:mm:ss", Locale.TAIWAN);
            timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String createTime = timeFormat.format(new Date());

            String newTitle = String.valueOf(addTitle.getText());
            String newContext = String.valueOf(addContext.getText());
            familyID = Integer.valueOf(userRowData.get(6));

            String result = BoardDBconnector.executeInsert(
                      creater,
                      createTime,
                      newTitle,
                      newContext,
                      familyID
            );

            if(result.equals("欄位不允許空白")){
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            }
            if(!result.equals("欄位不允許空白")){
                ArrayList<JSONObject> board_list = BoardDBconnector.getBoardData(String.valueOf(familyID));
                try{
                    boardDBHper.insertBoardRec(
                            board_list.get(0).getInt("boa001"),
                            board_list.get(0).getString("boa002"),//創建者
                            board_list.get(0).getString("boa003"),//創建時間
                            board_list.get(0).getString("boa004"),//標題
                            board_list.get(0).getString("boa005"),
                            board_list.get(0).getString("boa006"),
                            board_list.get(0).getInt("boa007"),
                            board_list.get(0).getString("boa008"),
                            board_list.get(0).getString("boa009"),
                            board_list.get(0).getString("boa010"),
                            board_list.get(0).getString("boa011"),
                            board_list.get(0).getString("boa012"),
                            board_list.get(0).getString("boa013")
                    );
                }catch (Exception e){

                }
                Toast.makeText(getApplicationContext(),getString(R.string.addSuccessMsg),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(Board_Add.this, Board.class);
                startActivity(intent);
                finish();
            }
            if(result==null){
                Toast.makeText(getApplicationContext(),getString(R.string.adderrorMsg),Toast.LENGTH_SHORT).show();
            }

            }
            else{
                Toast.makeText(getApplicationContext(), getString(R.string.toofast), Toast.LENGTH_SHORT).show();
            }

        }
    };

    //防止連點
    public static boolean noFastClick() {
        boolean flag = false;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME ) {
            flag = true;//大於5000時間 true
        }
        lastClickTime = currentClickTime;
        return flag;
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