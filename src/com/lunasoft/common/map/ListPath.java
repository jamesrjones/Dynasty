package com.lunasoft.common.map;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ForwardingList;

public class ListPath<T extends Location> extends ForwardingList<T> implements Path<T> {

	private final List<T> delegate;

	ListPath(List<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public Iterator<T> iterator() {
		return delegate.iterator();
	}

	@Override
	protected List<T> delegate() {
		return delegate;
	} 

}
