package tw.careabout.yourfamily.record;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tw.careabout.yourfamily.Elder_editDbHelper;
import tw.careabout.yourfamily.board.BoardDBHper;
import tw.careabout.yourfamily.FamilyDbHelper;
import tw.careabout.yourfamily.home.HomeDbHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import tw.careabout.yourfamily.R;

public class RecordEdit extends AppCompatActivity implements
        View.OnClickListener,
        View.OnLongClickListener {

    private GoogleSignInAccount account;
    private int user = 0;

    private String DB_Name = "";
    private int DBversion = 1;
    private HomeDbHelper home_dpHper, G_home_dpHper, U_home_dpHper;
    private Record_DbHelper dbHper, G_record_dbHelper, U_record_dbHelper;
    private Elder_editDbHelper elder_dbHper, G_elder_dbHper, U_elder_dbHper;

    private ArrayList<String> findeldSet_re = new ArrayList<>(),
            findrecSet_re = new ArrayList<>();
    private static final String
            DB_TABLE_ELD = "elder",
            DB_COLUMN_ELD_eldID = "eld001",
            DB_COLUMN_ELDs = "eld001, eld002, eld004, eld005, eld006, eld007",
            DB_TABLE_REC = "recordedit",
            DB_COLUMN_REC_recID = "rec001",
            DB_COLUMN_REC_eldID = "rec002",
            DB_COLUMN_RECs = "rec001, rec002, rec003, rec004, rec005, rec006, " +
                    "rec007, rec008, rec009, rec010, rec011, rec012, " +
                    "rec013, rec014, rec015, rec016, rec017, rec018";

    private String sdfnow = "", eldID = null, grpID = null;
    private int aniswitcher = 0, chknetswitcher = 0, valueswitcher = 0, updateswitcher = 0, editswitcher = 0;

    private SwipeRefreshLayout sl000;
    private RelativeLayout rl000;
    private TableRow tr010, tr170, tr499;
    private Spinner sppage, spsex, spbloodtype;
    private NestedScrollView sv010;// 解決 SwipeRefreshLayout 衝突！！
    private TextView tvalue, tbuilddate, teditdate, tbirth, tagetitle, tage,
            tsex, tbloodtype, tbmititle, tbmi, tbmimessage, tbuilder, teditor, ename, etall;
    private EditText eheight, eweight, esystolicbloodp, ediastolicbloodp, ebeforebloods,
            etemperature, eafterbloods, eeat, eturn, epee, epoo;
    private Menu menu;

    private final Handler handler = new Handler();

    private static final int FAST_CLICK_DELAY_TIME = 5000;//間格時間
    private static long lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordedit);
        enableStrictMode();
        setupELDid();
        setupViewComponent();
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

    private void setupELDid() {
        Bundle bundle = this.getIntent().getExtras();
        eldID = bundle.getString("key_eldid");
    }

    private void setupViewComponent() {
        setupRefresh();

        rl000 = findViewById(R.id.recordedit_rl000);
        rl000.setVisibility(View.INVISIBLE);

        //動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 95 / 100;// 設定 ScrollView 使用尺寸
        sv010 = findViewById(R.id.recordedit_sv010);
        sv010.getLayoutParams().height = newscrollheight;
        sv010.setLayoutParams(sv010.getLayoutParams());// 重定 ScrollView 大小

        sppage = findViewById(R.id.recordedit_sppage);
        spsex = findViewById(R.id.recordedit_spsex);
        spbloodtype = findViewById(R.id.recordedit_spbloodtype);

        tr010 = findViewById(R.id.recordedit_tr010);
        tr170 = findViewById(R.id.recordedit_tr170);
        tr499 = findViewById(R.id.recordedit_tr499);

        ename = findViewById(R.id.recordedit_e500);
        eheight = findViewById(R.id.recordedit_e501);
        eweight = findViewById(R.id.recordedit_e502);
        etemperature = findViewById(R.id.recordedit_e503);
        esystolicbloodp = findViewById(R.id.recordedit_e504);
        ediastolicbloodp = findViewById(R.id.recordedit_e505);
        ebeforebloods = findViewById(R.id.recordedit_e506);
        eafterbloods = findViewById(R.id.recordedit_e507);
        eeat = findViewById(R.id.recordedit_e508);
        eturn = findViewById(R.id.recordedit_e509);
        epee = findViewById(R.id.recordedit_e510);
        epoo = findViewById(R.id.recordedit_e511);

        TextView tnextpage = findViewById(R.id.recordedit_tnextpage);
        TextView tpreviouspage = findViewById(R.id.recordedit_tpreviouspage);
        tbuilddate = findViewById(R.id.recordedit_tbuilddate);
        teditdate = findViewById(R.id.recordedit_teditdate);
        tbirth = findViewById(R.id.recordedit_tbirth);
        tagetitle = findViewById(R.id.recordedit_tagetitle);
        tage = findViewById(R.id.recordedit_tage);
        tsex = findViewById(R.id.recordedit_tsex);
        tbloodtype = findViewById(R.id.recordedit_tbloodtype);
        tbmititle = findViewById(R.id.recordedit_tbmititle);
        tbmi = findViewById(R.id.recordedit_tbmi);
        tbmimessage = findViewById(R.id.recordedit_tbmimessage);
        tbuilder = findViewById(R.id.recordedit_tbuilder);
        teditor = findViewById(R.id.recordedit_teditor);

/*        Typeface typeface = Typeface.createFromAsset(getAssets(), "msjh.ttc");
        FontUtils fontUtils = new FontUtils();
        fontUtils.applyFontToView(tvalue, typeface);*/
//        src\main\assets 裡面放 英文名 字型
//        build.gradle 新增 implementation 'com.ajts.androidmads.fontutils:fontutils:1.0.2'

        tnextpage.setOnClickListener(this);
        tnextpage.setOnLongClickListener(this);
        tpreviouspage.setOnClickListener(this);
        tpreviouspage.setOnLongClickListener(this);

/*        tmyname =  findViewById(R.id.recordedit_tmyname);
        tmyname.setOnClickListener(this);
        tmyname.setOnLongClickListener(tmynameOnLong);*/

        this.setTitle(getString(R.string.recordedit_title));
        setupValue();
    }

    private void setupRefresh() {
        sl000 = findViewById(R.id.recordedit_sl000);
        sl000.setSize(SwipeRefreshLayout.LARGE);
        // 设置下拉多少距离之后开始刷新数据
        sl000.setDistanceToTriggerSync(256);
        // 设置进度条背景颜色
        sl000.setProgressBackgroundColorSchemeColor(getColor(R.color.DarkSalmon));
        // 设置刷新动画的颜色，可以设置1或者更多
        sl000.setColorSchemeColors(
                getColor(R.color.Red),
                getColor(R.color.Green),
                getColor(R.color.White),
                getColor(R.color.Black));
/*                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);*/

        sl000.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dbMySQL_Refresh();
            }
        });
    }

    private void setupValue() {
        tvalue = findViewById(R.id.recordedit_tvalue);
        tvalue.setOnClickListener(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] colorarray = new int[]{
                        R.color.Black, R.color.Brown, R.color.Red, R.color.Green,
                        R.color.Blue, R.color.Purple, R.color.DimGray
                };
                int i = (int) (Math.random() * 7);// 生成包含0到包含6的正整數 範圍是7個

                switch (valueswitcher) {
                    case 0:
                        tvalue.setText(getString(R.string.recordedit_bmivalue));
                        valueswitcher = 1;
                        break;
                    case 1:
                        tvalue.setText(getString(R.string.recordedit_bloodvalue));
                        valueswitcher = 0;
                        break;
                }
                tvalue.setTextColor(getColor(colorarray[i]));
                tvalue.setVisibility(View.VISIBLE);
                tvalue.startAnimation(AnimationUtils.loadAnimation(RecordEdit.this, R.anim.recordedit_move_left_to_right));

                handler.postDelayed(this, 6000);
            }
        }, 1500);//初始延遲時間
    }

    private void initDB() {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
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
                U_record_dbHelper = new Record_DbHelper(this, DB_Name, DBversion);
                U_record_dbHelper.createTable();
            }

            home_dpHper = U_home_dpHper;
            elder_dbHper = U_elder_dbHper;
            dbHper = U_record_dbHelper;
        }
        if (account == null) {
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
                G_record_dbHelper = new Record_DbHelper(this, DB_Name, DBversion);
                G_record_dbHelper.createTable();
            }

            home_dpHper = G_home_dpHper;
            elder_dbHper = G_elder_dbHper;
            dbHper = G_record_dbHelper;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // --START on_start_sign_in--
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            user = 1;
        }
        //--END on_start_sign_in--

        initDB();
        dbMySQL_Refresh();
    }

    private void setupClearData() {
        for (int i = 501; i < 512; i++) {
            @SuppressLint("DefaultLocale") String idName = "recordedit_e" + String.format("%03d", i);//%02d=xxx01 %03d=xxx001
            int etresID = getResources().getIdentifier(idName, "id", getPackageName());
            etall = findViewById(etresID);
            etall.setFocusable(false);
            etall.setFocusableInTouchMode(false);
            etall.setText(null);
            etall.setHint(getString(R.string.recordedit_nodata));
        }
        //tbirth.setText(null);
        //tsex.setText(null);
        //tbloodtype.setText(null);
        tbuilder.setText(null);
        teditor.setText(null);
        tbuilddate.setText(null);
        teditdate.setText(null);
        tbirth.setHint(getString(R.string.recordedit_nodata));
        tsex.setHint(getString(R.string.recordedit_nodata));
        tbloodtype.setHint(getString(R.string.recordedit_nodata));
        ename.setHintTextColor(tbuilder.getHintTextColors());
        esystolicbloodp.setTextColor(ename.getTextColors());
        ediastolicbloodp.setTextColor(ename.getTextColors());
        ebeforebloods.setTextColor(ename.getTextColors());
        tagetitle.setVisibility(View.GONE);
        tage.setVisibility(View.GONE);
        tbirth.setClickable(false);

        spsex.setSelection(0);
        spbloodtype.setSelection(0);
    }// sppage 未歸零

    @SuppressLint("SetTextI18n")
    private void setupRead() {
        sl000.setEnabled(true);

/*        String strename = ename.getText().toString().trim();
        String strspsex = spsex.getSelectedItem().toString();
        String strspbloodtype = spbloodtype.getSelectedItem().toString();*/
        String strtbirth = tbirth.getText().toString();

//      String elderID
        String streheight = eheight.getText().toString().trim();
        String streweight = eweight.getText().toString().trim();
//        String stretemperature = etemperature.getText().toString().trim();
        String stresystolicbloodp = esystolicbloodp.getText().toString().trim();//收縮壓
        String strediastolicbloodp = ediastolicbloodp.getText().toString().trim();//舒張壓
        String strebeforebloods = ebeforebloods.getText().toString().trim();
/*        String streafterbloods = eafterbloods.getText().toString().trim();
        String streeat = eeat.getText().toString().trim();
        String streturn = eturn.getText().toString().trim();
        String strepee = epee.getText().toString().trim();//小便
        String strepoo = epoo.getText().toString().trim();//大便
        String strtbuilder = tbuilder.getText().toString();
        String strtbuilddate = tbuilddate.getText().toString();
        String strteditor = teditor.getText().toString();
        String strteditdate = teditdate.getText().toString();*/
//      String grpID

        if (stresystolicbloodp.length() > 0) {// 務必注意 parseInt 內屬性 錯誤會閃退
            int intesystolicbloodp = Integer.parseInt(stresystolicbloodp);
            if (intesystolicbloodp > 119 && intesystolicbloodp < 140) {
                esystolicbloodp.setTextColor(getColor(R.color.Orange));
            } else if (intesystolicbloodp > 139) {
                esystolicbloodp.setTextColor(getColor(R.color.Red));
            } else {
                esystolicbloodp.setTextColor(getColor(R.color.Green));
            }
        }

        if (strediastolicbloodp.length() > 0) {
            int intediastolicbloodp = Integer.parseInt(strediastolicbloodp);
            if (intediastolicbloodp > 79 && intediastolicbloodp < 90) {
                ediastolicbloodp.setTextColor(getColor(R.color.Orange));
            } else if (intediastolicbloodp > 89) {
                ediastolicbloodp.setTextColor(getColor(R.color.Red));
            } else {
                ediastolicbloodp.setTextColor(getColor(R.color.Green));
            }
        }

        if (strebeforebloods.length() > 0) {
            int intebeforebloods = Integer.parseInt(strebeforebloods);
            if (intebeforebloods > 99 && intebeforebloods < 126) {
                ebeforebloods.setTextColor(getColor(R.color.Orange));
            } else if (intebeforebloods > 125) {
                ebeforebloods.setTextColor(getColor(R.color.Red));
            } else {
                ebeforebloods.setTextColor(getColor(R.color.Green));
            }
        }

        if (streheight.length() > 0 && streweight.length() > 0) {
            float floateheight = Float.parseFloat(streheight) / 100;
            float floateweight = Float.parseFloat(streweight);
            if (floateheight != 0 && floateweight != 0) {
                DecimalFormat bmiformat = new DecimalFormat("#.##");
                float bmirs = floateweight / floateheight / floateheight;
                String strbmirs = bmiformat.format(bmirs);
                if (bmirs < 18.5) {
                    tbmimessage.setTextColor(getColor(R.color.Orange));
                    tbmimessage.setText(getString(R.string.recordedit_bmi_ls18_5));
                } else if (bmirs >= 18.5 && bmirs < 24) {
                    tbmimessage.setTextColor(getColor(R.color.Green));
                    tbmimessage.setText(getString(R.string.recordedit_bmi_eqgt18_5_ls24));
                } else if (bmirs >= 24 && bmirs < 27) {
                    tbmimessage.setTextColor(getColor(R.color.Orange));
                    tbmimessage.setText(getString(R.string.recordedit_bmi_eqgt24_ls27));
                } else {
                    tbmimessage.setTextColor(getColor(R.color.Red));
                    tbmimessage.setText(getString(R.string.recordedit_bmi_eqgt27));
                }
                tbmi.setText(strbmirs);
                tbmi.setVisibility(View.VISIBLE);
                tbmititle.setVisibility(View.VISIBLE);
                tbmimessage.setVisibility(View.VISIBLE);
            } else {
                tbmi.setVisibility(View.GONE);
                tbmititle.setVisibility(View.GONE);
                tbmimessage.setVisibility(View.GONE);
            }
        } else {
            tbmi.setVisibility(View.GONE);
            tbmititle.setVisibility(View.GONE);
            tbmimessage.setVisibility(View.GONE);
        }

/*        if (strename.equals("")) {
            RecordEdit.this.setTitle(getString(R.string.recordedit_title));
        } else {
            RecordEdit.this.setTitle(strename + getString(R.string.recordedit_titlemessage));
        }*/

        for (int i = 500; i < 512; i++) {
            @SuppressLint("DefaultLocale") String idName = "recordedit_e" + String.format("%03d", i);//%02d=xxx01 %03d=xxx001
            int etresID = getResources().getIdentifier(idName, "id", getPackageName());
            etall = findViewById(etresID);
            etall.setFocusable(false);
            etall.setFocusableInTouchMode(false);
            if (etall.getText().toString().trim().equals("")) {
                etall.setHint(getString(R.string.recordedit_nodata));
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etall.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }// 隱藏小鍵盤

        if (strtbirth.length() > 0) {
            String[] arytbirth = strtbirth.split("-");
            java.util.Calendar cnow = java.util.Calendar.getInstance();
            int yearnow = cnow.get(java.util.Calendar.YEAR);
            int monthnow = cnow.get(java.util.Calendar.MONTH);
            int today = cnow.get(java.util.Calendar.DAY_OF_MONTH);
            int age = yearnow - Integer.parseInt(arytbirth[0]);
            int diffmonth = monthnow - Integer.parseInt(arytbirth[1]) + 1;
            int diffdayofmonth = today - Integer.parseInt(arytbirth[2]);
            if (age == 0) {
                if (diffmonth == 0) {
                    if (diffdayofmonth >= 0) {
                        tage.setText(getString(R.string.recordedit_agedd));
                    }
                } else if (diffmonth > 0) {
                    tage.setText(diffmonth + getString(R.string.recordedit_agemm));
                }
            } else if (age > 0) {
                tage.setText(age + getString(R.string.recordedit_ageyy));
            }
            tagetitle.setVisibility(View.VISIBLE);
            tage.setVisibility(View.VISIBLE);
        } else {
            tbirth.setHintTextColor(ename.getHintTextColors());
            tbirth.setHint(getString(R.string.recordedit_nodata));
            tagetitle.setVisibility(View.GONE);
            tage.setVisibility(View.GONE);
        }
        tbirth.setClickable(false);

        if (spsex.getSelectedItemPosition() == 0) {
            tsex.setHint(getString(R.string.recordedit_nodata));
        } else {
            tsex.setText(spsex.getSelectedItem().toString());
        }
        if (spbloodtype.getSelectedItemPosition() == 0) {
            tbloodtype.setHint(getString(R.string.recordedit_nodata));
        } else {
            tbloodtype.setText(spbloodtype.getSelectedItem().toString());
        }

        tr010.setVisibility(View.VISIBLE);
        tr170.setVisibility(View.VISIBLE);
        tr499.setVisibility(View.VISIBLE);
        tsex.setVisibility(View.VISIBLE);
        tbloodtype.setVisibility(View.VISIBLE);
        spsex.setVisibility(View.GONE);
        spbloodtype.setVisibility(View.GONE);

        handler.post(new Runnable() {
            @Override
            public void run() {
                sv010.fullScroll(ScrollView.FOCUS_UP);
            }
        });// fullScroll 正確使用方法 否則機率無效
    }

    private void setupWrite() {
        sl000.setEnabled(false);

//        RecordEdit.this.setTitle(getString(R.string.recordedit_title));

        for (int i = 500; i < 512; i++) {
            @SuppressLint("DefaultLocale") String idName = "recordedit_e" + String.format("%03d", i);//%02d=xxx01 %03d=xxx001
            int etresID = getResources().getIdentifier(idName, "id", getPackageName());
            etall = findViewById(etresID);
            etall.setFocusable(true);
            etall.setFocusableInTouchMode(true);
            if (etall.getText().toString().trim().equals("")) {
                etall.setHint(getString(R.string.recordedit_nhint));
            }
        }
        ename.setHint(getString(R.string.recordedit_shint));
        eheight.setHint(getString(R.string.recordedit_cmhint));
        eweight.setHint(getString(R.string.recordedit_kghint));
        if (tbirth.getText().toString().equals("")) {
            tbirth.setHint(getString(R.string.recordedit_birthtitle));
        }
        tbirth.setOnClickListener(this);
        esystolicbloodp.setTextColor(ename.getTextColors());
        ediastolicbloodp.setTextColor(ename.getTextColors());
        ebeforebloods.setTextColor(ename.getTextColors());

        String[] arysexs = getResources().getStringArray(R.array.recordedit_sexs);
        for (int i = 1; i < 3; i++) {
            if (tsex.getText().toString().equals(arysexs[i])) {
                spsex.setSelection(i);
            }
        }
        String[] arybloods = getResources().getStringArray(R.array.recordedit_bloods);
        for (int i = 1; i < 5; i++) {
            if (tbloodtype.getText().toString().equals(arybloods[i])) {
                spbloodtype.setSelection(i);
            }
        }

        tagetitle.setVisibility(View.GONE);
        tage.setVisibility(View.GONE);
        tr010.setVisibility(View.GONE);
        tr170.setVisibility(View.GONE);
        tr499.setVisibility(View.GONE);
        tsex.setVisibility(View.GONE);
        tbloodtype.setVisibility(View.GONE);
        spsex.setVisibility(View.VISIBLE);
        spbloodtype.setVisibility(View.VISIBLE);
        if (tbmi.getVisibility() == View.VISIBLE) {
            tbmi.setVisibility(View.GONE);
            tbmititle.setVisibility(View.GONE);
            tbmimessage.setVisibility(View.GONE);
        }

        menu.setGroupVisible(R.id.recordedit_iread, false);
        menu.setGroupVisible(R.id.recordedit_iwrite, true);
    }

    private void setupSDFdate() {
        SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.TAIWAN);
        sdfdate.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        sdfnow = sdfdate.format(new Date());
    }// 現在臺灣時間

    private void setupPage() {
        ArrayAdapter<String> paryadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        if (findrecSet_re.size() > 0) {
            for (int i = 0; i < findrecSet_re.size(); i++) {
                paryadapter.add(getString(R.string.recordedit_the) + " " + (i + 1) + " " +
                        getString(R.string.recordedit_page));
            }
        } else {
            paryadapter.add(getString(R.string.recordedit_nodata));
        }
        paryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sppage.setAdapter(paryadapter);

        sppage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dbSQLite_R();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void dbSQLite_R() {
        if (aniswitcher == 0) {
            sv010.setAnimation(AnimationUtils.loadAnimation(this, R.anim.recordedit_move_in_from_bottom));
        }
        setupClearData();
        updateswitcher = 0;
        aniswitcher = 0;

        String[] eldfld = findeldSet_re.get(0).split("#");

        ename.setText(eldfld[1]);
        tsex.setText(eldfld[2]);
        tbloodtype.setText(eldfld[3]);
        tbirth.setText(eldfld[4]);
        grpID = eldfld[5];

        tvalue.setContentDescription(eldID + "#");

        if (findrecSet_re.size() > 0) {
            int p = sppage.getSelectedItemPosition() + 1;
            String[] recfld = findrecSet_re.get(p - 1).split("#");
            for (int i = 501; i < 512; i++) {
                @SuppressLint("DefaultLocale") String idName = "recordedit_e" + String.format("%03d", i);//%02d=xxx01 %03d=xxx001
                int etresID = getResources().getIdentifier(idName, "id", getPackageName());
                etall = findViewById(etresID);
                etall.setText(recfld[i - 501 + 2]);// e501身高 = rec003 = fld[2]
            }
            tbuilder.setText(recfld[13]);
            tbuilddate.setText(recfld[14]);
            teditor.setText(recfld[15]);
            teditdate.setText(recfld[16]);

            tvalue.setContentDescription(eldID + "#" + recfld[0]);

            editswitcher = 1;
        } else {
            editswitcher = 0;//鎖定 iedit
            Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nodata), Toast.LENGTH_SHORT).show();
        }

        setupRead();
    }

    private void dbMySQL_Refresh_IN_THREAD() {
        dbHper.eldClear_re();//先清空

        ArrayList<String> elderRowData = new ArrayList<>();
        ContentValues newRow_elder = new ContentValues();

        elderRowData = elder_dbHper.selectElder(eldID);
        newRow_elder.put("eld001", elderRowData.get(0));
        newRow_elder.put("eld002", elderRowData.get(1));
        newRow_elder.put("eld004", elderRowData.get(3));
        newRow_elder.put("eld005", elderRowData.get(4));
        newRow_elder.put("eld006", elderRowData.get(5));
        newRow_elder.put("eld007", elderRowData.get(6));
        dbHper.eldInsert_m_re(newRow_elder);

        //重新載入SQLite
        findeldSet_re = dbHper.findeldSet_re();
        findrecSet_re = dbHper.findrecSet_re(eldID);


        if (findeldSet_re.size() > 0) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sl000.setRefreshing(false);

                    rl000.setVisibility(View.VISIBLE);
                    aniswitcher = 1;
                    rl000.setAnimation(AnimationUtils.loadAnimation(RecordEdit.this, R.anim.recordedit_move_in_from_bottom));
                    sv010.setAnimation(AnimationUtils.loadAnimation(RecordEdit.this, R.anim.recordedit_move_in_from_bottom));
//                        一定要在 spinner 設定監聽之前 動畫 才會正常

                    int p = sppage.getSelectedItemPosition() + 1;
                    setupPage();
                    switch (updateswitcher) {
                        case -1:// 刪除
                            if (findrecSet_re.size() >= p) {
                                sppage.setSelection(Math.max(p - 2, 0), true);
                            } else {
                                sppage.setSelection(Math.max(findrecSet_re.size() - 1, 0), true);
                            }

                            Toast.makeText(getApplicationContext(), getString(R.string.recordedit_deleted), Toast.LENGTH_SHORT).show();
                            break;
                        case 0:// Read
                            if (findrecSet_re.size() >= p) {
                                sppage.setSelection(p - 1, true);// 一定要加 true 才能肯定執行監聽！
                            } else {
                                sppage.setSelection(Math.max(findrecSet_re.size() - 1, 0), true);
                            }
                            break;
                        case 1:// 新增
                            sppage.setSelection(0, true);

                            Toast.makeText(getApplicationContext(), getString(R.string.recordedit_updated), Toast.LENGTH_SHORT).show();
                            menu.setGroupVisible(R.id.recordedit_iwrite, false);
                            menu.setGroupVisible(R.id.recordedit_iread, true);
                            break;
                        case 2:// 編輯
                            if (findrecSet_re.size() >= p) {
                                sppage.setSelection(p - 1, true);
                            } else {
                                sppage.setSelection(Math.max(findrecSet_re.size() - 1, 0), true);
                            }

                            Toast.makeText(getApplicationContext(), getString(R.string.recordedit_updated), Toast.LENGTH_SHORT).show();
                            menu.setGroupVisible(R.id.recordedit_iwrite, false);
                            menu.setGroupVisible(R.id.recordedit_iread, true);
                            break;
                    }
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sl000.setRefreshing(false);

                    chknetswitcher = 1;

                    AlertDialog.Builder adnonet = new AlertDialog.Builder(RecordEdit.this, R.style.recordedit_AlertDialog);
                    adnonet.setCancelable(false);
                    adnonet.setIcon(android.R.drawable.ic_dialog_alert);
                    adnonet.setTitle(getString(R.string.recordedit_chknettitle));
                    adnonet.setMessage(getString(R.string.recordedit_chknetmessage));

                    adnonet.setPositiveButton(R.string.recordedit_revert, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            menu.performIdentifierAction(R.id.action_settings, 0);
                        }
                    });

                    adnonet.show();
                }
            });
        }
    }// 這是一定要放在 Thread 裡面的方法！！

    private void dbMySQL_Refresh() {
        sl000.setRefreshing(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dbMySQL_Refresh_IN_THREAD();
            }
        }).start();
    }

    // 讀取MySQL elder資料
    private void dbMySQL_R_ELD() {
        //        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        String sqlctl = "SELECT DISTINCT " + DB_COLUMN_ELDs + " FROM " + DB_TABLE_ELD +
                " WHERE " + DB_COLUMN_ELD_eldID + " = " + eldID + "";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Record_DBConnector.executeQuery_re(nameValuePairs);

            /*
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             */
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料

//                int eldrowsAffected = dbHper.eldClear17();                 // 匯入前,刪除所有SQLite資料

                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
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
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite---------------------------------------
//                    long rowID_ELD = dbHper.eldInsert17_m(newRow);
                    dbHper.eldInsert_m_re(newRow);
//                    Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            }// else {
//                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_SHORT).show();
//            }
            findeldSet_re = dbHper.findeldSet_re();  //重新載入SQLite
            // --------------------------------------------------------
        } catch (Exception e) {
//            Log.d(TAG, e.toString());
        }
    }

    // 讀取MySQL record資料
    private void dbMySQL_R_REC() {
        dbHper.recClear_re();//先清空record SQLite

        String sqlctl = "SELECT DISTINCT " + DB_COLUMN_RECs + " FROM " + DB_TABLE_REC +
                " ORDER BY " + DB_COLUMN_REC_recID + " DESC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Record_DBConnector.executeQuery_re(nameValuePairs);

            /*
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             */
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料

//                int recrowsAffected = dbHper.recClear17();                 // 匯入前,刪除所有SQLite資料

                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
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
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite---------------------------------------
//                    long rowID_REC = dbHper.recInsert17_m(newRow);
                    dbHper.recInsert_m_re(newRow);
//                    Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            }// else {
//                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_SHORT).show();
//            }
            findrecSet_re = dbHper.findrecSet_re();  //重新載入SQLite
            // --------------------------------------------------------
        } catch (Exception e) {
//            Log.d(TAG, e.toString());
        }
    }

    private void dbMySQL_C() {
        String strename = ename.getText().toString().trim();
        String strspsex = spsex.getSelectedItem().toString();
        String strspbloodtype = spbloodtype.getSelectedItem().toString();
        String strtbirth = tbirth.getText().toString();

//      String elderID
        String streheight = eheight.getText().toString().trim();
        String streweight = eweight.getText().toString().trim();
        String stretemperature = etemperature.getText().toString().trim();
        String stresystolicbloodp = esystolicbloodp.getText().toString().trim();//收縮壓
        String strediastolicbloodp = ediastolicbloodp.getText().toString().trim();//舒張壓
        String strebeforebloods = ebeforebloods.getText().toString().trim();
        String streafterbloods = eafterbloods.getText().toString().trim();
        String streeat = eeat.getText().toString().trim();
        String streturn = eturn.getText().toString().trim();
        String strepee = epee.getText().toString().trim();//小便
        String strepoo = epoo.getText().toString().trim();//大便
        String strtbuilder = tbuilder.getText().toString();
        String strtbuilddate = tbuilddate.getText().toString();
        String strteditor;
        String strteditdate;
//      String grpID

        if (spsex.getSelectedItemPosition() == 0) {
            strspsex = "";
        }
        if (spbloodtype.getSelectedItemPosition() == 0) {
            strspbloodtype = "";
        }

        setupSDFdate();
        if (strtbuilddate.equals("")) {
            strtbuilddate = sdfnow;
        }
        strteditdate = sdfnow;

        if (strtbuilder.equals("")) {
            if (user == 1) {
                strtbuilder = account.getDisplayName();
                strteditor = account.getDisplayName();
            } else {
                strtbuilder = getString(R.string.recordedit_guest);
                strteditor = getString(R.string.recordedit_guest);
            }
        } else {
            if (user == 1) {
                strteditor = account.getDisplayName();
            } else {
                strteditor = getString(R.string.recordedit_guest);
            }
        }

        final ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(eldID);

        nameValuePairs.add(strename);
        nameValuePairs.add(strspsex);
        nameValuePairs.add(strspbloodtype);
        nameValuePairs.add(strtbirth);

        nameValuePairs.add("0");
        nameValuePairs.add(eldID);
        nameValuePairs.add(streheight);
        nameValuePairs.add(streweight);
        nameValuePairs.add(stretemperature);
        nameValuePairs.add(stresystolicbloodp);
        nameValuePairs.add(strediastolicbloodp);
        nameValuePairs.add(strebeforebloods);
        nameValuePairs.add(streafterbloods);
        nameValuePairs.add(streeat);
        nameValuePairs.add(streturn);
        nameValuePairs.add(strepee);
        nameValuePairs.add(strepoo);
        nameValuePairs.add(strtbuilder);
        nameValuePairs.add(strtbuilddate);
        nameValuePairs.add(strteditor);
        nameValuePairs.add(strteditdate);
        nameValuePairs.add(grpID);

        sl000.setRefreshing(true);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                //------
//        String result = DBConnector17.executeInsert(nameValuePairs);//真正執行新增 mysql
                if (account != null) {
                    Record_DBConnector.executeInsert_re(nameValuePairs);
                    dbMySQL_R_REC();
                }
                if(account == null ){
                    dbHper.recInsert_m_re(nameValuePairs);
                }

                dbMySQL_Refresh_IN_THREAD();
            }
        }).start();
        //------
    }

    private void dbMySQL_U() {
        String strename = ename.getText().toString().trim();
        String strspsex = spsex.getSelectedItem().toString();
        String strspbloodtype = spbloodtype.getSelectedItem().toString();
        String strtbirth = tbirth.getText().toString();

//      String elderID
        String streheight = eheight.getText().toString().trim();
        String streweight = eweight.getText().toString().trim();
        String stretemperature = etemperature.getText().toString().trim();
        String stresystolicbloodp = esystolicbloodp.getText().toString().trim();//收縮壓
        String strediastolicbloodp = ediastolicbloodp.getText().toString().trim();//舒張壓
        String strebeforebloods = ebeforebloods.getText().toString().trim();
        String streafterbloods = eafterbloods.getText().toString().trim();
        String streeat = eeat.getText().toString().trim();
        String streturn = eturn.getText().toString().trim();
        String strepee = epee.getText().toString().trim();//小便
        String strepoo = epoo.getText().toString().trim();//大便
        String strtbuilder = tbuilder.getText().toString();
        String strtbuilddate = tbuilddate.getText().toString();
        String strteditor;
        String strteditdate;
//      String grpID

        String[] aryids = tvalue.getContentDescription().toString().split("#");

        if (spsex.getSelectedItemPosition() == 0) {
            strspsex = "";
        }
        if (spbloodtype.getSelectedItemPosition() == 0) {
            strspbloodtype = "";
        }

        setupSDFdate();
        if (strtbuilddate.equals("")) {
            strtbuilddate = sdfnow;
        }
        strteditdate = sdfnow;

        if (strtbuilder.equals("")) {
            if (user == 1) {
                strtbuilder = account.getDisplayName();
                strteditor = account.getDisplayName();
            } else {
                strtbuilder = getString(R.string.recordedit_guest);
                strteditor = getString(R.string.recordedit_guest);
            }
        } else {
            if (user == 1) {
                strteditor = account.getDisplayName();
            } else {
                strteditor = getString(R.string.recordedit_guest);
            }
        }

        final ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(eldID);

        nameValuePairs.add(strename);
        nameValuePairs.add(strspsex);
        nameValuePairs.add(strspbloodtype);
        nameValuePairs.add(strtbirth);

        nameValuePairs.add(aryids[1]);
        nameValuePairs.add(eldID);
        nameValuePairs.add(streheight);
        nameValuePairs.add(streweight);
        nameValuePairs.add(stretemperature);
        nameValuePairs.add(stresystolicbloodp);
        nameValuePairs.add(strediastolicbloodp);
        nameValuePairs.add(strebeforebloods);
        nameValuePairs.add(streafterbloods);
        nameValuePairs.add(streeat);
        nameValuePairs.add(streturn);
        nameValuePairs.add(strepee);
        nameValuePairs.add(strepoo);
        nameValuePairs.add(strtbuilder);
        nameValuePairs.add(strtbuilddate);
        nameValuePairs.add(strteditor);
        nameValuePairs.add(strteditdate);
        nameValuePairs.add(grpID);

        sl000.setRefreshing(true);

        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
//-----------------------------------------------
//        String result = DBConnector17.executeUpdate(nameValuePairs);
                if(account != null) {
                    Record_DBConnector.executeUpdate_re(nameValuePairs);
                    dbMySQL_R_REC();
                }
                if(account == null){
                    dbHper.recUpdate_m_re(nameValuePairs);
                }
                dbMySQL_Refresh_IN_THREAD();
            }
        }).start();
//-----------------------------------------------
    }

    private void dbMySQL_D() {
        updateswitcher = -1;

        String[] aryids = tvalue.getContentDescription().toString().split("#");
        final ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(aryids[1]);

        sl000.setRefreshing(true);

        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
//-----------------------------------------------
/*        String result = DBConnector17.executeDelete(nameValuePairs);
        Log.d(TAG, "Delete result:" + result);*/
                if (account != null) {
                    Record_DBConnector.executeDelete_re(nameValuePairs);
                }
                dbHper.deleteRecordRec(Integer.valueOf(aryids[1]));
                dbMySQL_Refresh_IN_THREAD();
//-----------------------------------------------
            }
        }).start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int p = sppage.getSelectedItemPosition() + 1;
        switch (v.getId()) {
            case R.id.recordedit_tnextpage:
                if (findrecSet_re.size() > 0) {
                    if (findrecSet_re.size() > 1) {
                        aniswitcher = 1;
                        sv010.setAnimation(AnimationUtils.loadAnimation(this, R.anim.recordedit_move_in_from_right));

                        if (p == findrecSet_re.size()) {
                            sppage.setSelection(0, true);
                        } else {
                            sppage.setSelection(p, true);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nomoredata), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nodata), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recordedit_tpreviouspage:
                if (findrecSet_re.size() > 0) {
                    if (findrecSet_re.size() > 1) {
                        aniswitcher = 1;
                        sv010.setAnimation(AnimationUtils.loadAnimation(this, R.anim.recordedit_move_in_from_left));

                        if (p == 1) {
                            sppage.setSelection(findrecSet_re.size() - 1, true);
                        } else {
                            sppage.setSelection(p - 2, true);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nomoredata), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nodata), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recordedit_tvalue:
                AlertDialog.Builder advalue = new AlertDialog.Builder(RecordEdit.this, R.style.recordedit_AlertDialog);
                advalue.setIcon(android.R.drawable.ic_dialog_info);

                advalue.setPositiveButton(R.string.recordedit_iknow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                switch (valueswitcher) {
                    case 1:
                        advalue.setTitle(getString(R.string.recordedit_bmivalue));
                        advalue.setMessage(getString(R.string.recordedit_bmivaluedata));
                        break;
                    case 0:
                        advalue.setTitle(getString(R.string.recordedit_bloodvalue));
                        advalue.setMessage(getString(R.string.recordedit_bloodvaluedata));
                        break;
                }
                advalue.show();
                break;
            case R.id.recordedit_tbirth:
                java.util.Calendar cbirth = java.util.Calendar.getInstance();

                DatePickerDialog datePicDlg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strtbirth = (year) + "-" + (month + 1) + "-" + (dayOfMonth);
                        tbirth.setText(strtbirth);

                        java.util.Calendar cnow = java.util.Calendar.getInstance();
                        int yearnow = cnow.get(java.util.Calendar.YEAR);
                        int monthnow = cnow.get(java.util.Calendar.MONTH);
                        int today = cnow.get(java.util.Calendar.DAY_OF_MONTH);
                        int age = yearnow - year;
                        int diffmonth = monthnow - month;
                        int diffdayofmonth = today - dayOfMonth;

                        if (age == 0) {
                            if (diffmonth == 0) {
                                if (diffdayofmonth >= 0) {
                                    tage.setText(getString(R.string.recordedit_agedd));
                                } else {
                                    tbirth.setHintTextColor(getColor(R.color.Red));
                                    tbirth.setText(null);
                                    tbirth.setHint(getString(R.string.recordedit_reinput));
                                    tagetitle.setVisibility(View.GONE);
                                    tage.setVisibility(View.GONE);
                                }
                            } else if (diffmonth > 0) {
                                tage.setText(diffmonth + getString(R.string.recordedit_agemm));
                            } else {
                                tbirth.setHintTextColor(getColor(R.color.Red));
                                tbirth.setText(null);
                                tbirth.setHint(getString(R.string.recordedit_reinput));
                                tagetitle.setVisibility(View.GONE);
                                tage.setVisibility(View.GONE);
                            }
                        } else if (age > 0) {
                            tage.setText(age + getString(R.string.recordedit_ageyy));
                        } else {
                            tbirth.setHintTextColor(getColor(R.color.Red));
                            tbirth.setText(null);
                            tbirth.setHint(getString(R.string.recordedit_reinput));
                            tagetitle.setVisibility(View.GONE);
                            tage.setVisibility(View.GONE);
                        }
                    }
                },
                        cbirth.get(java.util.Calendar.YEAR),
                        cbirth.get(java.util.Calendar.MONTH),
                        cbirth.get(java.util.Calendar.DAY_OF_MONTH));

                datePicDlg.setTitle(getString(R.string.recordedit_birthtitle));
                datePicDlg.setMessage(getString(R.string.recordedit_birthmessage));
                datePicDlg.setIcon(android.R.drawable.ic_dialog_info);
                datePicDlg.setCancelable(false);
                datePicDlg.show();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onLongClick(View v) {
        int p = sppage.getSelectedItemPosition() + 1;
        switch (v.getId()) {
            case R.id.recordedit_tnextpage:
                if (findrecSet_re.size() > 1) {
                    if (p - 1 != Math.max(findrecSet_re.size() - 1, 0)) {
                        sppage.setSelection(Math.max(findrecSet_re.size() - 1, 0), true);
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_lastpage), Toast.LENGTH_SHORT).show();
                } else if (findrecSet_re.size() == 1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nomoredata), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nodata), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recordedit_tpreviouspage:
                if (findrecSet_re.size() > 1) {
                    if (p - 1 != 0) {
                        sppage.setSelection(0, true);
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_firstpage), Toast.LENGTH_SHORT).show();
                } else if (findrecSet_re.size() == 1) {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nomoredata), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nodata), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(getApplicationContext(), getString(R.string.recordedit_onBackPressed), Toast.LENGTH_SHORT).show();
    }

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

    //----------------------------------------menu---------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recordedit_menu, menu);
        this.menu = menu;
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recordedit_iadd:
                updateswitcher = 1;
                setupClearData();
                setupWrite();
                break;
            case R.id.recordedit_iedit:
                updateswitcher = 2;
                if (editswitcher == 1) {
                    setupWrite();
                } else {
                    menu.performIdentifierAction(R.id.recordedit_iadd, 0);
                }
                break;
            case R.id.recordedit_idelete:
                if (findrecSet_re.size() > 0) {
                    AlertDialog.Builder addelete = new AlertDialog.Builder(RecordEdit.this, R.style.recordedit_AlertDialog);
                    addelete.setTitle(R.string.recordedit_delete);
                    addelete.setMessage(R.string.recordedit_deletemessage);
                    addelete.setIcon(android.R.drawable.ic_delete);

                    addelete.setPositiveButton(R.string.recordedit_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbMySQL_D();
                        }
                    });

                    addelete.setNegativeButton(R.string.recordedit_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    addelete.show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_nodata), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.recordedit_iupdate:
                String strename = ename.getText().toString().trim();

                if (strename.length() > 0) {
                    switch (updateswitcher) {
                        case 1:
                            if( noFastClick() ){
                                dbMySQL_C();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), getString(R.string.toofast), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            if( noFastClick() ){
                                dbMySQL_U();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), getString(R.string.toofast), Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.recordedit_updatetoast), Toast.LENGTH_SHORT).show();
                    ename.setHintTextColor(getColor(R.color.Red));
                    ename.setHint(getString(R.string.recordedit_updatehint));
                }
                break;
            case R.id.recordedit_icancel:
                dbSQLite_R();
                menu.setGroupVisible(R.id.recordedit_iwrite, false);
                menu.setGroupVisible(R.id.recordedit_iread, true);
                break;
            case R.id.action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }

        if (chknetswitcher == 1) {
            this.finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        this.finish();
    }
}