package com.qgd.commons.tv.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qgd.commons.tv.R;


public class TvTabPageIndicator extends LinearLayout implements PageIndicatorInterface {

    private ViewPager.OnPageChangeListener mPageChangeListener;
    private ViewPager mViewPager;
    private int mSelectedTabIndex;
    private final LinearLayout mTabLayout;
    private Runnable mTabSelector;
    private static final CharSequence EMPTY_TITLE = "";
    private int mMaxTabWidth;
    private OnTabReselectedListener mTabReselectedListener;


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //        Log.d("TvTabPageIndicator","dispatchKeyEvent "+event.getSource());

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            //            Log.d("TvTabPageIndicator","KEYCODE_DPAD_LEFT RIGHT");

            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private void onSelect(View view) {

        View tabView = view;
        final int oldSelected = mViewPager.getCurrentItem();
        final int newSelected = tabView.getId();
        mViewPager.setCurrentItem(newSelected);

        if (oldSelected == newSelected && mTabReselectedListener != null) {
            mTabReselectedListener.onTabReselected(newSelected);
        }
        for (int i = 0; i < mTabLayout.getChildCount(); i++) {
            if (mSelectedTabIndex != i) {
                mTabLayout.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.tab_round_item));
            }
        }
        view.setBackground(getResources().getDrawable(R.drawable.tab_round_item_backgroud));
    }


    private final OnClickListener mTabItemClickListener = new OnClickListener() {
        public void onClick(View view) {
            onSelect(view);
        }
    };

    private final OnFocusChangeListener mTabItemFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                onSelect(view);
            } else {

            }
        }
    };


    public TvTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTabLayout = new RatioFocusLinearLayout(context, attrs);
        mTabLayout.setGravity(Gravity.CENTER);
        mTabLayout.setClipToPadding(false);
        mTabLayout.setClipChildren(false);
        addView(mTabLayout, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }


    @Override
    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {

        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                child.requestFocus();
                //animateToTab(item);
            } else {

            }
        }


    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                //smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }


    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mPageChangeListener = onPageChangeListener;
    }

    @Override
    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        TvTabPagerIndicatorPageAdapterInterface indicatorAdapter = null;

        if (adapter instanceof TvTabPagerIndicatorPageAdapterInterface) {
            indicatorAdapter = (TvTabPagerIndicatorPageAdapterInterface) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            if (indicatorAdapter != null) {
                addTab(i, indicatorAdapter.getTabView(i, null, mTabLayout));
            } else {
                addTab(i, title, -1);
            }
        }

        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }


    private class TabView extends TvTextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null);
            setGravity(Gravity.CENTER);
            setFocusable(true);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }

    private void addTab(int index, View view) {
        if (view == null)
            throw new IllegalStateException("TabView does not have view instance.");
        view.setId(index);
        view.setOnClickListener(mTabItemClickListener);
        view.setOnFocusChangeListener(mTabItemFocusChangeListener);
        if (view.getParent() == null) {
            mTabLayout.addView(view);
        }

    }

    //addTab
    private void addTab(int index, CharSequence text, int iconResId) {
        final TabView tabView = new TabView(getContext());
        tabView.setId(index);
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabItemClickListener);
        tabView.setOnFocusChangeListener(mTabItemFocusChangeListener);
        tabView.setText(text);

        if (iconResId != 0) {
            tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        }

        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        //        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            Log.d("TvTabPageIndicator", "onMeasure");

            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        //        Log.d("TvTabPageIndicator","onPageSelected:"+position);

        setCurrentItem(position);
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrollStateChanged(state);
        }
    }


}
