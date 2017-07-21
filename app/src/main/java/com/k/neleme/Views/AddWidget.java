package com.k.neleme.Views;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.k.neleme.R;

public class AddWidget extends FrameLayout {

	private View add, sub;
	private TextView tv_count;
	private int count;

	public interface OnAddClick {
		void onAddClick(View view);
	}

	private OnAddClick onAddClick;

	public void setOnAddClick(OnAddClick onAddClick) {
		this.onAddClick = onAddClick;
	}

	public AddWidget(@NonNull Context context) {
		super(context);
	}

	public AddWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.view_addwidget, this);
		add = findViewById(R.id.iv_add);
		sub = findViewById(R.id.iv_sub);
		tv_count = (TextView) findViewById(R.id.tv_count);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (count == 0) {
					ViewAnimator.animate(sub)
							.translationX(add.getLeft() - sub.getLeft(), 0)
							.rotation(360)
							.alpha(0, 255)
							.duration(200)
							.interpolator(new DecelerateInterpolator())
							.andAnimate(tv_count)
							.translationX(add.getLeft() - tv_count.getLeft(), 0)
							.rotation(360)
							.alpha(0, 255)
							.interpolator(new DecelerateInterpolator())
							.duration(200)
							.start()
					;
				}
				if (onAddClick != null) {
					onAddClick.onAddClick(v);
				}
				count++;
				tv_count.setText(count + "");
			}
		});
		sub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (count == 1) {
					ViewAnimator.animate(sub)
							.translationX(0, add.getLeft() - sub.getLeft())
							.rotation(-360)
							.alpha(255, 0)
							.duration(200)
							.interpolator(new AccelerateInterpolator())
							.andAnimate(tv_count)
							.translationX(0, add.getLeft() - tv_count.getLeft())
							.rotation(-360)
							.alpha(255, 0)
							.interpolator(new AccelerateInterpolator())
							.duration(200)
							.start()
					;
				}
				count--;
				tv_count.setText(count == 0 ? "1" : count + "");
			}
		});
	}
}
