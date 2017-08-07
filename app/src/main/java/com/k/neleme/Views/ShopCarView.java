package com.k.neleme.Views;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.k.neleme.R;
import com.k.neleme.utils.ViewUtils;

import java.math.BigDecimal;

import static com.k.neleme.MainActivity.carAdapter;

public class ShopCarView extends FrameLayout {
	private TextView car_limit, tv_amount;
	public ImageView iv_shop_car;
	public TextView car_badge;
	private BottomSheetBehavior behavior;
	public boolean sheetScrolling;
	public View shoprl;
	public int[] carLoc;

	public void setBehavior(final BottomSheetBehavior behavior) {
		this.behavior = behavior;
	}

	public void setBehavior(final BottomSheetBehavior behavior, final View blackView) {
		this.behavior = behavior;
		behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int newState) {
				sheetScrolling = false;
				if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
					blackView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) {
				sheetScrolling = true;
				blackView.setVisibility(View.VISIBLE);
				ViewCompat.setAlpha(blackView, slideOffset);
			}
		});
		blackView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
				return true;
			}
		});
	}

	public ShopCarView(@NonNull Context context) {
		super(context);
	}

	public ShopCarView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (iv_shop_car == null) {
			iv_shop_car = findViewById(R.id.iv_shop_car);
			car_badge = findViewById(R.id.car_badge);
			car_limit = findViewById(R.id.car_limit);
			tv_amount = findViewById(R.id.tv_amount);
			shoprl = findViewById(R.id.car_rl);
			shoprl.setOnClickListener(new toggleCar());
			carLoc = new int[2];
			iv_shop_car.getLocationInWindow(carLoc);
			carLoc[0] = carLoc[0] + iv_shop_car.getWidth() / 2 - ViewUtils.dip2px(getContext(), 10);
		}
	}

	public void updateAmount(BigDecimal amount) {
		if (amount.compareTo(new BigDecimal(0.0)) == 0) {
			car_limit.setText("¥20 起送");
			car_limit.setTextColor(Color.parseColor("#a8a8a8"));
			car_limit.setBackgroundColor(Color.parseColor("#535353"));
			findViewById(R.id.car_nonselect).setVisibility(View.VISIBLE);
			findViewById(R.id.amount_container).setVisibility(View.GONE);
			iv_shop_car.setImageResource(R.drawable.shop_car_empty);
			behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		} else if (amount.compareTo(new BigDecimal(20.0)) < 0) {
			car_limit.setText("还差 ¥" + new BigDecimal(20.0).subtract(amount) + " 起送");
			car_limit.setTextColor(Color.parseColor("#a8a8a8"));
			car_limit.setBackgroundColor(Color.parseColor("#535353"));
			findViewById(R.id.car_nonselect).setVisibility(View.GONE);
			findViewById(R.id.amount_container).setVisibility(View.VISIBLE);
			iv_shop_car.setImageResource(R.drawable.shop_car);
		} else {
			car_limit.setText("     去结算     ");
			car_limit.setTextColor(Color.WHITE);
			car_limit.setBackgroundColor(Color.parseColor("#59d178"));
			findViewById(R.id.car_nonselect).setVisibility(View.GONE);
			findViewById(R.id.amount_container).setVisibility(View.VISIBLE);
			iv_shop_car.setImageResource(R.drawable.shop_car);
		}
		tv_amount.setText("¥" + amount);
	}

	public void showBadge(int total) {
		if (total > 0) {
			car_badge.setVisibility(View.VISIBLE);
			car_badge.setText(total + "");
		} else {
			car_badge.setVisibility(View.INVISIBLE);
		}
	}

	private class toggleCar implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (sheetScrolling) {
				return;
			}
			if (carAdapter.getItemCount() == 0) {
				return;
			}
			if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
				behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
			} else {
				behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
			}
		}
	}

}
