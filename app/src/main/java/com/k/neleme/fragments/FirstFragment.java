package com.k.neleme.fragments;


import android.os.Bundle;

import com.k.neleme.MainActivity;
import com.k.neleme.R;
import com.k.neleme.Views.ListContainer;
import com.k.neleme.adapters.FoodAdapter;
import com.k.neleme.adapters.TypeAdapter;
import com.shizhefei.fragment.LazyFragment;

public class FirstFragment extends LazyFragment {

	private ListContainer listContainer;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_first);
		listContainer = (ListContainer) findViewById(R.id.listcontainer);
		listContainer.setAddClick((MainActivity) getActivity());
	}

	public FoodAdapter getFoodAdapter() {
		return listContainer.foodAdapter;
	}

	public TypeAdapter getTypeAdapter() {
		return listContainer.typeAdapter;
	}

}
