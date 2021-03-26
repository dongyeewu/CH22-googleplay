package tw.careabout.yourfamily.info;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import tw.careabout.yourfamily.MyAlertDialog;
import tw.careabout.yourfamily.R;

import static java.lang.Thread.sleep;

public class InfoPageFragment extends Fragment implements InfoCardAdapter.OnCardListen {

    private final int index;
    private Spinner sp01, sp02;
    private RecyclerView recycleview;
    private TextView nodata;
    private DbHelper dbHelper;
    private Handler mHandler = new Handler();
    private final List<InfoCard> cardList = new ArrayList<>();


    private String DB_NAME = "cayf_info.db";
    private int DB_VERSION = 1;

    private static final String[] wTABLE = {"welfare01", "welfare02", "welfare03", "welfare04", "welfare05"};
    private static final String[] iTABLE =
            {"institution01", "institution02", "institution03", "institution04", "institution05", "institution06"};
    private static final String[] fTABLE =
            {"facilities01", "facilities02", "facilities03", "facilities04", "facilities05", "facilities06", "facilities07"};


    public InfoPageFragment(int position) {
        index = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_page, container, false);
        DisplayMetrics metric = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        sp01 = v.findViewById(R.id.info_sp1);
        sp02 = v.findViewById(R.id.info_sp2);
        recycleview = v.findViewById(R.id.recycleview);
        nodata = v.findViewById(R.id.nodata);

        nodata.setVisibility(getView().GONE);
        sp01.setDropDownWidth(metric.widthPixels);
        sp02.setDropDownWidth(metric.widthPixels);
        initDB();
        setsp01();
        return v;
    }

    private void setsp01() {
        ArrayList<String> list = new ArrayList<>();

        switch (index) {
            case 1:
                Collections.addAll(list, getResources().getStringArray(R.array.info_welfare));
                break;
            case 2:
                Collections.addAll(list, getResources().getStringArray(R.array.info_institution));
                break;
            case 3:
                Collections.addAll(list, getResources().getStringArray(R.array.info_care_facilities));
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        sp01.setAdapter(adapter);
        sp01.setOnItemSelectedListener(setsp02);

    }

    private void initDB() {
        if (dbHelper == null) {
            dbHelper = new DbHelper(getContext(), DB_NAME, null, DB_VERSION);
        }

        switch (index) {
            case 1:
                if (dbHelper.getInfoDistrict(wTABLE[wTABLE.length-1]).size() == 0) {
                    sqltosqlite(wTABLE);
                }
                break;
            case 2:
                if (dbHelper.getInfoDistrict(iTABLE[iTABLE.length-1]).size() == 0) {
                    sqltosqlite(iTABLE);
                }
                break;
            case 3:
                if (dbHelper.getInfoDistrict(fTABLE[fTABLE.length-1]).size() == 0) {
                    sqltosqlite(fTABLE);
                }
                break;
        }
    }

    private void sqltosqlite(final String[] TABLE) {
        final ProgressDialog progDlg = new ProgressDialog(getContext());
        progDlg.setTitle(getString(R.string.info_wait));
        progDlg.setMessage(getString(R.string.info_load));
        progDlg.setIcon(android.R.drawable.presence_away);
        progDlg.setCancelable(false);
        progDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progDlg.setMax(100);
        progDlg.show();
        new Thread(new Runnable() {
            private int whatmsg;
            @Override
            public void run() {
                dbHelper.chkAllTableExists();
                for (final String s : TABLE) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String sql = "SELECT * FROM " + s + " ORDER BY ifo001 ASC";
                    try {
                        String result = DBConnector.executeQuery(sql);
                        if (DBConnector.httpstate == 200) {
                            JSONArray jsonArray = new JSONArray(result);
                            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                                progDlg.setMax(jsonArray.length());
                                // 匯入前,刪除所有SQLite資料
                                dbHelper.clearRec(s);
                                // 處理JASON 傳回來的每筆資料
                                ContentValues newRow = new ContentValues();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    Log.i("cayf=>",Integer.toString(i));

                                    JSONObject jsonData = jsonArray.getJSONObject(i);
//                                    ContentValues newRow = new ContentValues();
                                    // 自動取的欄位 --取出 jsonObject 每個欄位("key","value")
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
                                        newRow.put(key, value); // 動態找出有幾個欄位
                                        // -------------------------------------------------------------------
                                    }
                                    onResume();
                                    final long rowID = dbHelper.insertRec_mysql(newRow, s);
                                    dbHelper.close();


                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progDlg.setProgress((int) rowID);
                                        }
                                    });
                                }
                            }
                            whatmsg=1; // 下載完成後發送處理消息
                        } else {
                            whatmsg=0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(whatmsg);
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
                    final MyAlertDialog myAlertDialog = new MyAlertDialog(getContext());
                    myAlertDialog.setTitle("連線失敗請確認網路是否開啟!");
                    myAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.info_dia_positive),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myAlertDialog.cancel();
                                }
                            });
                    myAlertDialog.setCancelable(false);
                    myAlertDialog.show();

                    break;
                case 1:
                    setsp01();
                    break;
            }
        }
    };

    private final AdapterView.OnItemSelectedListener setsp02 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String[][] table = new String[][]{wTABLE, iTABLE, fTABLE};
            ArrayList<String> recDist = dbHelper.getInfoDistrict(table[index - 1][position]);
            Set set = new TreeSet<String>();
            for (String s : recDist) {
                String[] b_ary = s.split("\\|");
                Collections.addAll(set, b_ary);
            }

            ArrayList<String> list = new ArrayList<>();
            Collections.addAll(list, getResources().getStringArray(R.array.info_district));

            for (int i = 0; i < list.size(); i++) {
                int cunt = 0;
                for (Object s1 : set) {
                    if (s1.toString().equals(list.get(i)) || list.get(i).equals("全區")) {
                        cunt++;
                    }
                }
                if (cunt == 0) {
                    list.remove(i);
                    i--;
                }
            }

//            Collator collator = Collator.getInstance(Locale.TAIWAN);
//            Comparator comparator = new ChineseComparator();
//            Collections.sort(recDist,comparator);

//            recDist.clear();
//            recDist.addAll(set);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
            sp02.setAdapter(adapter);
            sp02.setOnItemSelectedListener(getdata);


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener getdata = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cardList.clear();

            switch (index) {
                case 1:
                    switch (sp01.getSelectedItemPosition()) {
                        case 0:
                            initData(wTABLE[0]);
                            break;
                        case 1:
                            initData(wTABLE[1]);
                            break;
                        case 2:
                            initData(wTABLE[2]);
                            break;
                        case 3:
                            initData(wTABLE[3]);
                            break;
                        case 4:
                            initData(wTABLE[4]);
                            break;
                    }
                    break;
                case 2:
                    switch (sp01.getSelectedItemPosition()) {
                        case 0:
                            initData(iTABLE[0]);
                            break;
                        case 1:
                            initData(iTABLE[1]);
                            break;
                        case 2:
                            initData(iTABLE[2]);
                            break;
                        case 3:
                            initData(iTABLE[3]);
                            break;
                        case 4:
                            initData(iTABLE[4]);
                            break;
                        case 5:
                            initData(iTABLE[5]);
                            break;
                    }
                    break;
                case 3:
                    switch (sp01.getSelectedItemPosition()) {
                        case 0:
                            initData(fTABLE[0]);
                            break;
                        case 1:
                            initData(fTABLE[1]);
                            break;
                        case 2:
                            initData(fTABLE[2]);
                            break;
                        case 3:
                            initData(fTABLE[3]);
                            break;
                        case 4:
                            initData(fTABLE[4]);
                            break;
                        case 5:
                            initData(fTABLE[5]);
                            break;
                        case 6:
                            initData(fTABLE[6]);
                            break;
                    }

                    break;
            }
            setRecycleView();
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void initData(String tableName) {

        ArrayList<String> recSet = dbHelper.getInfoRec(tableName);

        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            String newfld5 = fld[5].replace("|", "\n");

            cardList.add(new InfoCard(fld[1], fld[3], fld[2], fld[4], newfld5, fld[7]));
        }
    }

    private void setRecycleView() {

        String ara = sp02.getSelectedItem().toString();
        if (!ara.equals(getResources().getStringArray(R.array.info_district)[0]) && !cardList.isEmpty()) {
            for (int i = 0; i < cardList.size(); i++) {
                try {
                    if (!cardList.get(i).getT100().contains(ara) &&
                            cardList.get(i).getT100().indexOf("全區") == -1) {
                        cardList.remove(i);
                        i--;
                    }
                } catch (Exception ignored) {

                }
            }
        }

        //如果沒有條件 顯示無符合項目
        if (cardList.isEmpty()) {
            nodata.setVisibility(getView().VISIBLE);
        } else {
            nodata.setVisibility(getView().GONE);
        }

        InfoCardAdapter cardAdapter = new InfoCardAdapter(cardList, this);
        recycleview.setAdapter(cardAdapter);
        recycleview.setHasFixedSize(true);
    }

    @Override
    public void OnCardClick(int position, View v) {
        InfoCard card = cardList.get(position);
        switch (v.getId()) {
            //播打電話
            case R.id.t001:
                String num = card.getT001();
                num = num.replace("-", "");
                num = num.replace("(", "");
                num = num.replace(")", "");
                String phonecall = "tel:" + num.substring(0, 10);
                getActivity().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(phonecall)));
                break;
            //開啟地圖
            case R.id.t002:
                String adr = card.getT002();
                if (adr.substring(1, 2).equals("中")) {
                    String adra = adr.substring(0, adr.indexOf("號") + 1);
                    String u = "https://maps.google.com/maps?f=d&saddr=24.170709472723136, 120.61082037203339" +
                            "&daddr=" + adra + "&hl=tw";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(u)));
                }
                break;
            //開啟網址
            case R.id.t003:
                String url = card.getT003();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (dbHelper == null) {
            dbHelper = new DbHelper(getContext(), DB_NAME, null, DB_VERSION);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
        mHandler.removeCallbacks(null);
    }


    private class ChineseComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Collator collator = Collator.getInstance(Locale.TAIWAN);

            return collator.compare(o1.toString(), o2.toString());
        }
    }


}