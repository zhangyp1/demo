package com.newland.paas.sbcommon.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author laiCheng
 * @date 2018/8/27 15:17
 */
public abstract class CommandPool<T, K> {


	private List<T> list = new ArrayList<>();

	private Object listLock = new Object();

	public List<T> copyList() {
		synchronized (listLock) {
			return new ArrayList<>(list);
		}
	}

	public void list(Consumer<T> cons) {
		List<T> lstCp = copyList();
		for (T cp : lstCp) {
			synchronized (listLock) {
				list.stream().filter(e -> getKey(e).equals(getKey(cp))).findFirst().ifPresent((find) -> {
					cons.accept(cp);
				});
			}

		}
	}

	public void delete(K key) {
		synchronized (listLock) {
			list = new ArrayList<>(list.stream().filter(e -> !getKey(e).equals(key)).collect(Collectors.toList()));
		}
	}

	public void add(T t) {
		synchronized (listLock) {
			list.removeIf(e -> getKey(e).equals(getKey(t)));
			list.add(t);
		}
	}

	public abstract K getKey(T t);
}
