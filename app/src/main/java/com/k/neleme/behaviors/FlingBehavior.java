package com.k.neleme.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.k.neleme.R;

/**
 * Created by Administrator on 2016/12/19.
 */
public final class FlingBehavior extends AppBarLayout.Behavior implements AppBarLayout.OnOffsetChangedListener {

	private LinearLayout ll_cut;
	private int currentOffset;
	private int cutHeight;
	private int cutMaxHeight = 500;
	private int lastY;
	private boolean why = false;

	public FlingBehavior() {
	}

	public FlingBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDragCallback(new DragCallback() {
			@Override
			public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
				return false;
			}
		});
	}

	@Override
	public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
		why = false;
		if (ll_cut == null) {
			ll_cut = (LinearLayout) child.findViewById(R.id.ll_cut);
			cutHeight = ll_cut.getHeight();
			child.addOnOffsetChangedListener(this);
		}
		return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
	}

	@Override
	public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
								  int velocityX, int velocityY, int[] consumed) {
		why = true;
		if (ll_cut != null && ((velocityY > 0 && ll_cut.getHeight() > cutHeight) || (velocityY < 0 && currentOffset == 0))) {
			if ((lastY > 0 && velocityY < 0) || (lastY < 0) && velocityY > 0) {
				lastY = velocityY;
			} else {
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ll_cut.getLayoutParams();
				int nHeight = lp.height - velocityY < cutHeight ? cutHeight : lp.height - velocityY;
				lp.height = nHeight > cutMaxHeight ? cutMaxHeight : nHeight;
				ll_cut.setLayoutParams(lp);
				consumed[1] = velocityY;
				lastY = velocityY;
			}
		} else {
			super.onNestedPreScroll(coordinatorLayout, child, target, velocityX, velocityY, consumed);
		}

	}


	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int
			dxUnconsumed, int dyUnconsumed) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
	}

	@Override
	public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
		super.onStopNestedScroll(coordinatorLayout, abl, target);
		if (why) {
			int cHeight = ll_cut.getHeight();
			if (cHeight > cutHeight) {
				TransitionManager.beginDelayedTransition(abl);
				FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll_cut.getLayoutParams();
				if (cHeight < 400) {
					params.height = cutHeight;
				}else {
					params.height = 400;
				}
				ll_cut.setLayoutParams(params);
			}
		}
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		currentOffset = verticalOffset;
	}
}
