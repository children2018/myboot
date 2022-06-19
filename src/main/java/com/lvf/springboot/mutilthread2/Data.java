package com.lvf.springboot.mutilthread2;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

public class Data<T> {

	private List<T> list;
	private int size;

	public Data(int size, List<T> list) {
		this.list = list;
		this.size = size;
	}

	public synchronized List<T> getData() {
		
		if (list == null || list.isEmpty()) {
			return null;
		}

		List<T> result = new ArrayList<T>();
		while (list.size() > 0) {
			result.add(list.get(0));
			list.remove(0);
			if (result.size() >= size) {
				break;
			}
		}
		
		return result;
	}

}
