package com.qgd.commons.tv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 作者：ethan on 2016/1/5 16:54
 * 邮箱：ethan.chen@fm2020.com
 * 页面指示器
 */
public class TextPageIndicatorView extends TextView implements PageViewIndicatorInterface {
    private int totalCount;
    private ImageView leftArrow;
    private ImageView rightArrow;
    public TextPageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void initIndicator(int count) {
        totalCount=count;
    }
    public void setArrow(ImageView left,ImageView right){
        leftArrow=left;
        rightArrow=right;
    }
    public void setTotalPage(int totalpage){
        this.totalCount=totalpage;
    }
    public void setSelectedPage(int selected) {
        setText(selected+"/"+totalCount+"页");
        if(leftArrow!=null&&rightArrow!=null){
            if(selected==1){
                leftArrow.setVisibility(View.INVISIBLE);
                if(totalCount==1)
                    rightArrow.setVisibility(View.INVISIBLE);
                else{
                    rightArrow.setVisibility(View.VISIBLE);
                }
            }else if(selected==totalCount){
                rightArrow.setVisibility(View.INVISIBLE);
                leftArrow.setVisibility(View.VISIBLE);
            }else{
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
            }
        }
    }
}
