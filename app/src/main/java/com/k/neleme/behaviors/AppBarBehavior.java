package com.k.neleme.behaviors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.florent37.viewanimator.ViewAnimator;
import com.k.neleme.R;
import com.k.neleme.utils.ViewUtils;

/**
 * Created by Administrator on 2016/12/19.
 */
public final class AppBarBehavior extends AppBarLayout.Behavior implements AppBarLayout.OnOffsetChangedListener {

	private static final int TOP_CHILD_FLING_THRESHOLD = 3;
	private boolean isPositive, fromFling;
	private float currentOffset, mTotalRange;
	public static int cutExpHeight = -1;
	public static int cutMaxHeight = -1;
	private View scroll_container;
	private Context mContext;

	public AppBarBehavior() {
	}

	public AppBarBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setDragCallback(new DragCallback() {
			@Override
			public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
				return false;
			}
		});
	}

	@Override
	public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
		boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
		// 需要在调用过super.onLayoutChild()方法之后获取
		if (cutExpHeight <= 0) {
			View ll_cut = parent.findViewById(R.id.ll_cut);
			cutExpHeight = (int) (ll_cut.getHeight() - mContext.getResources().getDimension(R.dimen.cut_margin));
			cutMaxHeight = ViewUtils.dip2px(mContext, 30) + cutExpHeight;
			scroll_container = parent.findViewById(R.id.scroll_container);
			mTotalRange = abl.getTotalScrollRange();

			abl.addOnOffsetChangedListener(this);
		}
		return handled;

	}

	@Override
	public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
		isPositive = dy > 0;
		Log.i("---", "preScroll->" + dy);
		float cty = scroll_container.getTranslationY();
		if (dy > 0 && cty > 0 && currentOffset == 0) {//向上
			cty -= dy;
			scroll_container.setTranslationY(cty);
			consumed[1] = dy;
		} else {
			super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
		}
	}

	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int
			dxUnconsumed, int dyUnconsumed) {
		Log.i("---", "dyConsumed->" + dyConsumed + "   dyUnconsumed->" + dyUnconsumed);
		if (dyConsumed == 0 && dyUnconsumed < 0 && currentOffset == 0) {//向下
			float cty = scroll_container.getTranslationY();
			cty = cty - dyUnconsumed > cutMaxHeight ? cutMaxHeight : cty - dyUnconsumed;
			scroll_container.setTranslationY(cty);
		}
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
	}

	@Override
	public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean
			consumed) {
		if (velocityY > 0) {
			fromFling = true;
		}
		if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
			velocityY = velocityY * -1;
		}
		if (target instanceof RecyclerView && velocityY < 0) {
			final RecyclerView recyclerView = (RecyclerView) target;
			final View firstChild = recyclerView.getChildAt(0);
			final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
			consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
		}
		return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
	}

	@Override
	public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
		return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
	}

	@Override
	public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
		super.onStopNestedScroll(coordinatorLayout, abl, target);
		float cty = scroll_container.getTranslationY();
		if (cty > 0) {
			int s = 0;
			if (!fromFling && cty >= cutExpHeight) {
				s = cutExpHeight;
			}
			fromFling = false;
			ViewAnimator.animate(scroll_container)
					.translationY(cty, s)
					.duration(100)
					.start();
		}
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		currentOffset = verticalOffset;
	}

}
