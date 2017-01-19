package com.example.administrator.woltest;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

public class MyApplication extends Application {

    private final String mFirToken = "f763f154b6657147d94c698425128984";

    /**
     * MyApplication单例对象
     */
    private static MyApplication mInstance;

    private Toast mToast;

    /**
     * 存储文件的根目录
     */
    private String filePath;

    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FIR.init(this);
    }

    /**
     * 获取MyApplication单例
     *
     * @return MyApplication
     */
    public static MyApplication getInstance() {
        return mInstance;
    }

    public void CheckVersion(final Context context) {
        FIR.checkForUpdateInFIR(mFirToken, new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                Log.e("fir", "check from fir.im success! " + "\n" + versionJson);
                if (!TextUtils.isEmpty(versionJson)) {
                    VersionEntity versionEntity = new Gson().fromJson(versionJson, VersionEntity.class);
                    if (versionEntity.getBuild() > getVersion()
                            && !TextUtils.isEmpty(versionEntity.getDirect_install_url())) {
                        // 有新版
                        String[] tips = {
                                getResources().getString(R.string.tip_new_version_forceup),
                                getResources().getString(
                                        R.string.tip_more_version_hasNewer_forceup)
                                , versionEntity.getChangelog(),
                                getResources().getString(R.string.update_now),
                                getResources().getString(R.string.update_later)
                        };
                        UpdateDialog dialog = new UpdateDialog(context, tips
                                , versionEntity.getDirect_install_url());
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFail(Exception exception) {
                Log.e("fir", "check fir.im fail! " + "\n" + exception.getMessage());
            }

            @Override
            public void onStart() {
                //Toast.makeText(getApplicationContext(), "正在获取", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                //Toast.makeText(getApplicationContext(), "获取完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取当前应用的版本号
     *
     * @return
     */
    public int getVersion() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getApplicationContext().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查网络是否好用.
     *
     * @return true or false
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            // 如果仅仅是用来判断网络连接　　　　　　
            // 则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo workinfo : info) {
                    if (workinfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 显示提示信息，居中显示
     */
    public void toastMiddle(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(mInstance, msg, Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                Looper.loop();
            }
        });
    }

    /**
     * 返回是否存在sd卡.
     *
     * @return
     */
    public boolean existSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else
            return false;
    }

    /**
     * 返回手机存储目录的位置
     * 如果有sd卡，返回sd卡的根目录，否则返回手机的缓存目录
     *
     * @return String
     */
    public String getFilePath() {
        if (filePath != null) {
            return filePath;
        } else {
            if (existSDCard()) {
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                        + MyApplication.getInstance().getPackageName() + File.separator;
                File file = new File(filePath);
                if(!file.exists()){
                    file.mkdirs();
                }
                return filePath;
            } else {
                filePath = MyApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator
                        + MyApplication.getInstance().getPackageName() + File.separator;
                File file = new File(filePath);
                if(!file.exists()){
                    file.mkdirs();
                }
                return filePath;
            }
        }
    }
}
