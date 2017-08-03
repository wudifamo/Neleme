package com.k.neleme.adapters;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.k.neleme.R;
import com.k.neleme.Views.AddWidget;
import com.k.neleme.bean.FoodBean;

import java.math.BigDecimal;
import java.util.List;

public class CarAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
	private AddWidget.OnAddClick onAddClick;

	public CarAdapter(@Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick) {
		super(R.layout.item_car, data);
		this.onAddClick = onAddClick;
	}

	@Override
	protected void convert(BaseViewHolder helper, FoodBean item) {
		helper.setText(R.id.car_name, item.getName())
				.setText(R.id.car_price, item.getStrPrice(mContext, item.getPrice().multiply(BigDecimal.valueOf(item.getSelectCount()))))
		;
		AddWidget addWidget = helper.getView(R.id.car_addwidget);
//		addWidget.setData(this, helper.getAdapterPosition(), onAddClick);
		addWidget.setData(onAddClick,item);
	}
}
