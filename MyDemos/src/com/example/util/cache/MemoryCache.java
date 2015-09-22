package com.example.util.cache;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MemoryCache<IDType, T> implements Cache<IDType, T> {

	private final Lock lock = new ReentrantLock();
	private final int maxCapacity;
	private final Map<IDType, T> eden;
	private final Map<IDType, T> perm;

	public MemoryCache(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		this.eden = new ConcurrentHashMap<IDType, T>(maxCapacity);
		this.perm = new WeakHashMap<IDType, T>(maxCapacity);
	}

	public T get(IDType id) {
		T t = this.eden.get(id);
		if (t == null) {
			lock.lock();
			try {
				t = this.perm.get(id);
			} finally {
				lock.unlock();
			}
			if (t != null) {
				this.eden.put(id, t);
			}
		}
		return t;
	}

	
	public void put(IDType id, T t) {
		if (this.eden.size() >= maxCapacity) {
			lock.lock();
			try {
				this.perm.putAll(this.eden);
			} finally {
				lock.unlock();
			}
			this.eden.clear();
		}
		this.eden.put(id, t);
	}

}
