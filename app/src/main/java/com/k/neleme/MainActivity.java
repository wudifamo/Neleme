package com.k.neleme;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.k.neleme.utils.ViewUtils;


public class MainActivity extends AppCompatActivity {

	private Context mContext = this;
	private SimpleDraweeView iv_shop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ViewUtils.getBlurFresco(mContext, (SimpleDraweeView) findViewById(R.id.iv_shop_bg), "res:///" + R.drawable.icon_shop);
		iv_shop = (SimpleDraweeView) findViewById(R.id.iv_shop);
		ViewUtils.getFrescoController(mContext, iv_shop, "res:///" + R.drawable.icon_shop, 40, 40);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
