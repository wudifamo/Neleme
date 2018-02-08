package com.k.neleme.utils;


import android.content.Context;

import com.k.neleme.bean.CommentBean;
import com.k.neleme.bean.FoodBean;
import com.k.neleme.bean.TypeBean;

import java.math.BigDecimal;
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
		for (int i = 0; i < 91; i++) {
			FoodBean foodBean = new FoodBean();
			foodBean.setId(i);
			foodBean.setName("食品--" + i + 1);
			foodBean.setPrice(BigDecimal.valueOf((new Random().nextDouble() * 100)).setScale(1, BigDecimal.ROUND_HALF_DOWN));
			foodBean.setSale("月售" + new Random().nextInt(100));
			foodBean.setType("类别" + i / 10);
			int resID = context.getResources().getIdentifier("food" + new Random().nextInt(8), "drawable", "com.k.neleme");
			foodBean.setIcon(resID);
			fList.add(foodBean);
		}
		return fList;
	}

	public static List<FoodBean> getDetails(List<FoodBean> fList) {
		ArrayList<FoodBean> flist = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			if (fList.size() > i * 10) {
				flist.add(fList.get(i * 10 - 1));
				flist.add(fList.get(i * 10));
			} else {
				break;
			}
		}
		return flist;
	}

	public static List<CommentBean> getComment() {
		ArrayList<CommentBean> cList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			cList.add(new CommentBean());
		}
		return cList;
	}
}
