package com.k.neleme.adapters;


import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.k.neleme.R;
import com.k.neleme.bean.TypeBean;

import java.util.List;

public class TypeAdapter extends BaseQuickAdapter<TypeBean, BaseViewHolder> {
	private int checked;

	public TypeAdapter(@Nullable List<TypeBean> data) {
		super(R.layout.item_type, data);
	}


	@Override
	protected void convert(BaseViewHolder helper, TypeBean item) {
		helper.setText(R.id.tv_name, item.getName())
				.setTag(R.id.item_main, item.getName());
		if (helper.getAdapterPosition() == checked) {
			helper.setBackgroundColor(R.id.item_main, Color.WHITE)
					.setTextColor(R.id.tv_name, Color.BLACK)
			;
		} else {
			helper.setBackgroundColor(R.id.item_main, ContextCompat.getColor(mContext, R.color.type_gray))
					.setTextColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.type_normal))
			;
		}
	}

	public void setChecked(int checked) {
		this.checked = checked;
		notifyDataSetChanged();
	}

}
