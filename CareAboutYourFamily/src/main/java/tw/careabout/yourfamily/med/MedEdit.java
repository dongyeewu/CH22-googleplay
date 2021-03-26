package tw.careabout.yourfamily.med;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tw.careabout.yourfamily.R;


public class MedEdit extends AppCompatActivity implements View.OnClickListener {

    private TextView time;
    private CAYFDbHelper dbHper;
    private static final String DB_FILE = "CAYF.db";
    private static final String DB_TABLE = "mededit";
    private static final int DBversion = 1;
    private EditText e001;
    private Intent intent;
    private String id = "0";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mededit);
        initDB();
        setupViewConponent();
    }

    private void setupViewConponent() {
        time = findViewById(R.id.mededit_time);
        e001 = findViewById(R.id.mededit_e001);

        findViewById(R.id.mededit_timepicker).setOnClickListener(this);
        findViewById(R.id.mededit_save).setOnClickListener(this);

        intent = getIntent();

        if (intent.hasExtra("content")) {
            e001.setText(intent.getExtras().get("content").toString());
            time.setText(intent.getStringExtra("time"));
            id = intent.getStringExtra("id");
        }
    }

    private void initDB() {
        if (dbHper == null) {
            dbHper = new CAYFDbHelper(this, DB_FILE, null, DBversion);
            //起始資料庫為空，啟用FriendDbHelper建資料庫
            dbHper.createTable(); //新增此行，呼叫方法建立資料表
        }
    }

    private TimePickerDialog.OnTimeSetListener timepicker = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

//            String s = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.getTime());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String ss = simpleDateFormat.format(c.getTime());
            time.setText(ss);
        }
    };

    @Override
    public void onClick(View v) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        //.................................................................
        switch (v.getId()) {
            case R.id.mededit_timepicker:
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        this,
                        timepicker,
                        c.get(java.util.Calendar.HOUR_OF_DAY),
                        c.get(java.util.Calendar.MINUTE),
                        DateFormat.is24HourFormat(this)
                );
                timePickerDialog.setTitle(getString(R.string.mededit_tpTitle));
                timePickerDialog.setMessage(getString(R.string.mededit_tpMsg));
                timePickerDialog.setIcon(android.R.drawable.ic_dialog_info);
                timePickerDialog.setCancelable(false);
                timePickerDialog.show();
                break;

            case R.id.mededit_save:

                String content = e001.getText().toString().trim();
                String settime = time.getText().toString().trim();

                if (content.equals("") || settime.equals("")) {
                    Toast.makeText(MedEdit.this, "資料空白無法新增!", Toast.LENGTH_LONG).show();
                    return;
                }
                String msg = "";
                long rowID = -1;

                if (intent.hasExtra("content")) {
                    int nc = (int) System.currentTimeMillis();
                    String sc = String.valueOf(nc);

                    rowID = dbHper.updateRec(id, content, settime, sc);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                            Integer.parseInt(id),
                            new Intent(this, AlertReciver.class),
                            PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.cancel(pendingIntent);

                    Intent intent = new Intent(this, AlertReciver.class);
                    intent.putExtra("msg", content);
                    pendingIntent = PendingIntent.getBroadcast(this,
                            nc,
                            intent,
                            PendingIntent.FLAG_ONE_SHOT);
                    Calendar calendar = Calendar.getInstance();
                    String[] tn = settime.split(":");
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tn[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(tn[1]));
                    calendar.set(Calendar.SECOND, 0);
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


                    msg = (rowID != -1) ? "更新成功" : "更新失敗";
                } else {

                    int nc = (int) System.currentTimeMillis();
                    String sc = String.valueOf(nc);

                    rowID = dbHper.insertRec(content, settime, sc);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(this, AlertReciver.class);
                    intent.putExtra("msg", content);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, nc, intent, PendingIntent.FLAG_ONE_SHOT);
                    String[] tn = settime.split(":");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tn[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(tn[1]));
                    calendar.set(Calendar.SECOND, 0);

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                    msg = (rowID != -1) ? "新增成功" : "新增失敗";
                }
                Toast.makeText(MedEdit.this, msg, Toast.LENGTH_LONG).show();
                finish();
        }
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
        if (dbHper == null) {
            dbHper = new CAYFDbHelper(this, DB_FILE, null, DBversion);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mededit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent menu_it = new Intent();
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}