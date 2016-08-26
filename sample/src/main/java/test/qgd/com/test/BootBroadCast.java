package test.qgd.com.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.qgd.commons.tv.widget.TvDialog;

/**
 * 作者：ethan on 2016/8/26 15:30
 * 邮箱：ethan.chen@fm2020.com
 */
public class BootBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final TvDialog tvDialog = TvDialog.createDialog(context, "提示!", "开启启动对话框测试");
        tvDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        tvDialog.setCancelable(false);
//        tvDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                System.out.println("onShow.....");
//            }
//        });

        tvDialog.withButton1Text("按钮1");
        tvDialog.withButton2Text("按钮2");
        tvDialog.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDialog.dismiss();
            }
        });



        tvDialog.show();
    }

}
