package com.k.neleme.detail;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.k.neleme.R;

class DetailHeaderBehavior extends AppBarLayout.Behavior implements AppBarLayout.OnOffsetChangedListener, GestureDetector.OnGestureListener {
	public boolean canDrag;
	private CoordinatorLayout mParent;
	private AppBarLayout dhv;
	private float lastY, doff;
	private Toolbar toolbar;
	private Context mContext;
	private TextView toolbar_title, tv_close;
	private int scaleOffset = -1, scaleWidth, initOffset, closeOffset, initWidth;
	private int pWidth, offSet;
	private MotionEvent mov;
	private View detail_recyclerView, ic_close, car_fl;
	private GestureDetector mGestureDetector;
	private ViewAnimator finishAnim, resetAnim, carAnim;
	private static final int TOP_CHILD_FLING_THRESHOLD = 3;
	private boolean isPositive;

	public DetailHeaderBehavior() {
		super();
	}

	public DetailHeaderBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setDragCallback(new DragCallback() {
			@Override
			public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
				return true;
			}
		});
	}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
		mParent = parent;
		mGestureDetector = new GestureDetector(this);
		return super.layoutDependsOn(parent, child, dependency);
	}

	@Override
	public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
		boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
		if (toolbar == null) {
			detail_recyclerView = parent.findViewById(R.id.detail_recyclerView);
			car_fl = parent.findViewById(R.id.car_mainfl);
			car_fl.setTranslationY(mContext.getResources().getDimension(R.dimen.shopcar_height));
			tv_close = parent.findViewById(R.id.tv_close);
			ic_close = parent.findViewById(R.id.ic_close);
			toolbar = abl.findViewById(R.id.toolbar);
			toolbar_title = abl.findViewById(R.id.toolbar_title);
			toolbar_title.setTranslationY(abl.getTotalScrollRange());
			dhv = abl;
			dhv.addOnOffsetChangedListener(this);
			CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dhv.getLayoutParams();
			int pHeight = parent.getHeight();
			pWidth = parent.getWidth();
			int mHeight = dhv.getHeight();
			initWidth = pWidth - 200;
			scaleWidth = initWidth;
			initOffset = (pHeight - mHeight) / 2;
			closeOffset = initOffset + 100;
			scaleOffset = initOffset;
			doff = (float) scaleOffset / (pWidth - scaleWidth);
			lp.width = scaleWidth;
			dhv.setLayoutParams(lp);
		}
		float alpha = 1 - (scaleOffset / (float) initOffset);
		detail_recyclerView.setAlpha(alpha);
		ic_close.setAlpha(alpha);
		if (scaleOffset > initOffset) {
			tv_close.setAlpha((scaleOffset - initOffset) / (float) (closeOffset - initOffset));
			if (scaleOffset >= closeOffset) {
				tv_close.setTranslationY(closeOffset - initOffset);
				tv_close.setText("释放关闭");
			} else {
				tv_close.setText("下滑关闭");
				tv_close.setTranslationY(scaleOffset - initOffset);
			}
		}
		dhv.offsetTopAndBottom(scaleOffset);
		return handled;
	}

	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
		return super.onDependentViewChanged(parent, child, dependency);
	}

	@Override
	public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
		judgeDrag();
		if (!canDrag) {
			if (ev.getRawX() < dhv.getLeft() || ev.getRawX() > dhv.getRight() || ev.getRawY() < dhv.getTop() || ev.getRawY() > dhv.getBottom()) {
				((Activity) mContext).finish();
			} else {
				mGestureDetector.onTouchEvent(ev);
			}
		} else {
			lastY = ev.getRawY();
		}
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mGestureDetector.onTouchEvent(ev);
				break;
			case MotionEvent.ACTION_UP:
				onActionUp(ev);
				mGestureDetector.onTouchEvent(ev);
				break;
		}
		return canDrag && super.onInterceptTouchEvent(parent, child, ev);
	}

	@Override
	public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
		judgeDrag();
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mGestureDetector.onTouchEvent(ev);
				break;
			case MotionEvent.ACTION_MOVE:
				if (mov != null) {
					mov.recycle();
					mov = null;
				}
				if (!canDrag || canDrag && offSet == 0 && ev.getRawY() - lastY > 0) {
					mGestureDetector.onTouchEvent(ev);
					return false;
				}
				lastY = ev.getRawY();
				return super.onTouchEvent(parent, child, ev);
			case MotionEvent.ACTION_UP:
				onActionUp(ev);
				break;
		}
		return super.onTouchEvent(parent, child, ev);
	}

	private int animState = 0;//0 normal 1 uping  2 downing

	private void judgeDrag() {
		canDrag = dhv.getWidth() == pWidth;
		if (canDrag && car_fl.getTranslationY() > 0 && animState != 1) {
			if (carAnim != null && animState == 2) {
				carAnim.cancel();
			}
			animState = 1;
			carAnim = ViewAnimator.animate(car_fl).onStop(new AnimationListener.Stop() {
				@Override
				public void onStop() {
					animState = 0;
				}
			}).translationY(car_fl.getTranslationY(), 0).duration(200).start();
		} else if (!canDrag && car_fl.getTranslationY() < car_fl.getHeight() && animState != 2) {
			if (carAnim != null && animState == 1) {
				carAnim.cancel();
			}
			animState = 2;
			carAnim = ViewAnimator.animate(car_fl).onStop(new AnimationListener.Stop() {
				@Override
				public void onStop() {
					animState = 0;
				}
			}).translationY(car_fl.getTranslationY(), car_fl.getHeight()).duration(200).start();
		}
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		offSet = verticalOffset;
		if (dhv == null) {
			return;
		}
		if (dhv.getTotalScrollRange() + verticalOffset < 100) {
			toolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
		} else {
			toolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
		}
		toolbar_title.setTranslationY(dhv.getTotalScrollRange() + verticalOffset);
	}

	private void setLP() {
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dhv.getLayoutParams();
		lp.width = scaleWidth;
		dhv.setLayoutParams(lp);
	}

	@Override
	public boolean onDown(MotionEvent motionEvent) {
		lastY = motionEvent.getRawY();
		return false;
	}

	@Override
	public void onShowPress(MotionEvent motionEvent) {

	}

	private void onActionUp(MotionEvent motionEvent) {
		if (scaleOffset > initOffset) {
			if (scaleOffset > closeOffset) {
				finishAnim();
			} else {
				resetAnim(initWidth);
				tv_close.setAlpha(0);
			}
		} else if (scaleOffset < initOffset) {
			resetAnim(dy > 0 ? initWidth : pWidth);
		} else {
			resetAnim(scaleOffset > initOffset / 2 ? initWidth : pWidth);
		}
		lastY = motionEvent.getRawY();
	}

	@Override
	public boolean onSingleTapUp(MotionEvent motionEvent) {
		return false;
	}

	int dy;

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		dy = (int) (motionEvent1.getRawY() - lastY);
		scaleWidth = dhv.getWidth() - dy;
		if (offSet == 0) {
			if (dy > 0) {//向下划
				if (scaleWidth < pWidth - 200) {
					scaleWidth = pWidth - 200;
					scaleOffset += dy / 4;
				} else {
					scaleOffset = (int) ((pWidth - scaleWidth) * doff);
				}
				setLP();
			} else if (dy < 0) {
				if (scaleOffset > initOffset) {
					scaleOffset += dy;
					scaleWidth = dhv.getWidth();
				} else {
					scaleOffset = (int) ((pWidth - scaleWidth) * doff);
					scaleWidth = scaleWidth > pWidth ? pWidth : scaleWidth;
				}
				setLP();
				if (scaleWidth == pWidth) {
					if (mov == null) {
						mov = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, motionEvent1.getRawX(), motionEvent1.getRawY(), 0);
						super.onInterceptTouchEvent(mParent, dhv, mov);
					}
				}
			}
		}
		lastY = motionEvent1.getRawY();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		if (v1 > 1000) {
			if (scaleOffset > initOffset && scaleOffset < closeOffset) {
				finishAnim();
			}
		} else if (v1 < 1000) {

		}
		return false;
	}

	private void resetAnim(int end) {
		if (canDrag) {
			return;
		}
		if (resetAnim != null) {
			resetAnim.cancel();
		}
		resetAnim = ViewAnimator.animate(dhv).width(dhv.getWidth(), end).custom(new AnimationListener.Update() {
			@Override
			public void update(View view, float value) {
				scaleOffset = (int) ((pWidth - value) * doff);
			}
		}, dhv.getWidth(), end).decelerate().duration(200).start();
	}

	private void finishAnim() {
		if (finishAnim != null) {
			return;
		}
		if (resetAnim != null) {
			resetAnim.cancel();
		}
		finishAnim = ViewAnimator.animate(dhv).translationY(0, 2000).duration(200).onStop(new AnimationListener.Stop() {
			@Override
			public void onStop() {
				((Activity) mContext).finish();
			}
		}).start();

	}

	@Override
	public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
		if (target.getId() == R.id.car_recyclerview) {
			return;
		}
		super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
		isPositive = dy > 0;
	}

	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int
			dxUnconsumed, int dyUnconsumed) {
		if (target.getId() == R.id.car_recyclerview) {
			return;
		}
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
	}

	@Override
	public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean
			consumed) {
		if (target.getId() == R.id.car_recyclerview) {
			return true;
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

	public void setDragable(final boolean canDrag) {
		setDragCallback(new DragCallback() {
			@Override
			public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
				return canDrag;
			}
		});
	}
}
