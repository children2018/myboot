package com.lvf.redis;

public enum dbApi {
	
	PUT("/api/put"), QUERY("/api/query");

	private String path;

	dbApi(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return path;
	}
	
	public static void main(String[] args) {
		System.out.println(dbApi.PUT.toString());
		System.out.println(dbApi.PUT);
	}
}