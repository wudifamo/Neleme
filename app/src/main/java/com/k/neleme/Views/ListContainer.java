package com.k.neleme.Views;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.k.neleme.R;
import com.k.neleme.adapters.FoodAdapter;
import com.k.neleme.adapters.TypeAdapter;
import com.k.neleme.bean.FoodBean;
import com.k.neleme.detail.DetailActivity;
import com.k.neleme.utils.BaseUtils;
import com.k.neleme.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ListContainer extends LinearLayout {

	public TypeAdapter typeAdapter;
	private RecyclerView recyclerView2;
	private LinearLayoutManager linearLayoutManager;
	private List<FoodBean> foodBeanList;
	private boolean move;
	private int index;
	private Context mContext;
	public FoodAdapter foodAdapter;
	public static List<FoodBean> commandList = new ArrayList<>();
	private TextView tvStickyHeaderView;
	private View stickView;

	public ListContainer(Context context) {
		super(context);
	}

	public ListContainer(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflate(mContext, R.layout.view_listcontainer, this);
		RecyclerView recyclerView1 = findViewById(R.id.recycler1);
		recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
		typeAdapter = new TypeAdapter(BaseUtils.getTypes());
		View view = new View(mContext);
		view.setMinimumHeight(ViewUtils.dip2px(mContext, 50));
		typeAdapter.addFooterView(view);
		typeAdapter.bindToRecyclerView(recyclerView1);
		recyclerView1.addItemDecoration(new SimpleDividerDecoration(mContext));
		((DefaultItemAnimator) recyclerView1.getItemAnimator()).setSupportsChangeAnimations(false);
		recyclerView1.addOnItemTouchListener(new OnItemClickListener() {
			@Override
			public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
				if (recyclerView2.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
					typeAdapter.fromClick = true;
					typeAdapter.setChecked(i);
					String type = view.getTag().toString();
					for (int ii = 0; ii < foodBeanList.size(); ii++) {
						FoodBean typeBean = foodBeanList.get(ii);
						if (typeBean.getType().equals(type)) {
							index = ii;
							moveToPosition(index);
							break;
						}
					}
				}
			}
		});
		recyclerView2 = findViewById(R.id.recycler2);
		linearLayoutManager = new LinearLayoutManager(mContext);
		recyclerView2.setLayoutManager(linearLayoutManager);
		((DefaultItemAnimator) recyclerView2.getItemAnimator()).setSupportsChangeAnimations(false);
		foodBeanList = BaseUtils.getDatas(mContext);
		commandList = BaseUtils.getDetails(foodBeanList);
		recyclerView2.addOnItemTouchListener(new OnItemClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				super.onItemChildClick(adapter, view, position);
				if (view.getId() == R.id.food_main) {
					Intent intent = new Intent(mContext, DetailActivity.class);
					intent.putExtra("food", (FoodBean) adapter.getData().get(position));
					intent.putExtra("position", position);
					mContext.startActivity(intent);
					((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				}
			}

			@Override
			public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

			}
		});
	}

	private void moveToPosition(int n) {
		//先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
		int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
		int lastItem = linearLayoutManager.findLastVisibleItemPosition();
		//然后区分情况
		if (n <= firstItem) {
			//当要置顶的项在当前显示的第一个项的前面时
			recyclerView2.scrollToPosition(n);
		} else if (n <= lastItem) {
			//当要置顶的项已经在屏幕上显示时
			int top = recyclerView2.getChildAt(n - firstItem).getTop();
			recyclerView2.scrollBy(0, top);
		} else {
			//当要置顶的项在当前显示的最后一项的后面时
			recyclerView2.scrollToPosition(n);
			//这里这个变量是用在RecyclerView滚动监听里面的
			move = true;
		}
	}


	public void setAddClick(AddWidget.OnAddClick onAddClick) {
		foodAdapter = new FoodAdapter(foodBeanList, onAddClick);
		View view = new View(mContext);
		view.setMinimumHeight(ViewUtils.dip2px(mContext, 50));
		foodAdapter.addFooterView(view);
		foodAdapter.bindToRecyclerView(recyclerView2);
		stickView = findViewById(R.id.stick_header);
		tvStickyHeaderView = findViewById(R.id.tv_header);
		tvStickyHeaderView.setText("类别0");
        recyclerView2.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                typeAdapter.fromClick = false;
                return false;
            }
        });
		recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (move) {
					move = false;
					//获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
					int n = index - linearLayoutManager.findFirstVisibleItemPosition();
					if (0 <= n && n < recyclerView.getChildCount()) {
						//获取要置顶的项顶部离RecyclerView顶部的距离
						int top = recyclerView.getChildAt(n).getTop();
						//最后的移动
						recyclerView.smoothScrollBy(0, top);
					}
				} else {
					View stickyInfoView = recyclerView.findChildViewUnder(stickView.getMeasuredWidth() / 2, 5);
					if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
						tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
						typeAdapter.setType(String.valueOf(stickyInfoView.getContentDescription()));
					}

					View transInfoView = recyclerView.findChildViewUnder(stickView.getMeasuredWidth() / 2, stickView.getMeasuredHeight
							() + 1);
					if (transInfoView != null && transInfoView.getTag() != null) {
						int transViewStatus = (int) transInfoView.getTag();
						int dealtY = transInfoView.getTop() - stickView.getMeasuredHeight();
						if (transViewStatus == FoodAdapter.HAS_STICKY_VIEW) {
							if (transInfoView.getTop() > 0) {
								stickView.setTranslationY(dealtY);
							} else {
								stickView.setTranslationY(0);
							}
						} else if (transViewStatus == FoodAdapter.NONE_STICKY_VIEW) {
							stickView.setTranslationY(0);
						}
					}
				}
			}
		});
	}
}
