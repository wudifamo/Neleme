package com.k.neleme.Views;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.k.neleme.R;

public class ShopInfoContainer extends RelativeLayout {

	public TextView shop_name, shop_arrow, shop_sum, shop_type, shop_send;

	public ShopInfoContainer(Context context) {
		super(context);
	}

	public ShopInfoContainer(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.view_shopinfo, this);
		shop_name = (TextView) findViewById(R.id.tv_shop_name);
		shop_arrow = (TextView) findViewById(R.id.tv_shop_arrow);
		shop_sum = (TextView) findViewById(R.id.tv_shop_summary);
		shop_type = (TextView) findViewById(R.id.tv_shop_type);
		shop_send = (TextView) findViewById(R.id.tv_shop_send);
	}


	public void setWgAlpha(float alpha) {
		shop_sum.setAlpha(alpha);
		shop_arrow.setAlpha(alpha);
		shop_sum.setAlpha(alpha);
		shop_type.setAlpha(alpha);
		shop_send.setAlpha(alpha);
	}
}
