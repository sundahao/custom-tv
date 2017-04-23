package test.qgd.com.test;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.qgd.commons.tv.widget.TvDialog;

public class MainActivity extends Activity {


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TvDialog tvDialog = TvDialog.createSimpleDialog(this);
        tvDialog.withMessage("您今天已经没有抽奖的机会了明天可以继续抽奖哦");
        //tvDialog.setSize(500, 100);//可以设置大小
        tvDialog.show();
        tvDialog.withButton1Text("我知道了");
        tvDialog.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDialog.withButton1Text("点解了");
            }
        });
        context = this;

        setContentView(R.layout.activity_main);


    }

    String str = "";
    int i = 0;

    public void showToastDialog(View v) {
       final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final TvDialog tvDialog = TvDialog.createToastDialog(context, str);
                tvDialog.show();
                str += "啊";
                i++;
                handler.postDelayed(this,3000);
            }
        }, 3000);

    }

    public void showNoCancelDialog(View v) {//不可取消按钮
        final TvDialog tvDialog = TvDialog.createDialog(getApplicationContext(), "提示!", "不可按返回取消");
        tvDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        tvDialog.setCancelable(false);
        tvDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                System.out.println("onShow.....");
            }
        });

        tvDialog.setButton1Click("只能点这里取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDialog.dismiss();
            }
        });
        tvDialog.show();
    }

    public void showTipDialog(View v) {//无标题，
        TvDialog.createTipDialog(this, "我是一只tip").show();
    }

    public void showProgressDialog(View v) {//显示进度条
        TvDialog.createProgressDialog(this, "提示!", "额鹅鹅鹅进度条").show();
    }
    public  void showMarginDialog(View v){
        TvDialog dialog= TvDialog.createDialogMargin(this);
        dialog.withTitle("提示!");
        dialog.withMessage("额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条额鹅鹅鹅进度条");
        dialog.withButton1Text("上一页");
        dialog.withButton2Text("关闭");
        dialog.withButton3Text("下一页");
        dialog.setSize(600,400);
        dialog.show();

    }

    public void showDialog(View v) {

        TvDialog dialogBuilder = TvDialog.createDefaultDialog(this);
        dialogBuilder.withMessage("内容内容内容内容内容内容,内容内容内容内容内容内容");//内容
        dialogBuilder.withTitle("提示信息")//标题
                .setButton1Click(new View.OnClickListener() {//按钮事件
                    @Override
                    public void onClick(View v) {
                        TvDialog.createToastDialog(context, "按了确定").show();
                    }
                }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TvDialog.createToastDialog(context, "按了取消").show();
            }
        });

        switch (v.getId()) {//一个按钮 确定
            case R.id.dialogOk:
                dialogBuilder.withButton1Text("确定");
                break;
            case R.id.dialogBoth://2个按钮
                dialogBuilder.withButton1Text("确定");
                dialogBuilder.withButton2Text("取消");
                dialogBuilder.withMessage("内容内容内容内容内容内容");//内容

                break;

            case R.id.dialogThree:
                dialogBuilder.withButton1Text("确定");
                dialogBuilder.withButton2Text("取消");
                dialogBuilder.withButton3Text("多出来");
                dialogBuilder.setButton3Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TvDialog.createToastDialog(context, "我是多出来的按钮么").show();
                    }
                });
                break;
            case R.id.dialogNone://无按钮

                break;
            case R.id.dialogCustom://自定义内部显示
                dialogBuilder.setCustomView(R.layout.custom_view, this);
                break;
        }


        dialogBuilder.show();
    }
}
