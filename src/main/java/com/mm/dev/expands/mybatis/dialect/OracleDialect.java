package com.mm.dev.expands.mybatis.dialect;

public class OracleDialect extends Dialect {

	public String getLimitString(String querySqlString, int offset, int limit) {

		boolean isForUpdate = false;
		if (querySqlString.toLowerCase().endsWith(" for update")) {
            querySqlString = querySqlString.substring(0, querySqlString.length() - 11);
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer(querySqlString.length() + 100);

		pagingSelect
				.append("select * from ( select row_.*, rownum rownum_ from ( ");

		pagingSelect.append(querySqlString);

		pagingSelect.append(" ) row_ ) where rownum_ > " + offset
				+ " and rownum_ <= " + (offset + limit));

		if (isForUpdate) {
			pagingSelect.append(" for update");
		}

		return pagingSelect.toString();
	}

	@Override
	public String getCountString(String querySqlString) {
		return null;
	}
}
