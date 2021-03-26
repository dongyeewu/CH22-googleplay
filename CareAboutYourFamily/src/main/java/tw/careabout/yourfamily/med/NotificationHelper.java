package tw.careabout.yourfamily.med;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import tw.careabout.yourfamily.R;

public class NotificationHelper extends ContextWrapper {

    public static final String ChannelID= "CAYF";
    public static final String ChannelName= "定時提醒";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel = new NotificationChannel(ChannelID,ChannelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.design_default_color_primary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(Context context,String msg){
        return  new NotificationCompat.Builder(context,ChannelID)
                .setContentText(msg)
                .setContentTitle(getString(R.string.medshow_notifyTitle))
                .setSmallIcon(R.drawable.alpaca007);
    }
}
