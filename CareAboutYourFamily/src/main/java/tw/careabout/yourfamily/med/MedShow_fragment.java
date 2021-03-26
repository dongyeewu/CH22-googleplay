package tw.careabout.yourfamily.med;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tw.careabout.yourfamily.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;

import tw.careabout.yourfamily.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


public class MedShow_fragment extends Fragment implements MedShow_CardAdapter.OnCardListen {

    private RecyclerView recycleview;
    private List<MedShow_Card> cardList = new ArrayList<>();
    private MedShow_CardAdapter mcAdapter;
    private CAYFDbHelper dbHper;
    private static final String DB_FILE = "CAYF.db";
    private static final String DB_TABLE = "mededit";
    private static final int DBversion = 1;
    private TextView noNotiText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medshow_fragment, container, false);

        recycleview =view.findViewById(R.id.medshow_recyclerview);
        noNotiText = (TextView)view.findViewById(R.id.noNotiText);

        view.findViewById(R.id.medshow_b001).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MedEdit.class));
            }
        });

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        return view;
    }

    private void initDB() {
        if (dbHper == null) {
            dbHper = new CAYFDbHelper(getContext(), DB_FILE, null, DBversion);
            //起始資料庫為空，啟用FriendDbHelper建資料庫
            dbHper.createTable(); //新增此行，呼叫方法建立資料表
        }
    }

    private void initData() {
        cardList.clear();
        ArrayList<String> recSet = dbHper.setRec();

        if( recSet.size()==0 ){
            noNotiText.setVisibility(View.VISIBLE);
        }else{
            noNotiText.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            cardList.add(new MedShow_Card(fld[0], fld[1], fld[2]));
        }

        mcAdapter = new MedShow_CardAdapter(cardList,this);
        recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleview.setAdapter(mcAdapter);

    }

    @Override
    public void OnCardClick(final int position, View v) {
        final MedShow_Card card = cardList.get(position);
        switch (v.getId()){
            case R.id.medshow_edit:
                Intent intent = new Intent(getActivity(), MedEdit.class);
                Bundle bundle =new Bundle();
                bundle.putString("id",card.getMed001());
                bundle.putString("content",card.getMed002());
                bundle.putString("time",card.getMed003());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.medshow_del:
                final MyAlertDialog alertDialog = new MyAlertDialog(getActivity());
                alertDialog.setCancelable(false);
                alertDialog.setTitle(getString(R.string.medshow_diaTitle));
                alertDialog.setMessage(getString(R.string.medshow_diaMsg));
                alertDialog.setIcon(android.R.drawable.ic_delete);
                alertDialog.setButton(BUTTON_POSITIVE, getString(R.string.mededit_diaBtnOK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHper.delRec(card.getMed001());
                        CancelAlarm(Integer.parseInt(card.getMed001()));
                        onResume();
                        alertDialog.cancel();
                    }
                });
                alertDialog.setButton(BUTTON_NEGATIVE, getString(R.string.mededit_diaBtnCxl), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });

//                //dialog背景為無填滿
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                break;
        }
    }

    private void CancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),id,intent,PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    //關掉app時，關掉dbHPer
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
        initDB();
        initData();
    }
}