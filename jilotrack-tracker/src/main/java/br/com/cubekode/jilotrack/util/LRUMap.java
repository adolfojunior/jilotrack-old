package br.com.cubekode.jilotrack.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;

	private final int capacity;

	public LRUMap(int capacity, boolean accessOrder) {
		super(capacity + 1, 1.1f, accessOrder);
		this.capacity = capacity;
	}

	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > capacity;
	}
}
