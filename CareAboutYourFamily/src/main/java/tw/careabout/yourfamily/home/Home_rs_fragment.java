package tw.careabout.yourfamily.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import tw.careabout.yourfamily.Elder_editDbHelper;
import tw.careabout.yourfamily.Family;
import tw.careabout.yourfamily.R;
import tw.careabout.yourfamily.record.Record_DbHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;



public class Home_rs_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //SQLite資料庫宣告
    private String DB_Name = "";
    private int DBversion = 1;
    private HomeDbHelper home_dpHper, G_home_dpHper, U_home_dpHper;
    private Record_DbHelper record_dbHelper, G_record_dbHelper, U_record_dbHelper;
    private Elder_editDbHelper elder_dbHper, G_elder_dbHper, U_elder_dbHper;
    private GoogleSignInAccount account;

    private ArrayList<String> userRowData = new ArrayList<>();
    private ArrayList<String> elderData = new ArrayList<>();
    private LinearLayout record_layout, noFamily_layout, noRecord_layout;
    private TextView recordCreater, recordBuildDate, elderName, elderSex, editter, editTime, noFamily_btn;
    private TextView t001,t002,t003,t004,t005,t006,t007,t008,t009,t010,t011;
    private ArrayList<String> recordData = new ArrayList<>();



    public Home_rs_fragment() {
        // Required empty public constructor
    }


    public static Home_rs_fragment newInstance(String param1, String param2) {
        Home_rs_fragment fragment = new Home_rs_fragment();
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
        View view = inflater.inflate(R.layout.home_rs_fragment, container, false);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        initDB();
        setViewComponent(view);

        if(account==null){
            setRecordView(view);
        }
        if(account!=null){
            userRowData = home_dpHper.getUser();
            if( Integer.valueOf(userRowData.get(6)) ==0 ){
                setNoFamilyView(view);
            }
            if( Integer.valueOf(userRowData.get(6)) !=0 ){
                setRecordView(view);
            }
        }
        return view;
    }

    private void initDB() {
        if(account!=null){
            DB_Name = "CAYF.db";
            if (U_home_dpHper == null) {
                U_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                U_home_dpHper.createHomeTable();
            }
            if (U_record_dbHelper == null) {
                U_record_dbHelper = new Record_DbHelper(getActivity(), DB_Name, DBversion);
                U_record_dbHelper.createTable();
            }
            if (U_elder_dbHper == null) {
                U_elder_dbHper = new Elder_editDbHelper(getActivity(), DB_Name, null, DBversion);
                U_elder_dbHper.creatfile();
            }

            home_dpHper = U_home_dpHper;
            record_dbHelper = U_record_dbHelper;
            elder_dbHper = U_elder_dbHper;
        }
        if(account==null){
            DB_Name = "guest.db";
            if (G_home_dpHper == null) {
                G_home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
                G_home_dpHper.createHomeTable();
            }
            if (G_record_dbHelper == null) {
                G_record_dbHelper = new Record_DbHelper(getActivity(), DB_Name, DBversion);
                G_record_dbHelper.createTable();
            }
            if (G_elder_dbHper == null) {
                G_elder_dbHper = new Elder_editDbHelper(getActivity(), DB_Name, null, DBversion);
                G_elder_dbHper.creatfile();
            }

            home_dpHper = G_home_dpHper;
            record_dbHelper = G_record_dbHelper;
            elder_dbHper = G_elder_dbHper;
        }


    }

    private void setViewComponent(View view) {
        record_layout = (LinearLayout)view.findViewById(R.id.home_rs_recordlayout);
        recordCreater = (TextView)view.findViewById(R.id.home_rs_creater);
        recordBuildDate = (TextView)view.findViewById(R.id.home_rs_tbuilddate);
        elderName = (TextView)view.findViewById(R.id.home_rs_ename);
        elderSex = (TextView)view.findViewById(R.id.home_rs_sex);
        t001 = (TextView)view.findViewById(R.id.home_rs_t001);
        t002 = (TextView)view.findViewById(R.id.home_rs_t002);
        t003 = (TextView)view.findViewById(R.id.home_rs_t003);
        t004 = (TextView)view.findViewById(R.id.home_rs_t004);
        t005 = (TextView)view.findViewById(R.id.home_rs_t005);
        t006 = (TextView)view.findViewById(R.id.home_rs_t006);
        t007 = (TextView)view.findViewById(R.id.home_rs_t007);
        t008 = (TextView)view.findViewById(R.id.home_rs_t008);
        t009 = (TextView)view.findViewById(R.id.home_rs_t009);
        t010 = (TextView)view.findViewById(R.id.home_rs_t010);
        t011 = (TextView)view.findViewById(R.id.home_rs_t011);
        editter = (TextView)view.findViewById(R.id.home_rs_editter);
        editTime = (TextView)view.findViewById(R.id.home_rs_teditdate);

        noFamily_layout = (LinearLayout)view.findViewById(R.id.home_rs_noFamily_layout);
        noRecord_layout = (LinearLayout)view.findViewById(R.id.noRecord_layout);

    }

    private void setNoRecordView(View view) {
        record_layout.setVisibility(View.INVISIBLE);
        noFamily_layout.setVisibility(View.INVISIBLE);
        noRecord_layout.setVisibility(View.VISIBLE);
    }

    private void setNoFamilyView(View view) {
        record_layout.setVisibility(View.INVISIBLE);
        noFamily_layout.setVisibility(View.VISIBLE);
        noRecord_layout.setVisibility(View.INVISIBLE);

        noFamily_btn = (TextView)view.findViewById(R.id.home_rs_noFamily_btn);
        noFamily_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goFamily_it = new Intent();
                goFamily_it.setClass(getActivity(), Family.class);
                startActivity(goFamily_it);
            }
        });
    }

    private void setRecordView(View view) {
        initDB();
        record_layout.setVisibility(View.VISIBLE);
        noFamily_layout.setVisibility(View.INVISIBLE);
        noRecord_layout.setVisibility(View.INVISIBLE);

        recordData = record_dbHelper.findrecSet_re();


        if(recordData.size()==0){
            setNoRecordView(view);
        }
        if(recordData.size()!=0){
            String[] recordRow = recordData.get(0).split("#");
            elderData = elder_dbHper.selectElder(recordRow[1]);

            recordCreater.setText(recordRow[13]);
            recordBuildDate.setText(recordRow[14]);
            elderName.setText(elderData.get(1));
            elderSex.setText(elderData.get(3));
            t001.setText(recordRow[5]); //收縮壓
            t002.setText(recordRow[6]); //舒張壓
            t003.setText(recordRow[7]); //飯前血壓
            t004.setText(recordRow[8]); //飯後血壓
            t005.setText(recordRow[2]); //身高
            t006.setText(recordRow[3]); //體重
            t007.setText(recordRow[4]); //體溫
            t008.setText(recordRow[9]); //餵飯
            t009.setText(recordRow[10]); //翻身
            t010.setText(recordRow[11]); //小便
            t011.setText(recordRow[12]); //大便
            editter.setText(recordRow[15]); //最新編輯者
            editTime.setText(recordRow[16]); //編輯時間
            
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        initDB();
    }
}