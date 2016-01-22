package test.qgd.com.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qgd.commons.tv.widget.Effectstype;
import com.qgd.commons.tv.widget.TvDialog;

public class MainActivity extends Activity {

    private Effectstype effect = Effectstype.Fadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TvDialog.createTipDialog(this,"呵呵呵！",4000).show();


        setContentView(R.layout.activity_main);
    }


    public void showDialog(View v) {

        TvDialog dialogBuilder = TvDialog.getInstance(this);
        dialogBuilder.withTitle("提示信息")                                  //.withTitle(null)  no title
                //.withTitleColor("#FFFFFF")                                  //def
                //.withDividerColor("#11000000")                              //def
                .withMessage("对话框")                     //.withMessage(null)  no Msg
                //.withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                //.withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)                               //def
                //.withIcon(getResources().getDrawable(R.drawable.ic_launcher)).isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(100)                                          //def
                .withEffect(effect)                                         //def Effectstype.Slidetop
                                                    //def gone
                                               //def gone
                      //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "按钮一", Toast.LENGTH_SHORT).show();
                    }
                }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "按钮二", Toast.LENGTH_SHORT).show();
            }
        });

        switch (v.getId()){
            case R.id.dialogOk:
                dialogBuilder.withButton1Text("确定");
                break;
            case R.id.dialogBoth:
                dialogBuilder.withButton1Text("确定");
                dialogBuilder.withButton2Text("取消");
                break;
            case R.id.dialogNone:

                break;
            case R.id.dialogCustom:
                dialogBuilder.setCustomView(R.layout.custom_view, this);
                break;
        }


        dialogBuilder.show();
    }
}
