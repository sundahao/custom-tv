package test.qgd.com.test;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 作者：ethan on 2016/2/18 11:46
 * 邮箱：ethan.chen@fm2020.com
 */
public class WidgetStackActivity extends Activity implements View.OnFocusChangeListener, View.OnClickListener, View.OnKeyListener, View.OnLayoutChangeListener {


    RelativeLayout listRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stack);


        RelativeLayout main = (RelativeLayout) findViewById(R.id.listRelativeLayout);


        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);

        listRelativeLayout = (RelativeLayout) findViewById(R.id.listRelativeLayout);

        for (int i = 0; i < listRelativeLayout.getChildCount(); i++) {
            listRelativeLayout.getChildAt(i).setOnFocusChangeListener(this);
            listRelativeLayout.getChildAt(i).setOnClickListener(this);
            listRelativeLayout.getChildAt(i).setOnKeyListener(this);
            listRelativeLayout.getChildAt(i).addOnLayoutChangeListener(this);
        }

        textView1.requestFocus();


        LayoutTransition layoutTransition = new LayoutTransition();
        listRelativeLayout.setLayoutTransition(layoutTransition);

    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator transAnimatorLeft = ObjectAnimator.ofInt(v, "left", oldLeft, left);
        ObjectAnimator transAnimatorTop = ObjectAnimator.ofInt(v, "top", oldTop, top);

        ObjectAnimator transAnimatorRight = ObjectAnimator.ofInt(v, "right", oldRight, right);
        ObjectAnimator transAnimatorBottom = ObjectAnimator.ofInt(v, "bottom", oldBottom, bottom);

        animatorSet.playTogether(transAnimatorLeft, transAnimatorTop,transAnimatorRight,transAnimatorBottom );
        animatorSet.setDuration(500);
        animatorSet.start();

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();
        if (!b) {
        }

    }

    private void closeStack(View view) {
        int i = listRelativeLayout.indexOfChild(view);
        if (i != (listRelativeLayout.getChildCount() - 1)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.height = 175;
            params.removeRule(RelativeLayout.RIGHT_OF);
            if (i != 0)
                params.addRule(RelativeLayout.BELOW, listRelativeLayout.getChildAt(i - 1).getId());
            view.setLayoutParams(params);

        }

    }

    private void openStack(View view) {
        int id = view.getId();
        int i = listRelativeLayout.indexOfChild(view);

        for (int j = i; j < listRelativeLayout.getChildCount(); j++) {
            view = listRelativeLayout.getChildAt(j);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.height = 550;
            if (j != 0) {
                params.removeRule(RelativeLayout.BELOW);
                params.addRule(RelativeLayout.RIGHT_OF, listRelativeLayout.getChildAt(j - 1).getId());
            }
            view.setLayoutParams(params);


        }

    }

    @Override
    public void onClick(View view) {
        openStack(view);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                closeStack(view);
            }
        }
        return false;
    }
}
