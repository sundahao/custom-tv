package test.qgd.com.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.qgd.commons.tv.widget.TvDialog;

public class MainActivity extends Activity {


    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TvDialog tvDialog = TvDialog.createToastDialog(this, "呵呵呵！"); //类似toast
        tvDialog.setSize(500, 100);//可以设置大小
        //tvDialog.show();
        context=this;

        setContentView(R.layout.activity_main);


    }

    public  void showToastDialog(View v){
        TvDialog.createToastDialog(this, "我是一只Toast").show();
    }

    public void showNoCancelDialog(View v) {//不可取消按钮
        final TvDialog tvDialog=TvDialog.createDialog(this, "提示!", "不可按返回取消");
        tvDialog.setCancelable(false);
        tvDialog.setButton1Click("只能点这里取消",new View.OnClickListener(){
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

    public void showDialog(View v) {

        TvDialog dialogBuilder = TvDialog.createDefaultDialog(this);
        dialogBuilder.withMessage("内容内容内容内容内容内容");//内容
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
