package com.mm.dev.expands.mybatis.dialect;

public class MySql5Dialect extends Dialect {

	public String getLimitString(String querySqlString, int offset, int limit) {
		return querySqlString + " limit " + offset + " ," + limit;
	}

	@Override
	public String getCountString(String querySqlString) {

		int limitIndex = querySqlString.lastIndexOf("limit");

		if(limitIndex != -1){
			querySqlString = querySqlString.substring(0, limitIndex != -1 ? limitIndex : querySqlString.length() - 1);
		}

		return "SELECT COUNT(*) FROM (" + querySqlString + ") tem";
	}

	public boolean supportsLimit() {
		return true;
	}
}
