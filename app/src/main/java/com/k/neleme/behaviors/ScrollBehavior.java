package com.k.neleme.behaviors;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

public class ScrollBehavior extends AppBarLayout.ScrollingViewBehavior {

	private WeakReference<View> dependentView;

	public ScrollBehavior() {
		super();
	}

	public ScrollBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
		super.onStopNestedScroll(coordinatorLayout, child, target);
	}


}
