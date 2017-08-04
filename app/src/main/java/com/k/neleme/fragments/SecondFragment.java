package com.k.neleme.fragments;


import android.os.Bundle;

import com.k.neleme.R;
import com.shizhefei.fragment.LazyFragment;

public class SecondFragment extends LazyFragment{
	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.item_comment_header);
	}
}
