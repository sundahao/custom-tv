package com.qgd.commons.tv.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;


public class AutoGridLayoutManager extends GridLayoutManager {

    private int measuredWidth = 0;
    private int measuredHeight = 0;

    public AutoGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AutoGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public AutoGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


    private int[] mMeasuredDimension = new int[2];

//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//        final int widthMode = View.MeasureSpec.getMode(widthSpec);
//        final int heightMode = View.MeasureSpec.getMode(heightSpec);
//        final int widthSize = View.MeasureSpec.getSize(widthSpec);
//        final int heightSize = View.MeasureSpec.getSize(heightSpec);
//        System.out.println("width:"+widthSize+" heith:"+heightSize);
//        int width = 0;
//        int height = 0;
//        int count = getItemCount();
//        int span = getSpanCount();
//        for (int i = 0; i < count; i++) {
//            measureScrapChild(recycler, i,
//                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//                    mMeasuredDimension);
//
//            if (getOrientation() == HORIZONTAL) {
//                if (i % span == 0) {
//                    width = width + mMeasuredDimension[0];
//                }
//                if (i == 0) {
//                    height = mMeasuredDimension[1];
//                }
//            } else {
//                if (i % span == 0) {
//                    height = height + mMeasuredDimension[1];
//                }
//                if (i == 0) {
//                    width = mMeasuredDimension[0];
//                }
//            }
//        }
//
//        switch (widthMode) {
//            case View.MeasureSpec.EXACTLY:
//                width = widthSize;
//            case View.MeasureSpec.AT_MOST:
//            case View.MeasureSpec.UNSPECIFIED:
//        }
//
//        switch (heightMode) {
//            case View.MeasureSpec.EXACTLY:
//                height = heightSize;
//            case View.MeasureSpec.AT_MOST:
//            case View.MeasureSpec.UNSPECIFIED:
//        }
//
//        setMeasuredDimension(width, height);
//    }
//
//    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
//            int heightSpec, int[] measuredDimension) {
//        if (position < getItemCount()) {
//            try {
//                View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException
//                if (view != null) {
//                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
//                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
//                            getPaddingLeft() + getPaddingRight(), p.width);
//                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
//                            getPaddingTop() + getPaddingBottom(), p.height);
//                    view.measure(childWidthSpec, childHeightSpec);
//                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
//                    measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
//                    recycler.recycleView(view);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//                View view = recycler.getViewForPosition(0);
//                if(view != null){
//                    measureChild(view, widthSpec, heightSpec);
//
//                    int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//                    int measuredHeight = view.getMeasuredHeight();
//                    System.out.println("measuredWidth:"+measuredWidth+" measuredHeight:"+measuredHeight);
//
//                    setMeasuredDimension(measuredWidth, measuredHeight);
//                }
//
//    }

//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//
//        //核心就是下面这块代码块啦
//        int width = getMeasuredWidth();
//        setMeasuredDimension(width, width);
//        ViewGroup.LayoutParams lp = getLayoutParams();
//        lp.height = width;
//        setLayoutParams(lp);
//    }

    //        @Override
    //    public void onMeasure(RecyclerView.Recycler recycler,
    //                          RecyclerView.State state, int widthSpec, int heightSpec) {
    //        if (measuredHeight <= 0) {
    //            View view = recycler.getViewForPosition(0);
    //            if (view != null) {
    //                measureChild(view, widthSpec, heightSpec);
    //                measuredWidth = View.MeasureSpec.getSize(widthSpec);
    //                measuredHeight = view.getMeasuredHeight() * getSpanCount();
    //            }
    //        }
    //        setMeasuredDimension(measuredWidth, measuredHeight);
    //    }

}