package com.k.neleme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.k.neleme.Views.AddWidget;
import com.k.neleme.fragments.FirstFragment;
import com.k.neleme.utils.ViewUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;


public class MainActivity extends AppCompatActivity implements AddWidget.OnAddClick {

	private Context mContext = this;
	private ImageView iv_shop_car;
	private int shopWidth;
	private int[] carLoc;
	private CoordinatorLayout rootview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		rootview = (CoordinatorLayout) findViewById(R.id.rootview);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ViewUtils.getBlurFresco(mContext, (SimpleDraweeView) findViewById(R.id.iv_shop_bg), "res:///" + R.drawable.icon_shop);
		SimpleDraweeView iv_shop = (SimpleDraweeView) findViewById(R.id.iv_shop);
		ViewUtils.getFrescoController(mContext, iv_shop, "res:///" + R.drawable.icon_shop, 40, 40);
		initViewpager();
		initRecyclerView();
		initShopCar();
	}

	private void initShopCar() {
		iv_shop_car = (ImageView) findViewById(R.id.iv_shop_car);
	}

	private void initViewpager() {
		ScrollIndicatorView mSv = (ScrollIndicatorView) findViewById(R.id.indicator);
		ColorBar colorBar = new ColorBar(mContext, ContextCompat.getColor(mContext, R.color.dicator_line_blue), 6,
				ScrollBar.Gravity.BOTTOM);
		colorBar.setWidth(ViewUtils.dip2px(mContext, 30));
		mSv.setScrollBar(colorBar);
		mSv.setSplitAuto(false);
		mSv.setOnTransitionListener(new OnTransitionTextListener().setColor(ContextCompat.getColor(mContext, R.color.dicator_line_blue),
				ContextCompat.getColor(mContext, R.color.black)));
		ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
		IndicatorViewPager indicatorViewPager = new IndicatorViewPager(mSv, mViewPager);
		ViewpagerAdapter myAdapter = new ViewpagerAdapter(getSupportFragmentManager());
		indicatorViewPager.setAdapter(myAdapter);
	}

	private class ViewpagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
		private LayoutInflater inflater;
		private int padding;

		ViewpagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			inflater = LayoutInflater.from(mContext);
			padding = ViewUtils.dip2px(mContext, 20);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public View getViewForTab(int position, View convertView, ViewGroup container) {
			convertView = inflater.inflate(R.layout.item_textview, container, false);
			TextView textView = (TextView) convertView;
			textView.setText("商品"); //名称
			textView.setPadding(padding, 0, padding, 0);
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			return new FirstFragment();
		}
	}

	private void initRecyclerView() {
	}


	@Override
	public void onAddClick(View view) {
		int[] addLoc = new int[2];
		view.getLocationInWindow(addLoc);
		if (shopWidth == 0) {
			carLoc = new int[2];
			shopWidth = iv_shop_car.getWidth() / 2;
			iv_shop_car.getLocationInWindow(carLoc);
			carLoc[0] = carLoc[0] + shopWidth - view.getWidth() / 2;
		}

		Path path = new Path();
		path.moveTo(addLoc[0], addLoc[1]);
		path.quadTo(addLoc[0] - 200, addLoc[1] - 200, carLoc[0], carLoc[1]);

		final TextView textView = new TextView(mContext);
		textView.setBackgroundResource(R.drawable.circle_blue);
		textView.setText("1");
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(view.getWidth(), view.getHeight());
		rootview.addView(textView, lp);
		ViewAnimator.animate(textView).path(path).accelerate().duration(400).onStop(new AnimationListener.Stop() {
			@Override
			public void onStop() {
				rootview.removeView(textView);
			}
		}).start();
	}


	public void expendCut(View view) {
		LinearLayout ll_cut = (LinearLayout) findViewById(R.id.ll_cut);
		TransitionManager.beginDelayedTransition(rootview);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ll_cut.getLayoutParams();
		if (ll_cut.getHeight() < 400) {
			params.height = 400;
		} else {
			params.height = 153;
		}
		ll_cut.setLayoutParams(params);
	}
}
