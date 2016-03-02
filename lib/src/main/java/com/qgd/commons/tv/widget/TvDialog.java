package com.qgd.commons.tv.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgd.commons.tv.R;
import com.qgd.commons.tv.util.ColorUtils;
import com.qgd.commons.tv.util.DimensionConvert;
import com.qgd.commons.tv.widget.effects.BaseEffects;


public class TvDialog extends Dialog implements DialogInterface {

    private final String defTextColor = "#FFFFFFFF";

    private final String defDividerColor = "#11000000";

    private final String defMsgColor = "#FFFFFFFF";

    private final String defDialogColor = "#55808080";


    private static Context tmpContext;


    private Effectstype type = null;

    private LinearLayout mLinearLayoutView;

    private RelativeLayout mRelativeLayoutView;


    private LinearLayout mLinearLayoutTopView;

    private FrameLayout mFrameLayoutCustomView;

    private View mDialogView;

    private View mDivider;

    private TextView mTitle;

    private TextView mMessage;

    private ImageView mIcon;

    private Button mButton1;

    private Button mButton2;

    private Button mButton3;

    private View mMiddleLineView;
    private View mMiddleLineView1;

    private int mDuration = -1;

    private static int mOrientation = 1;

    private boolean isCancelable = true;


    private CountDownTimer timer = null;

    private Context mContext;

    private int mHeight=220;
    private int mWidth=450;

    WindowManager.LayoutParams params ;

    private  boolean isButtonFocus=true;

    public TvDialog(Context context) {
        super(context);
        init(context);

    }

    public TvDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = DimensionConvert.px2dip(getContext(), mWidth );
        params.height = DimensionConvert.px2dip(getContext(), mHeight);
        params.dimAmount=0.8f;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);



    }

    private static TvDialog getInstance(Context context) {

        return new TvDialog(context, R.style.dialog_untran);
    }


    public void setSize(int width,int height){
        mWidth=width;
        mHeight=height;
    }

    public static TvDialog createDefaultDialog(Context context) {
        TvDialog dialog = getInstance(context);
        dialog.setCustomView(R.layout.normal_view, context);
        return dialog;
    }

    public static TvDialog createDialog(Context context) {
        return createDefaultDialog(context);
    }

    public static TvDialog createDialog(Context context,String message) {
        return createDialog(context,"提示",message);
    }
    public static TvDialog createDialog(Context context, String title, String message) {
        TvDialog dialog = getInstance(context);
        dialog.setCustomView(R.layout.normal_view, context);
        dialog.withMessage(message);
        dialog.withTitle(title);
        return dialog;
    }

    public static TvDialog createDialog(Context context, String title, String message, String button1) {
        TvDialog dialog = getInstance(context);
        dialog.setCustomView(R.layout.normal_view, context);
        dialog.withMessage(message);
        dialog.withTitle(title);
        if (button1 != null)
            dialog.withButton1Text(button1);
        return dialog;
    }

    public static TvDialog createDialog(Context context, int title, int message) {
        return createDialog(context, getString(context, title), getString(context, message));
    }

    public static TvDialog createDialog(Context context, int title, int message, int button1) {
        return createDialog(context, getString(context, title), getString(context, message), getString(context, button1));
    }

    public static TvDialog createDialog(Context context, int title, int message, int button1, int button2) {
        return createDialog(context, getString(context, title), getString(context, message), getString(context, button1), getString(context, button2));
    }

    public static TvDialog createDialog(Context context, String title, String message, String button1, String button2) {
        TvDialog dialog = getInstance(context);
        dialog.setCustomView(R.layout.normal_view, context);
        dialog.withMessage(message);
        dialog.withTitle(title);
        if (button1 != null)
            dialog.withButton1Text(button1);
        if (button2 != null)
            dialog.withButton2Text(button2);

        return dialog;
    }

    public static TvDialog createProgressDialog(Context context, String title, String message) {
        TvDialog dialog = getInstance(context);
        dialog.withTitle(title);
        dialog.setCustomView(R.layout.progress_view, context);
        GifView gif = (GifView) dialog.mFrameLayoutCustomView.findViewById(R.id.progress);
        gif.setVisibility(View.VISIBLE);

        dialog.withMessage(message);
        return dialog;
    }

    public static TvDialog createProgressDialog(Context context, int title, int message) {
        return createProgressDialog(context, getString(context, title), getString(context, message));
    }


    public static TvDialog createTipDialog(Context context, int message) {
        return createTipDialog(context, getString(context, message));
    }

    public static TvDialog createTipDialog(Context context, String message) {
        TvDialog dialog = getInstance(context);
        dialog.hideTop();
        dialog.setCustomView(R.layout.tip_view, context);

        dialog.withMessage(message);
        return dialog;
    }

    private static String getString(Context context, int message) {
        if (context != null) {
            return context.getString(message);
        } else {
            return "";
        }
    }

    public static final int LONG_DELAY = 3500; // 3.5 seconds
    public static final int SHORT_DELAY = 2000; // 2 seconds

    public static TvDialog createToastDialog(Context context, String message) {
        return createTipDialog(context, message, SHORT_DELAY);
    }

    public static TvDialog createToastDialog(Context context, String message, int duration) {
        return createTipDialog(context, message, duration);
    }

    public static TvDialog createToastDialog(Context context, int message) {
        TvDialog dialog=createTipDialog(context, getString(context, message), SHORT_DELAY);

        return dialog;
    }

    public static TvDialog createToastDialog(Context context, int message, int duration) {
        return createTipDialog(context, getString(context, message), duration);
    }

    public static TvDialog createTipDialog(Context context, String message, int timeOut) {
        final TvDialog dialog = createTipDialog(context, message);
        CountDownTimer timer = new CountDownTimer(timeOut, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        };
        dialog.setCountDownTimer(timer);
        return dialog;
    }

    public void setCountDownTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    private void init(Context context) {
        mContext=context;
        mDialogView = View.inflate(context, R.layout.dialog_layout, null);

        mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);
        mRelativeLayoutView = (RelativeLayout) mDialogView.findViewById(R.id.main);
        mLinearLayoutTopView = (LinearLayout) mDialogView.findViewById(R.id.topPanel);

        mFrameLayoutCustomView = (FrameLayout) mDialogView.findViewById(R.id.customPanel);

        mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
        mMessage = null;

        mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
        mDivider = mDialogView.findViewById(R.id.titleDivider);
        mButton1 = (Button) mDialogView.findViewById(R.id.button1);
        mButton2 = (Button) mDialogView.findViewById(R.id.button2);
        mButton3 = (Button) mDialogView.findViewById(R.id.button3);

        mMiddleLineView = mDialogView.findViewById(R.id.middleLineView);
        mMiddleLineView1 = mDialogView.findViewById(R.id.middleLineView1);


        setContentView(mDialogView);

        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                mLinearLayoutView.setVisibility(View.VISIBLE);
                if (type == null) {
                    type = Effectstype.Fadein;
                }
                start(type);


            }
        });
        mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable)
                    dismiss();
            }
        });
    }

    private void hideTop() {
        mLinearLayoutTopView.setVisibility(View.GONE);
    }

    public void toDefault() {
        mTitle.setTextColor(Color.parseColor(defTextColor));
        mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
        mMessage.setTextColor(Color.parseColor(defMsgColor));
        mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
    }

    public void setButtonFocusable(boolean b){
        isButtonFocus=b;
    }
    public boolean getButtonFocusable(){
        return isButtonFocus;
    }

    public TvDialog withDividerColor(String colorString) {
        mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }

    public TvDialog withDividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }

    public TvDialog withTitle(int title){
        return withTitle( mContext.getString(title) );
    }
    public TvDialog withTitle(CharSequence title) {
        toggleView(mLinearLayoutTopView, title);
        mTitle.setText(title);
        return this;
    }

    public TvDialog withTitleColor(String colorString) {
        mTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public TvDialog withTitleColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }

    public TvDialog withMessage(int textResId) {
        mMessage = (TextView) mFrameLayoutCustomView.findViewWithTag("message");
        if (mMessage != null)
            mMessage.setText(textResId);
        return this;
    }

    public TvDialog withMessage(CharSequence msg) {
        mMessage = (TextView) mFrameLayoutCustomView.findViewWithTag("message");
        if (mMessage != null)
            mMessage.setText(msg);
        return this;
    }

    public TvDialog withMessageColor(String colorString) {
        mMessage = (TextView) mFrameLayoutCustomView.findViewWithTag("message");
        if (mMessage != null)
            mMessage.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public TvDialog withMessageColor(int color) {
        if (mMessage != null)
            mMessage.setTextColor(color);
        return this;
    }

    public TvDialog withDialogColor(String colorString) {
        mMessage = (TextView) mFrameLayoutCustomView.findViewWithTag("message");
        if (mMessage != null)
            mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(Color.parseColor(colorString)));
        return this;
    }

    public TvDialog withDialogColor(int color) {
        mMessage = (TextView) mFrameLayoutCustomView.findViewWithTag("message");
        if (mMessage != null)
            mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
        return this;
    }

    public TvDialog withIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    public TvDialog withIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }

    public TvDialog withDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public TvDialog withEffect(Effectstype type) {
        this.type = type;
        return this;
    }

    public TvDialog withButtonDrawable(int resid) {
        mButton1.setBackgroundResource(resid);
        mButton2.setBackgroundResource(resid);
        mButton3.setBackgroundResource(resid);

        return this;
    }

    public TvDialog withButton2Text(int text){
        return withButton2Text( mContext.getString(text) );
    }

    public TvDialog withButton1Text(int text){
        return withButton1Text( mContext.getString(text) );
    }
    public TvDialog withButton1Text(CharSequence text) {
        mButton1.setVisibility(View.VISIBLE);
        mButton1.setText(text);
        if(isButtonFocus)
            mButton1.requestFocus();

        return this;
    }

    private void showMiddleLine() {
        if (mButton1.getVisibility() == View.VISIBLE && mButton2.getVisibility() == View.VISIBLE) {
            mMiddleLineView.setVisibility(View.VISIBLE);
            mButton1.setBackgroundResource(R.drawable.btn_selector_left);
            mButton2.setBackgroundResource(R.drawable.btn_selector_right);
        } else {
            mButton1.setBackgroundResource(R.drawable.btn_selector_bottom);
            mMiddleLineView.setVisibility(View.GONE);
        }
        if(mButton2.getVisibility() == View.VISIBLE&&mButton3.getVisibility()==View.VISIBLE){
            mMiddleLineView1.setVisibility(View.VISIBLE);
            mButton2.setBackgroundResource(R.drawable.btn_selector_middle);
            mButton3.setBackgroundResource(R.drawable.btn_selector_right);

        }
    }

    public TvDialog withButton2Text(CharSequence text) {
        mButton2.setVisibility(View.VISIBLE);
        mButton2.setText(text);

        return this;
    }

    public TvDialog withButton3Text(CharSequence text) {
        mButton3.setVisibility(View.VISIBLE);
        mButton3.setText(text);

        return this;
    }

    public TvDialog setButton1Click(View.OnClickListener click) {
        mButton1.setOnClickListener(click);
        return this;
    }
    private String getString(int id){
        if(mContext!=null)
            return mContext.getString(id);
        else
            return "";
    }

    public TvDialog setButton1Click(int button1Name,View.OnClickListener click) {
        return setButton1Click(getString(button1Name),click);
    }

    public TvDialog setButton2Click(int button2Name,View.OnClickListener click) {
        return setButton2Click(getString(button2Name),click);
    }
    public TvDialog setButton1Click(String button1Name,View.OnClickListener click) {
        withButton1Text(button1Name);
        setButton1Click(click);
        return this;
    }

    public TvDialog setButton2Click(String button2Name,View.OnClickListener click) {
        withButton2Text(button2Name);
        setButton2Click(click);
        return this;
    }

    public TvDialog setButton2Click(View.OnClickListener click) {
        mButton2.setOnClickListener(click);
        return this;
    }
    public TvDialog setButton3Click(View.OnClickListener click) {
        mButton3.setOnClickListener(click);
        return this;
    }


    public TvDialog setCustomView(int resId, Context context) {
        View customView = View.inflate(context, resId, null);

        if (mFrameLayoutCustomView.getChildCount() > 0) {
            mFrameLayoutCustomView.removeAllViews();

        }
        mFrameLayoutCustomView.addView(customView);
        return this;
    }

    public TvDialog setCustomView(View view, Context context) {
        if (mFrameLayoutCustomView.getChildCount() > 0) {
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(view);

        return this;
    }

    public TvDialog isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public TvDialog isCancelable(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCancelable(cancelable);
        return this;
    }

    private void toggleView(View view, Object obj) {
        if (view == null)
            return;
        if (obj == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {

        showMiddleLine();
        if (timer != null) {
            timer.start();
        }
        super.show();
    }

    private void start(Effectstype type) {
        BaseEffects animator = type.getAnimator();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mRelativeLayoutView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
        mButton3.setVisibility(View.GONE);

    }

    public Button getButton1() {
        return mButton1;
    }

    public Button getButton2() {
        return mButton2;
    }

    public Button getButton3() {
        return mButton3;
    }
}
