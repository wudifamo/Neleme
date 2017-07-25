package com.k.neleme.adapters;


import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.k.neleme.R;
import com.k.neleme.bean.TypeBean;

import java.util.List;

public class TypeAdapter extends BaseQuickAdapter<TypeBean, BaseViewHolder> {
	private int checked;
	private List<TypeBean> data;
	public boolean fromClick;
	private String typeStr;
	private int childHeight;

	public TypeAdapter(@Nullable List<TypeBean> data) {
		super(R.layout.item_type, data);
		this.data = data;
	}


	@Override
	protected void convert(BaseViewHolder helper, TypeBean item) {
		helper.setText(R.id.tv_name, item.getName())
				.setTag(R.id.item_main, item.getName());
		if (helper.getAdapterPosition() == checked) {
			helper.setBackgroundColor(R.id.item_main, Color.WHITE)
					.setTextColor(R.id.tv_name, Color.BLACK)
					.setTypeface(R.id.tv_name, Typeface.DEFAULT_BOLD)
			;
			if (childHeight == 0) {
				childHeight = helper.getConvertView().getHeight();
			}
		} else {
			helper.setBackgroundColor(R.id.item_main, ContextCompat.getColor(mContext, R.color.type_gray))
					.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.type_normal))
					.setTypeface(R.id.tv_name, Typeface.DEFAULT)
			;
		}
	}

	public void setChecked(int checked) {
		this.checked = checked;
		typeStr = data.get(checked).getName();
		notifyDataSetChanged();
	}

	public void setType(String type) {
		if (fromClick) {
			fromClick = !type.equals(typeStr);
			return;
		}
		if (type.equals(typeStr)) {
			return;
		}
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getName().equals(type) && i != checked) {
				setChecked(i);
				moveToPosition(i);
				break;
			}
		}
	}

	private void moveToPosition(int i) {
		//先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
		LinearLayoutManager linlm = (LinearLayoutManager) getRecyclerView().getLayoutManager();
		int firstItem = linlm.findFirstVisibleItemPosition();
		int lastItem = linlm.findLastVisibleItemPosition();
		//然后区分情况
		if (i <= firstItem) {
			//当要置顶的项在当前显示的第一个项的前面时
			getRecyclerView().scrollToPosition(i);
		} else if (i <= lastItem) {
			//当要置顶的项已经在屏幕上显示时
			int top = getRecyclerView().getChildAt(i - firstItem).getTop();
			getRecyclerView().scrollBy(0, top);
		} else {
			//当要置顶的项在当前显示的最后一项的后面时
			getRecyclerView().scrollToPosition(i);
			//这里这个变量是用在RecyclerView滚动监听里面的
		}

		int n = i - linlm.findFirstVisibleItemPosition();
		if (0 <= n && n < getRecyclerView().getChildCount()) {
			//获取要置顶的项顶部离RecyclerView顶部的距离
			int top = getRecyclerView().getChildAt(n).getTop();
			//最后的移动
			getRecyclerView().smoothScrollBy(0, top);
		}
	}

}
