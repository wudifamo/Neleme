package com.k.neleme.behaviors;


import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.k.neleme.R;
import com.k.neleme.Views.ShopInfoContainer;

import java.lang.ref.WeakReference;

public class ShopContainerBehavior extends CoordinatorLayout.Behavior<ShopInfoContainer> {

	private Context mContext;
	private int iconHeight, iconWidth;
	private int totalRange;
	private float startX, startY, bgRange;
	private float dx, dy;
	private View name_container, iv_shop;
	private WeakReference<ShopInfoContainer> mContainer;
	private View iv_shop_bg;

	public ShopContainerBehavior() {
		super();
	}

	public ShopContainerBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	public boolean onLayoutChild(CoordinatorLayout parent, ShopInfoContainer child, int layoutDirection) {
		boolean handled = super.onLayoutChild(parent, child, layoutDirection);
		if (mContainer == null) {
			mContainer = new WeakReference<>(child);
			name_container = child.findViewById(R.id.name_container);
			iv_shop = child.findViewById(R.id.iv_shop);
			iv_shop_bg = child.findViewById(R.id.iv_shop_bg);
		}
		return handled;
	}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, ShopInfoContainer child, View dependency) {
		return dependency instanceof AppBarLayout;
	}

	public boolean onDependentViewChanged(CoordinatorLayout parent, ShopInfoContainer child, View dependency) {
		if (dependency instanceof AppBarLayout) {
			AppBarLayout appBarLayout = (AppBarLayout) dependency;
			if (iconHeight > 0) {
				// 店名
				float dTop = dependency.getTop();
				float dt = dTop / totalRange;
				name_container.setX(startX + dx * dt);
				name_container.setY(startY + dy * dt);
				child.setWgAlpha(1 + dt * 2);
				//图标
				iv_shop.setY(startY + dTop / 4);
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) iv_shop.getLayoutParams();
				lp.width = iconWidth + (int) dTop / 2;
				lp.height = iconHeight + (int) dTop / 2;
				iv_shop.setLayoutParams(lp);
				//背景
				iv_shop_bg.setTranslationY(dTop * bgRange);
			} else {
				totalRange = appBarLayout.getTotalScrollRange();
				shouldInitProperties();
			}
		}

		return true;
	}


	private void shouldInitProperties() {
		startX = name_container.getX();
		startY = name_container.getY();
		int statusBarHeight = com.k.neleme.utils.ViewUtils.getStatusBarHeight(mContext);
		int acBarHeight = com.k.neleme.utils.ViewUtils.dip2px(mContext, 56);

		dx = startX - acBarHeight - 8;
		dy = -(statusBarHeight + acBarHeight / 2 - getContainer().shop_name.getHeight() / 2 - startY);

		iconHeight = iv_shop.getHeight();
		iconWidth = iv_shop.getWidth();

		bgRange = (totalRange - acBarHeight) / (float) totalRange;
	}

	private ShopInfoContainer getContainer() {
		return mContainer.get();
	}

}
