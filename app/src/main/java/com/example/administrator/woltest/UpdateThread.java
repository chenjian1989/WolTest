package com.example.administrator.woltest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateThread extends Thread {

    private String url;
    private int notification_id = 19172439;
    private NotificationManager nm;
    private Notification notification;
    private int count, lastCount = 0;
    private int divide = 1;
    private Context mContext;

    public UpdateThread(String url, Context context) {
//        this.url = "http://pub.i8xiaoshi.com/latest/i8xiaoshi.apk";
        this.url = url;
        this.mContext = context;
    }

    @Override
    public void run() {

        if (!MyApplication.getInstance().isNetworkAvailable()) {
            MyApplication.getInstance().toastMiddle(
                    MyApplication.getInstance().getString(R.string.error_no_network));
            return;
        }

        if (!MyApplication.getInstance().existSDCard()) {
            MyApplication.getInstance().toastMiddle("没有sd卡,无法在线更新,请到官网下载最新程序!");
            return;
        }

        nm = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);

        notification = new Notification(R.drawable.ic_launcher, "   "
                + MyApplication.getInstance().getString(R.string.msg_update),
                System.currentTimeMillis());
        notification.contentView = new RemoteViews(mContext.getPackageName(),
                R.layout.progressbar_download_notify);
        notification.contentView.setProgressBar(R.id.down_pb, 100, 0, false);
        Intent notificationIntent = new Intent(mContext, UpdateThread.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;
        showNotification();
        downFile(url, MyApplication.getInstance().getFilePath() + "temp.apk");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(MyApplication.getInstance().getFilePath(), "temp.apk")),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public void downFile(String url, String path) {
        try {
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            int fileSize = conn.getContentLength();// 根据响应获取文件大小
            if (fileSize <= 0)
                throw new RuntimeException("无法获知文件大小 ");
            if (is == null)
                throw new RuntimeException("stream is null");
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(path);
            // 把数据存入路径+文件名
            byte buf[] = new byte[1024];
            int downLoadFileSize = 0;

            do {
                // 循环读取
                int numread = is.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
                downLoadFileSize += numread;
                count = 100 * downLoadFileSize / fileSize;
                if (count - lastCount > divide) {
                    lastCount = count;
                    notification.contentView.setProgressBar(R.id.down_pb, 100, count, false);
                    showNotification();
                }
            } while (true);

            is.close();

            nm.cancel(notification_id);
        } catch (Exception ex) {
            nm.cancel(notification_id);
        }
    }

    public void showNotification() {
        nm.notify(notification_id, notification);
    }
}
