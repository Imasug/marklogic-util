package jp.imanaga.marklogic_util;

import java.util.List;

import com.marklogic.xcc.exceptions.RequestException;

public interface QueryRunner {

	List<Result> exec(String query) throws RequestException;
}
