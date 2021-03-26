package tw.careabout.yourfamily;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Open extends AppCompatActivity implements Runnable{

    private ImageView topImage;
    private TextView t001;
    private Intent intent = new Intent();
    private MediaPlayer startmusic;
    private Handler openhandler = new Handler();


    //權限
    private static final String[][] permissionsArray = new String[][]{
            {Manifest.permission.ACCESS_FINE_LOCATION, "精確定位"},
            {Manifest.permission.ACCESS_COARSE_LOCATION, "一般定位"}
    };
    private List<String> permissionsList = new ArrayList<String>();
    //申請權限後的返回碼
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkRequiredPermission(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open);
        setupViewConponent();
    }

    private void setupViewConponent() {

        topImage = (ImageView) findViewById(R.id.login_image);
        t001 = (TextView) findViewById(R.id.login_t001);
        startmusic = MediaPlayer.create(getApplicationContext(), R.raw.startmusic);

        topImage.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        t001.setAnimation(AnimationUtils.loadAnimation(this, R.anim.move_in_from_bottom));
        startmusic.start();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void run() {
        Intent intent = new Intent();
        intent.setClass(Open.this, Main.class);
        startActivity(intent);
        openhandler.removeCallbacks(this);
        finish();
    }

    private void checkRequiredPermission(final Activity activity) { //
        //        String permission_check= String[i][0] permission;
        for (int i = 0; i < permissionsArray.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissionsArray[i][0]) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permissionsArray[i][0]);
            }
        }
        if (permissionsList.size() != 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }else {
            openhandler.postDelayed(this,2000);
        }

    }

    /*** request需要的權限*/
    private void requestNeededPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
    }

    /*** ***********************************
     *  所需要申請的權限數組
     * ************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), permissionsArray[i][1] + "權限申請成功!", Toast.LENGTH_SHORT).show();
                        openhandler.postDelayed(this,2000);
                    } else {
                        //Toast.makeText(getApplicationContext(), "權限被拒絕： " + permissionsArray[i][1], Toast.LENGTH_SHORT).show();
                        //------------------
                        // 這邊是照官網說法，在確認沒有權限的時候，確認是否需要說明原因
                        // 需要的話就先顯示原因，在使用者看過原因後，再request權限
                        //-------------------
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Util.showDialog(this, R.string.dialog_msg1, android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestNeededPermission();
                                }
                            });
                        } else {
                            // 否則就直接request
                            requestNeededPermission();
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
