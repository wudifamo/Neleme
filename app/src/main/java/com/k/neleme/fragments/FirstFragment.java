package com.k.neleme.fragments;


import android.os.Bundle;

import com.k.neleme.MainActivity;
import com.k.neleme.R;
import com.k.neleme.Views.ListContainer;
import com.shizhefei.fragment.LazyFragment;

public class FirstFragment extends LazyFragment {

	public ListContainer listContainer;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_first);
		listContainer = (ListContainer) findViewById(R.id.listcontainer);
		listContainer.setAddClick((MainActivity) getActivity());

	}
}
