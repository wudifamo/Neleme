package com.k.neleme.behaviors;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.k.neleme.Views.ShopInfoContainer;

public class ShopContainerBehavior extends CoordinatorLayout.Behavior<ShopInfoContainer> {

	private Context mContext;
	private int startHeight, startWidth;
	private int totalRange;
	private float startX, startY;
	private int statusBarHeight;
	private float dx, dy;

	public ShopContainerBehavior() {
		super();
	}

	public ShopContainerBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, ShopInfoContainer child, View dependency) {
		return dependency instanceof AppBarLayout;
	}

	public boolean onDependentViewChanged(CoordinatorLayout parent, ShopInfoContainer child, View dependency) {
		if (dependency instanceof AppBarLayout) {
			AppBarLayout appBarLayout = (AppBarLayout) dependency;
			if (startHeight > 0) {
				// 图片大小
				float dTop = dependency.getTop();
				float dt = dTop / totalRange;
				child.setX(startX + dx * dt);
				child.setY(startY + dy * dt);
				child.setWgAlpha(1 - Math.abs(dTop / 80f));
			} else {
				totalRange = appBarLayout.getTotalScrollRange();
				shouldInitProperties(child);
			}
		}

		return true;
	}


	private void shouldInitProperties(ShopInfoContainer child) {
		startHeight = child.getHeight();
		startWidth = child.getWidth();
		startX = child.getX();
		startY = child.getY();
		statusBarHeight = com.k.neleme.utils.ViewUtils.getStatusBarHeight(mContext);
		int acBarHeight = com.k.neleme.utils.ViewUtils.dip2px(mContext, 56);
		dx = startX - acBarHeight - 8;
		dy = -(statusBarHeight + acBarHeight / 2 - child.shop_name.getHeight() / 2 - startY);

	}
}
