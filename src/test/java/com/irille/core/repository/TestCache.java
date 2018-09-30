package com.irille.core.repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class TestCache {

	public static void main(String[] args) {
		LoadingCache lc = CacheBuilder.newBuilder().maximumSize(100L).build(new CacheLoader<Integer, User>() {

			@Override
			public User load(Integer key) throws Exception {
				return null;
			}
		});
	}
}
