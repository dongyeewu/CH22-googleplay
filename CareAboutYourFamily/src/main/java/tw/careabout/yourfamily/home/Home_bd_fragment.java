package tw.careabout.yourfamily.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Map;


import tw.careabout.yourfamily.Family;
import tw.careabout.yourfamily.R;
import tw.careabout.yourfamily.board.BoardDBHper;

public class Home_bd_fragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //SQLite資料庫宣告
    private String DB_Name = "CAYF.db";
    private int DBversion = 1;
    private HomeDbHelper home_dpHper;
    private BoardDBHper boardDBHper;
    private GoogleSignInAccount account;

    private ArrayList<String> userRowData = new ArrayList<>();
    private TextView post_title, post_creater, post_createTime, post_context, noFamily_btn;
    private String title, creater, createTime, context;
    private LinearLayout guest_layout, noFamily_layout, noPost_layout, post_layout;
    private RelativeLayout bd_relativeLayout;
    private ScrollView bd_scollView;


    public Home_bd_fragment() {
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
        View view = inflater.inflate(R.layout.home_bd_fragment, container, false);
        initDB();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());
        setViewComponent(view);

        if(account==null){
            setGuestView(view);
        }
        if(account!=null){
            userRowData = home_dpHper.getUser();
            if( Integer.valueOf(userRowData.get(6)) ==0 ){
                    setNoFamilyView(view);
            }
            if( Integer.valueOf(userRowData.get(6)) !=0 ){
                    setBoardView(view);
            }
        }

        return view;
    }

    private void setViewComponent(View view) {
        post_title = (TextView)view.findViewById(R.id.home_bd_fragment_title);
        post_creater = (TextView)view.findViewById(R.id.home_bd_fragment_creater);
        post_createTime = (TextView)view.findViewById(R.id.home_bd_fragment_createTime);
        post_context = (TextView)view.findViewById(R.id.home_bd_fragment_context);
        post_context.setMovementMethod(ScrollingMovementMethod.getInstance());

        guest_layout = (LinearLayout)view.findViewById(R.id.guest_layout);

        noFamily_layout = (LinearLayout)view.findViewById(R.id.noFamily_layout);
        noFamily_btn = (TextView)view.findViewById(R.id.noFamily_btn);
        noFamily_btn.setOnClickListener(goFamily);

        noPost_layout = (LinearLayout)view.findViewById(R.id.noPost_layout);

        post_layout = (LinearLayout)view.findViewById(R.id.post_layout);

    }

    private void initDB() {
        if (home_dpHper == null) {
            home_dpHper = new HomeDbHelper(getActivity(), DB_Name, null, DBversion);
            home_dpHper.createHomeTable();
        }
        if (boardDBHper == null) {
            boardDBHper = new BoardDBHper(getActivity(), DB_Name, null, DBversion);
            boardDBHper.createBoardTable();
        }
    }

    private void setGuestView(View view) {
        guest_layout.setVisibility(View.VISIBLE);
        noFamily_layout.setVisibility(View.INVISIBLE);
        noPost_layout.setVisibility(View.INVISIBLE);
        post_layout.setVisibility(View.INVISIBLE);
    }

    private void setNoFamilyView(View view) {
        guest_layout.setVisibility(View.INVISIBLE);
        noFamily_layout.setVisibility(View.VISIBLE);
        noPost_layout.setVisibility(View.INVISIBLE);
        post_layout.setVisibility(View.INVISIBLE);
    }

    private void setBoardView(View view) {
        guest_layout.setVisibility(View.INVISIBLE);
        noFamily_layout.setVisibility(View.INVISIBLE);
        noPost_layout.setVisibility(View.INVISIBLE);
        post_layout.setVisibility(View.VISIBLE);
        setPost();
    }

    private void setPost() {
        ArrayList<Map<String,Object>> boardRec = new ArrayList<>();
        boardRec = boardDBHper.findAllRec();
        if( boardRec.size()==0 ){
            guest_layout.setVisibility(View.INVISIBLE);
            noFamily_layout.setVisibility(View.INVISIBLE);
            noPost_layout.setVisibility(View.VISIBLE);
            post_layout.setVisibility(View.INVISIBLE);
        }
        if( boardRec.size()!=0 ){
            title = String.valueOf(boardRec.get(0).get("boa004"));
            creater = String.valueOf(boardRec.get(0).get("boa002"));
            if( String.valueOf(boardRec.get(0).get("boa006")).equals("null") ){
                createTime = String.valueOf(boardRec.get(0).get("boa003"));
            }
            if( !String.valueOf(boardRec.get(0).get("boa006")).equals("null") ){
                createTime = String.valueOf(boardRec.get(0).get("boa006"));
            }
            context = String.valueOf(boardRec.get(0).get("boa005"));

            post_title.setText(getString(R.string.boardShow_title)+title);
            post_creater.setText(getString(R.string.boardShow_creater)+creater);
            post_createTime.setText(getString(R.string.boardShow_createTime)+"\n"+createTime);
            post_context.setText(context);
        }
    }

    private View.OnClickListener goFamily = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), Family.class);
            startActivity(intent);
        }
    };


}