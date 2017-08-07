package com.k.neleme.Views;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.k.neleme.R;


/**
 * Created by Administrator on 2017/1/11.
 */

public final class CustomLoadMoreView extends LoadMoreView {

	@Override
	public int getLayoutId() {
		return R.layout.view_load_more;
	}

	/**
	 * 如果返回true，数据全部加载完毕后会隐藏加载更多
	 * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
	 */

	@Override
	protected int getLoadingViewId() {
		return R.id.load_more_view;
	}

	@Override
	protected int getLoadFailViewId() {
		return R.id.load_more_fail_view;
	}

	/**
	 * isLoadEndGone()为true，可以返回0
	 * isLoadEndGone()为false，不能返回0
	 */
	@Override
	protected int getLoadEndViewId() {
		return R.id.load_more_end_view;
	}

	@Override
	public void convert(BaseViewHolder holder) {
		super.convert(holder);
		if (getLoadMoreStatus() == STATUS_LOADING) {
//			ImageView imageView = holder.getView(getLoadingViewId());
//			final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
//			if (animationDrawable != null) {
//				imageView.post(new Runnable() {
//					@Override
//					public void run() {
//						animationDrawable.start();
//					}
//				});
//			}
		}
	}
}
