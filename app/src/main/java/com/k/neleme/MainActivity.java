package com.k.neleme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.k.neleme.Views.AddWidget;
import com.k.neleme.Views.ShopCarView;
import com.k.neleme.adapters.CarAdapter;
import com.k.neleme.bean.FoodBean;
import com.k.neleme.behaviors.AppBarBehavior;
import com.k.neleme.fragments.FirstFragment;
import com.k.neleme.fragments.SecondFragment;
import com.k.neleme.utils.ViewUtils;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity implements AddWidget.OnAddClick {

	public static final String CAR_ACTION = "handleCar";
	private CoordinatorLayout rootview;
	public BottomSheetBehavior behavior;
	public View scroll_container;
	private FirstFragment firstFragment;
	public static CarAdapter carAdapter;
	private ShopCarView shopCarView;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		IntentFilter intentFilter = new IntentFilter(CAR_ACTION);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (CAR_ACTION.equals(intent.getAction())) {
				FoodBean foodBean = (FoodBean) intent.getSerializableExtra("foodbean");
				int p = intent.getIntExtra("position", -1);
				if (p >= 0 && p < firstFragment.getFoodAdapter().getItemCount()) {
					FoodBean fb = firstFragment.getFoodAdapter().getItem(p);
					fb.setSelectCount(foodBean.getSelectCount());
					firstFragment.getFoodAdapter().setData(p, fb);
				} else {
					for (int i = 0; i < firstFragment.getFoodAdapter().getItemCount(); i++) {
						FoodBean fb = firstFragment.getFoodAdapter().getItem(i);
						if (fb.getId() == foodBean.getId()) {
							fb.setSelectCount(foodBean.getSelectCount());
							firstFragment.getFoodAdapter().setData(i, fb);
							break;
						}
					}
				}
				dealCar(foodBean);
			}
		}
	};

	private void initViews() {
		rootview = (CoordinatorLayout) findViewById(R.id.rootview);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initViewpager();
		initShopCar();
	}

	private void initShopCar() {
		behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
		shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
		View blackView = findViewById(R.id.blackview);
		shopCarView.setBehavior(behavior, blackView);
		RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
		carRecView.setLayoutManager(new LinearLayoutManager(mContext));
		((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
		carAdapter = new CarAdapter(new ArrayList<FoodBean>(), this);
		carAdapter.bindToRecyclerView(carRecView);
	}

	private void initViewpager() {
		scroll_container = findViewById(R.id.scroll_container);
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
		firstFragment = new FirstFragment();
		ViewpagerAdapter myAdapter = new ViewpagerAdapter(getSupportFragmentManager());
		indicatorViewPager.setAdapter(myAdapter);
	}

	private class ViewpagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
		private LayoutInflater inflater;
		private int padding;
		private String[] tabs = new String[]{"商品", "评价"};

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
			textView.setText(tabs[position]); //名称
			textView.setPadding(padding, 0, padding, 0);
			return convertView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			switch (position) {
				case 0:
					return firstFragment;
			}
			return new SecondFragment();
		}
	}

	@Override
	public void onAddClick(View view, FoodBean fb) {
		dealCar(fb);
		ViewUtils.addTvAnim(view, shopCarView.carLoc, mContext, rootview);
	}


	@Override
	public void onSubClick(FoodBean fb) {
		dealCar(fb);
	}

	private void dealCar(FoodBean foodBean) {
		HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
		BigDecimal amount = new BigDecimal(0.0);
		int total = 0;
		boolean hasFood = false;
		if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
			firstFragment.getFoodAdapter().notifyDataSetChanged();
		}
		List<FoodBean> flist = carAdapter.getData();
		int p = -1;
		for (int i = 0; i < flist.size(); i++) {
			FoodBean fb = flist.get(i);
			if (fb.getId() == foodBean.getId()) {
				fb = foodBean;
				hasFood = true;
				if (foodBean.getSelectCount() == 0) {
					p = i;
				} else {
					carAdapter.setData(i, foodBean);
				}
			}
			total += fb.getSelectCount();
			if (typeSelect.containsKey(fb.getType())) {
				typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
			} else {
				typeSelect.put(fb.getType(), fb.getSelectCount());
			}
			amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
		}
		if (p >= 0) {
			carAdapter.remove(p);
		} else if (!hasFood && foodBean.getSelectCount() > 0) {
			carAdapter.addData(foodBean);
			if (typeSelect.containsKey(foodBean.getType())) {
				typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
			} else {
				typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
			}
			amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
			total += foodBean.getSelectCount();
		}
		shopCarView.showBadge(total);
		firstFragment.getTypeAdapter().updateBadge(typeSelect);
		shopCarView.updateAmount(amount);
	}

	public void expendCut(View view) {
		float cty = scroll_container.getTranslationY();
		if (!ViewUtils.isFastClick()) {
			ViewAnimator.animate(scroll_container)
					.translationY(cty, cty == 0 ? AppBarBehavior.cutExpHeight : 0)
					.decelerate()
					.duration(100)
					.start();
		}
	}

	public void clearCar(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		TextView tv = new TextView(mContext);
		tv.setText("清空购物车?");
		tv.setTextSize(14);
		tv.setPadding(ViewUtils.dip2px(mContext, 16), ViewUtils.dip2px(mContext, 16), 0, 0);
		tv.setTextColor(Color.parseColor("#757575"));
		AlertDialog alertDialog = builder
				.setNegativeButton("取消", null)
				.setCustomTitle(tv)
				.setPositiveButton("清空", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						List<FoodBean> flist = carAdapter.getData();
						for (int i = 0; i < flist.size(); i++) {
							FoodBean fb = flist.get(i);
							fb.setSelectCount(0);
						}
						carAdapter.setNewData(new ArrayList<FoodBean>());
						firstFragment.getFoodAdapter().notifyDataSetChanged();
						shopCarView.showBadge(0);
						firstFragment.getTypeAdapter().updateBadge(new HashMap<String, Long>());
						shopCarView.updateAmount(new BigDecimal(0.0));
					}
				})
				.show();
		Button nButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		nButton.setTextColor(ContextCompat.getColor(mContext, R.color.dodgerblue));
		nButton.setTypeface(Typeface.DEFAULT_BOLD);
		Button pButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		pButton.setTextColor(ContextCompat.getColor(mContext, R.color.dodgerblue));
		pButton.setTypeface(Typeface.DEFAULT_BOLD);
	}

	public void goAccount(View view) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
