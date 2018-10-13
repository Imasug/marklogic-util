package jp.imanaga.marklogic_util;

import com.marklogic.xcc.exceptions.RequestException;

public interface QueryRunner {

	String[] exec(String query) throws RequestException;
}
