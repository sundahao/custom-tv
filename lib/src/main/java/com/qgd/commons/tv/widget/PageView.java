package com.qgd.commons.tv.widget;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 作者：ethan on 2016/1/6 17:19
 * 邮箱：ethan.chen@fm2020.com
 */
public class PageView extends android.support.v4.view.ViewPager {
    private PageViewIndicatorInterface mIndicator;
    private PagingViewPageAdapter mPagerAdapter;

    private ArrayList<ViewGroup> mListViews;
    //    private int size;
    private int mTotalPage;
    private int mCurrentPage = 0;
    private PageView.CallBack mCallback;
    private int mCurrentFocus;

    public PageView(Context context) {
        super(context);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initPage(int totalPage, PageViewIndicatorInterface indicate) {
        mCurrentFocus = 0;
        mTotalPage = totalPage;
        if (totalPage < 0) {
            mTotalPage = 1;
        }
        mIndicator = indicate;
        indicate.initIndicator(totalPage);
        indicate.setSelectedPage(mCurrentPage);

    }

    public void setTotalPage(int totalPage) {
        mTotalPage = totalPage;
        mIndicator.setTotalPage(totalPage);
    }



    public void setIndicator(PageViewIndicatorInterface indicator) {
        mIndicator = indicator;
    }

    public void setPageCallBack(PageView.CallBack call) {
        mCallback = call;
        if (mListViews == null)
            mListViews = new ArrayList<ViewGroup>();
        mPagerAdapter = new PagingViewPageAdapter();
        setAdapter(mPagerAdapter);
        addOnPageChangeListener(pageChangeListener);

        ViewGroup items = mCallback.loadPageItemView(mCurrentPage);
        addPage(items);
        addPage(mCallback.loadPageItemView(mCurrentPage + 1));

        mPagerAdapter.notifyDataSetChanged();
    }

    private void setAllChildListener(ViewGroup items) {
        for (int i = 0; i < items.getChildCount(); i++) {
            items.getChildAt(i).setOnKeyListener(mOnKeyListener);
            items.getChildAt(i).setOnClickListener(mOnClickListener);
            items.getChildAt(i).setOnFocusChangeListener(mOnFocusChangeListener);
        }
    }

    private void addPage(ViewGroup items) {
        if (items != null) {
            setAllChildListener(items);
            mListViews.add(items);
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPage=position;
            if((position+1)<mTotalPage){
                if ((position + 1) >= mListViews.size()) {
                    ViewGroup items = mCallback.loadPageItemView(position+1);
                    addPage(items);
                    mPagerAdapter.notifyDataSetChanged();

                }
            }
        }

        public void onPageSelected(int position) {
            Log.d("PageView", "onPageSelected:" + position + " size:" + mListViews.size());


            if (position < mTotalPage) {
                mIndicator.setSelectedPage((position + 1));
                mListViews.get(position).requestFocus();
            }



        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    };

    //页面数据下标是0开始，显示是从1开始
    public void reloadPage(int pageNumber) {
        Log.d("PageView", "reloadPage begin:" + pageNumber + " size:" + mListViews.size());
        if (pageNumber >= 0 && pageNumber < mListViews.size()) {
            ViewGroup items = mCallback.loadPageItemView(pageNumber);
            setAllChildListener(items);
            Log.d("PageView", "reloadPage inner:" + pageNumber);
            ViewGroup oldItems = mListViews.get(pageNumber);
            if (oldItems != null) {
                oldItems.removeAllViews();
                mListViews.set(pageNumber, items);
                mPagerAdapter.notifyDataSetChanged();
                Log.d("PageView", "reloadPage end:" + pageNumber);
            }
        }
    }

    public void reloadCurrentPage() {
        reloadPage(mCurrentPage);
    }
    public void reloadNextPage() {
        reloadPage(mCurrentPage+1);
    }


    public class PagingViewPageAdapter extends PagerAdapter {

        public PagingViewPageAdapter() {// 构造函数
            // 初始化viewpager的时候给的一个页面
            if (mIndicator != null) {
                mIndicator.setSelectedPage(1);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            int index = mListViews.indexOf(object);
            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }

        public void setListViews(ArrayList<ViewGroup> listViews) {
            mListViews = listViews;

        }

        @Override
        public void restoreState(android.os.Parcelable state, ClassLoader loader) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public int getCount() {// 返回数量
            return mListViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            ((ViewGroup) container).removeView(mListViews.get(position));
             Log.d("PageView", "destroyItem:" + position + " size:" + mListViews.size());

        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public Object instantiateItem(View container, int position) {// 返回view对象
            try {
                Log.d("PageView", "instantiateItem:" + position + " size:" + mListViews.size());
                ViewGroup vg = null;
                if (mListViews.size()<position) {
                    vg = mCallback.loadPageItemView(position+1);
                    addPage(vg);
                    notifyDataSetChanged();
                } else {
                    vg = mListViews.get(position % mTotalPage);
                }
                if (vg != null) {
                    ((ViewPager) container).addView(vg, 0 );
                }
                return vg;
            } catch (Exception e) {
                Log.e("PageView", e.getMessage());
                return null;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }


    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            return mCallback.onItemOnKey(view, i, keyEvent);
        }
    };
    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            ViewGroup vg = (ViewGroup) view.getParent();
            mCallback.onItemFocusChange(view, b);
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCallback.onItemClickListener(view);
        }
    };

    public interface CallBack {
        ViewGroup loadPageItemView(int pos);

        void onItemClickListener(View view);

        void onItemLongClickListener(View view);

        void onItemFocusChange(View view, boolean b);

        boolean onItemOnKey(View view, int i, KeyEvent keyEvent);
    }

}
