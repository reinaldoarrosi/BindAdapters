package com.reinaldoarrosi.android.bindadapters;

import android.view.View;

abstract class AbstractBindAction<T> implements IBindAction<T> {
	private Integer view;
	private String key;
	
	public AbstractBindAction(Integer pview, String pkey) {
		view = pview;
		key = pkey;
	}

	@Override
	public abstract void bind(View rootView, T adapter, int position);
	
	protected Integer getView() {
		return view;
	}
	
	protected String getKey() {
		return key;
	}
}
