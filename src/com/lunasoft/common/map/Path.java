package com.lunasoft.common.map;

import java.util.Iterator;

public interface Path<T extends Location> extends Iterable<T> {

	Iterator<T> iterator();
}
