package com.example.administrator.woltest;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UpdateDialog {
    /**
     * 显示的Dialog
     */
    private Dialog mDialog;

    private Context mContext;

    private String[] mTips;

    private String mDownloadUrl;

    public UpdateDialog(Context context, String[] tips, String url) {
        this.mContext = context;
        this.mTips = tips;
        this.mDownloadUrl = url;
    }

    /**
     * 显示Dialog
     */
    public void show() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.Theme_dialog_empty);
            mDialog.setContentView(R.layout.update_dialog);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);

            TextView text_des = (TextView) mDialog.findViewById(R.id.text_des);
            if (TextUtils.isEmpty(mTips[2])) {
                text_des.setText("有重大更新,为保证更好的使用体验,须立即升级!");
            } else {
                text_des.setText(mTips[2]);
            }
            RelativeLayout btn = (RelativeLayout) mDialog.findViewById(R.id.rel_update);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击升级
                    UpdateThread uthread = new UpdateThread(mDownloadUrl, mContext);
                    Thread thread = new Thread(uthread);
                    thread.start();
                    mDialog.dismiss();
                }
            });

//            ImageView image = (ImageView) mDialog.findViewById(R.id.image_chacha);
//            image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

//            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    Log.e("fir", "对话框点击返回!!");
//                }
//            });
        }
        mDialog.show();
    }
}
