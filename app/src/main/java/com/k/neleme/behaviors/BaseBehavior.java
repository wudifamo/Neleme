package com.k.neleme.behaviors;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

class BaseBehavior extends CoordinatorLayout.Behavior<View> {
	float startX, startY;
	WeakReference<View> dependentView;

	BaseBehavior() {
		super();
	}

	BaseBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
		if (dependency != null) {
			dependentView = new WeakReference<>(dependency);
		}
		return dependency instanceof AppBarLayout;
	}

	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
		if (startY > 0) {
			return true;
		} else {
			shouldInitProperties(child);
			return false;
		}
	}

	private void shouldInitProperties(View child) {
		startX = child.getX();
		startY = child.getY();
	}

	@Override
	public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
		return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
	}

	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int
			dyUnconsumed) {
		if (dyUnconsumed > 0) {
			return;
		}
		View dependentView = getDependentView();
		float newTranslateY = dependentView.getTranslationY() - dyUnconsumed;
		final float maxHeaderTranslate = 0;
		if (newTranslateY < maxHeaderTranslate) {
			dependentView.setTranslationY(newTranslateY);
		}
	}

	View getDependentView() {
		return dependentView.get();
	}
}
