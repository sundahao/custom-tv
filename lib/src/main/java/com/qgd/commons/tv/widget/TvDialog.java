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
    private View mMiddleLineView;

    private int mDuration = -1;

    private static int mOrientation = 1;

    private boolean isCancelable = true;


    private CountDownTimer timer = null;

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
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = DimensionConvert.px2dip(getContext(), 450);
        params.height = DimensionConvert.px2dip(getContext(), 220);
        getWindow().setAttributes((WindowManager.LayoutParams) params);

    }

    private static TvDialog getInstance(Context context) {
        return  new TvDialog(context, R.style.dialog_untran);
    }

    public static TvDialog createDefaultDialog(Context context) {
        TvDialog dialog = getInstance(context);
        dialog.setCustomView(R.layout.normal_view, context);
        return dialog;
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

    public static TvDialog createDialog(Context context, String title, String message, String button1, String button2) {
        TvDialog dialog = getInstance(context);
        dialog.setCustomView(R.layout.normal_view, context);
        dialog.withMessage(message);
        dialog.withTitle(title);
        if (button1 != null)
            dialog.withButton1Text(button1);
        if (button2 != null)
            dialog.withButton1Text(button2);

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

    public static TvDialog createTipDialog(Context context, String message) {
        TvDialog dialog = getInstance(context);
        dialog.hideTop();
        dialog.setCustomView(R.layout.tip_view, context);

        dialog.withMessage(message);
        return dialog;
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
        mMiddleLineView = mDialogView.findViewById(R.id.middleLineView);


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

    public TvDialog withDividerColor(String colorString) {
        mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }

    public TvDialog withDividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
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
        return this;
    }

    public TvDialog withButton1Text(CharSequence text) {
        mButton1.setVisibility(View.VISIBLE);
        mButton1.setText(text);


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
    }

    public TvDialog withButton2Text(CharSequence text) {
        mButton2.setVisibility(View.VISIBLE);
        mButton2.setText(text);

        return this;
    }

    public TvDialog setButton1Click(View.OnClickListener click) {
        mButton1.setOnClickListener(click);
        return this;
    }

    public TvDialog setButton2Click(View.OnClickListener click) {
        mButton2.setOnClickListener(click);
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
    }


}
