package com.reinaldoarrosi.android.bindadapters;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BindCursorAdapter extends BaseAdapter {
	protected LayoutInflater inflater;
	protected Cursor cursor;
	protected Integer idColumn;
	protected int layout;
	protected ArrayList<IBindAction<BindCursorAdapter>> bindings;
		
	public BindCursorAdapter(Context context, Cursor cursor, int layout) {
		initialize(context, cursor, null, layout);
	}
	
	public BindCursorAdapter(Context context, Cursor cursor, String idColumnName, int layout) {
		int idColumn = cursor.getColumnIndexOrThrow(idColumnName);
		initialize(context, cursor, idColumn, layout);
	}
	
	public BindCursorAdapter(Context context, Cursor cursor, Integer idColumn, int layout) {
		initialize(context, cursor, idColumn, layout);
	}
	
	private void initialize(Context context, Cursor cursor, Integer idColumn, int layout) {
		this.cursor = cursor;
		this.layout = layout;
		this.idColumn = idColumn;
		this.inflater = LayoutInflater.from(context);
		this.bindings = new ArrayList<IBindAction<BindCursorAdapter>>();
	}
	
	public BindCursorAdapter bindText(Integer view, String column) {
		AbstractBindAction<BindCursorAdapter> action = new AbstractBindAction<BindCursorAdapter>(view, column) {
			
			@Override
			public void bind(View rootView, BindCursorAdapter adapter, int position) {
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
				
				Cursor dataSource = (Cursor) adapter.getItem(position);
				int columnIndex = dataSource.getColumnIndex(getKey());
				if(columnIndex < 0)
					return;
				
				String value = dataSource.getString(columnIndex);
				if(viewToBind instanceof TextView) {
					((TextView)viewToBind).setText(value);
				}
			}
		};
		
		bindings.add(action);
		return this;
	}
	
	public BindCursorAdapter bindVisible(Integer view, String column) {
		AbstractBindAction<BindCursorAdapter> action = new AbstractBindAction<BindCursorAdapter>(view, column) {
			
			@Override
			public void bind(View rootView, BindCursorAdapter adapter, int position) {
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
				
				Cursor dataSource = (Cursor) adapter.getItem(position);
				int columnIndex = dataSource.getColumnIndex(getKey());
				if(columnIndex < 0)
					return;
				
				String value = dataSource.getString(columnIndex);
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
	
	public BindCursorAdapter bindEnabled(Integer view, String column) {
		AbstractBindAction<BindCursorAdapter> action = new AbstractBindAction<BindCursorAdapter>(view, column) {
			
			@Override
			public void bind(View rootView, BindCursorAdapter adapter, int position) {
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
				
				Cursor dataSource = (Cursor) adapter.getItem(position);
				int columnIndex = dataSource.getColumnIndex(getKey());
				if(columnIndex < 0)
					return;
				
				String value = dataSource.getString(columnIndex);
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
		
	public BindCursorAdapter bindCustom(IBindAction<BindCursorAdapter> action) {
		bindings.add(action);
		return this;
	}
	
	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position)
	{
		cursor.moveToPosition(position);
		return cursor;
	}

	@Override
	public long getItemId(int position) {			
		if(idColumn == null) {
			return position;
		} else {			
			cursor.moveToPosition(position);
			return cursor.getLong(idColumn);
		}
	}

	@Override
	public View getView(int position, View reusableView, ViewGroup parent) {	
		View view;
		
		if(reusableView != null)
			view = reusableView;
		else
			view = inflater.inflate(layout, parent, false);
		
		for(IBindAction<BindCursorAdapter> bd : bindings) {
			bd.bind(view, this, position);
		}
		
		return view;
	}
		
	public void release() {
		if(cursor != null && !cursor.isClosed())
			cursor.close();
		
		cursor = null;
	}
}
