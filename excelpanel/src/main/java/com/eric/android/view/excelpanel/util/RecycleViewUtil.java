package com.eric.android.view.excelpanel.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecycleViewUtil {
    private RecyclerView mRecyclerView = null;
    private GestureDetector mGestureDetector = null;
    private RecyclerView.SimpleOnItemTouchListener mSimpleOnItemTouchListener;
    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    private Context context;

    public RecycleViewUtil(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.mRecyclerView = recyclerView;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            //滑动事件
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            //长按事件
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                if (mOnItemLongClickListener != null) {
                    View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = mRecyclerView.getChildLayoutPosition(childView);
                        mOnItemLongClickListener.onItemLongClick(position, childView);
                    }
                }
            }

            //单击事件
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemClickListener != null) {
                    View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        int position = mRecyclerView.getChildLayoutPosition(childView);
                        mOnItemClickListener.onItemClick(position, childView);
                        return true;
                    }
                }

                return super.onSingleTapUp(e);
            }
        });

        mSimpleOnItemTouchListener = new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (mGestureDetector.onTouchEvent(e)) {
                    return true;
                }
                return false;
            }
        };

        mRecyclerView.addOnItemTouchListener(mSimpleOnItemTouchListener);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        mOnItemLongClickListener = l;
    }

    //长按事件接口
    public interface OnItemLongClickListener {
        public void onItemLongClick(int position, View view);
    }

    //单击事件接口
    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }
}
