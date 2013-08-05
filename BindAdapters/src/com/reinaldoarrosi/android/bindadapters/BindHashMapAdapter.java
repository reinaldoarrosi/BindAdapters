package com.reinaldoarrosi.android.bindadapters;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BindHashMapAdapter<K, V> extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<HashMap<K, V>> list;
	private String idKey = null;
	private int layout;
	private ArrayList<IBindAction<BindHashMapAdapter<K, V>>> bindings;
	
	public BindHashMapAdapter(Context context, ArrayList<HashMap<K, V>> list, int layout) {
		this(context, list, null, layout);
	}
	
	public BindHashMapAdapter(Context context, ArrayList<HashMap<K, V>> list, String idKey, int layout) {
		initialize(context, list, idKey, layout);
	}
	
	private void initialize(Context context, ArrayList<HashMap<K, V>> list, String idKey, int layout) {
		this.list = list;
		this.layout = layout;
		this.idKey = idKey;
		this.inflater = LayoutInflater.from(context);
		this.bindings = new ArrayList<IBindAction<BindHashMapAdapter<K, V>>>();
	}
	
	public BindHashMapAdapter<K, V> bindText(Integer view, String key) {
		AbstractBindAction<BindHashMapAdapter<K, V>> action = new AbstractBindAction<BindHashMapAdapter<K, V>>(view, key) {
			
			@Override
			public void bind(View rootView, BindHashMapAdapter<K, V> adapter, int position) {
				if(rootView == null)
					return;
				
				View viewToBind;
				if(getView() != null) {
					viewToBind = rootView.findViewById(getView());
					
					if(viewToBind == null)
						return;
				}
				else
					viewToBind = rootView;
				
				@SuppressWarnings("unchecked")
				HashMap<K, V> dataSource = (HashMap<K, V>) adapter.getItem(position);
				String value = String.valueOf(dataSource.get(getKey()));
				
				if(viewToBind instanceof TextView) {
					((TextView)viewToBind).setText(value);
				}
			}
		};
		
		bindings.add(action);
		return this;
	}
	
	public BindHashMapAdapter<K, V> bindVisible(Integer view, String key) {
		AbstractBindAction<BindHashMapAdapter<K, V>> action = new AbstractBindAction<BindHashMapAdapter<K, V>>(view, key) {
			
			@Override
			public void bind(View rootView, BindHashMapAdapter<K, V> adapter, int position) {
				if(rootView == null)
					return;
				
				View viewToBind;
				if(getView() != null) {
					viewToBind = rootView.findViewById(getView());
					
					if(viewToBind == null)
						return;
				}
				else
					viewToBind = rootView;
				
				@SuppressWarnings("unchecked")
				HashMap<K, V> dataSource = (HashMap<K, V>) adapter.getItem(position); 
				String value = String.valueOf(dataSource.get(getKey()));
				int visible;
				
				try {
					int iValue = Integer.parseInt(value);
					
					if(iValue != View.VISIBLE && iValue != View.INVISIBLE && iValue != View.GONE)
						visible = (iValue < 0 ? View.GONE : View.VISIBLE);
					else
						visible = iValue;
				} catch (NumberFormatException nfe) {
					boolean bValue = Boolean.parseBoolean(value);
					visible = (bValue ? View.VISIBLE : View.GONE);
				}
				
				viewToBind.setVisibility(visible);
			}
		};
		
		bindings.add(action);		
		return this;
	}
	
	public BindHashMapAdapter<K, V> bindEnabled(Integer view, String key) {
		AbstractBindAction<BindHashMapAdapter<K, V>> action = new AbstractBindAction<BindHashMapAdapter<K, V>>(view, key) {
			
			@Override
			public void bind(View rootView, BindHashMapAdapter<K, V> adapter, int position) {
				if(rootView == null)
					return;
				
				View viewToBind;
				if(getView() != null) {
					viewToBind = rootView.findViewById(getView());
					
					if(viewToBind == null)
						return;
				}
				else
					viewToBind = rootView;
				
				@SuppressWarnings("unchecked")
				HashMap<K, V> dataSource = (HashMap<K, V>) adapter.getItem(position);
				String value = String.valueOf(dataSource.get(getKey()));
				boolean enabled;
				
				try {
					int iValue = Integer.parseInt(value);
					enabled = (iValue > 0 ? true : false);
				} catch (NumberFormatException nfe) {
					enabled = Boolean.parseBoolean(value);
				}
				
				viewToBind.setEnabled(enabled);
			}
		};
		
		bindings.add(action);
		return this;
	}
	
	public BindHashMapAdapter<K, V> bindCustom(IBindAction<BindHashMapAdapter<K, V>> action) {
		bindings.add(action);
		return this;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		if(idKey == null || idKey.length() == 0)
			return position;
		
		@SuppressWarnings("unchecked")
		HashMap<K, V> item = (HashMap<K, V>) getItem(position);
		
		if(!item.containsKey(idKey))	
			return position;
		
		String value = String.valueOf(item.get(idKey));
		
		try {
			long id = Long.parseLong(value);
			return id;
		} catch (NumberFormatException nfe) {
			return position;
		}
	}

	@Override
	public View getView(int position, View reusableView, ViewGroup parent) {	
		View view;
		
		if(reusableView != null)	
			view = reusableView;
		else
			view = inflater.inflate(layout, parent, false);
		
		for(IBindAction<BindHashMapAdapter<K, V>> bd : bindings) {
			bd.bind(view, this, position);
		}
		
		return view;
	}
}
