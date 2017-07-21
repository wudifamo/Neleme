package com.k.neleme.behaviors;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class ScrollBehavior extends AppBarLayout.ScrollingViewBehavior{
	public ScrollBehavior() {
		super();
	}

	public ScrollBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

		return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
	}

	@Override
	public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
		super.onStopNestedScroll(coordinatorLayout, child, target);
	}
}
