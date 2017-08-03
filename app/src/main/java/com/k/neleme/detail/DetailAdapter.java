package com.k.neleme.detail;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.k.neleme.R;
import com.k.neleme.Views.AddWidget;
import com.k.neleme.bean.FoodBean;

import java.util.List;

class DetailAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
	private AddWidget.OnAddClick onAddClick;

	DetailAdapter(@Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick) {
		super(R.layout.item_detail, data);
		this.onAddClick = onAddClick;
	}

	@Override
	protected void convert(BaseViewHolder helper, FoodBean item) {
		helper.setText(R.id.textView6, item.getName())
				.setText(R.id.textView7, item.getSale())
				.setText(R.id.textView8, item.getStrPrice(mContext))
				.setImageResource(R.id.imageView2, item.getIcon())
		;
		AddWidget addWidget = helper.getView(R.id.detail_addwidget);
		addWidget.setData(onAddClick,item);
	}
}
