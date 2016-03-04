package com.example.wb_lijinweia.mockmicalendar.micalendar;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

@CoordinatorLayout.DefaultBehavior(MonthPager.Behavior.class)
public class MonthPager extends ViewPager {
    private int selectedIndex;

    public MonthPager(Context context) {
        this(context, null);
    }

    public MonthPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * @return 返回应该预留的高度
     * 日历一行是7个元素，先计算行数，如果是第0天，则计算出来为0，返回0；如果是第8天，计算结果是1，返回100,
     */
    public int getTopMovableDistance() {
        int rowCount = selectedIndex / 7;
        return getHeight() / 6 * rowCount;
    }

    /**
     * @return 返回最大的移动距离
     * 如果控件高度为600则最多移动的距离是500，因为至少要留一行的距离 (600 / 6 * 5)
     */
    public int getWholeMovableDistance() {
        return getHeight() / 6 * 5; //getHeight为本控件的高度
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
*/
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static class Behavior extends CoordinatorLayout.Behavior<MonthPager> {
        private int mTop;

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, MonthPager child, View dependency) {
            return dependency instanceof RecyclerView;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, MonthPager child, int layoutDirection) {
            parent.onLayoutChild(child, layoutDirection);
            child.offsetTopAndBottom(mTop);
            return true;
        }

        private int dependentViewTop = -1;

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, MonthPager child, View dependency) {
            if (dependentViewTop != -1) {
                int dy = dependency.getTop() - dependentViewTop;    //dependency对其依赖的view(本例依赖的view是RecycleView)
                int top = child.getTop();
                if (dy > -top){
                    dy = -top;
                }

                if (dy < -top - child.getTopMovableDistance()){
                    dy = -top - child.getTopMovableDistance();
                }

                child.offsetTopAndBottom(dy);
            }
            dependentViewTop = dependency.getTop(); //dependency
            mTop = child.getTop();
            return true;
        }
    }

}
