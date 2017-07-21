package com.k.neleme.utils;


import android.content.Context;

import com.k.neleme.bean.FoodBean;
import com.k.neleme.bean.TypeBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseUtils {
	public static List<TypeBean> getTypes() {
		ArrayList<TypeBean> tList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			TypeBean typeBean = new TypeBean();
			typeBean.setName("类别" + i);
			tList.add(typeBean);
		}
		return tList;
	}

	public static List<FoodBean> getDatas(Context context) {
		ArrayList<FoodBean> fList = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("######0.0");
		for (int i = 0; i < 100; i++) {
			FoodBean foodBean = new FoodBean();
			foodBean.setId(i);
			foodBean.setName("食品--" + i + 1);
			foodBean.setPrice(Double.valueOf(df.format(new Random().nextDouble() * 100)));
			foodBean.setSale("月售" + new Random().nextInt(100));
			foodBean.setType("类别" + i / 10);
			int resID = context.getResources().getIdentifier("food" + new Random().nextInt(8), "drawable", "com.k.neleme");
			foodBean.setIcon(resID);
			fList.add(foodBean);
		}
		return fList;
	}
}
