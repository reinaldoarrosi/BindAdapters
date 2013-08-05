package com.reinaldoarrosi.android.bindadapters;

import android.view.View;

public interface IBindAction<T> {
	public void bind(View rootView, T adapter, int position);
}
