package com.k.neleme.behaviors;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

public class ShopIconBehavior extends CoordinatorLayout.Behavior<SimpleDraweeView> {

	private int startHeight, startWidth;
	private float startX, startY;

	public ShopIconBehavior() {
		super();
	}

	public ShopIconBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, SimpleDraweeView child, View dependency) {

		return dependency instanceof AppBarLayout;
	}

	public boolean onDependentViewChanged(CoordinatorLayout parent, SimpleDraweeView child, View dependency) {

		if (startHeight > 0) {
			// 图片大小
			int dTop = dependency.getTop();
//			child.setX(startX + dTop / 4);
			child.setY(startY + dTop / 4);
			CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
			lp.width = startWidth + dTop / 2;
			lp.height = startHeight + dTop / 2;
			child.setLayoutParams(lp);
			child.setAlpha(1 - Math.abs((float) dTop / 80f));
		} else {
			shouldInitProperties(child);
		}

		return true;
	}


	private void shouldInitProperties(SimpleDraweeView child) {
		if (startHeight == 0) {
			startHeight = child.getHeight();
			startWidth = child.getWidth();
			startX = child.getX();
			startY = child.getY();
		}
	}
}
