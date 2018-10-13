package jp.imanaga.marklogic_util;

import static org.testng.Assert.assertEquals;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;

public class QueryRunnerTest {

	private static final String NORMAL_SERVER_URI = "xcc://admin:password@localhost:8000/Documents";
	private static final String ABNORMAL_SERVER_URI = "xxx";

	private static final String NORMAL_QUERY_LOG = "xquery version '1.0-ml'; xdmp:log('aaa')";

	private static final String NORMAL_QUERY_CALCULATE = "xquery version '1.0-ml'; let $a := 1 return $a";
	private static final String EXPECTED_URI_CALCULATE = null;
	private static final String EXPECTED_VALUE_CALCULATE = "1";

	private static final String NORMAL_QUERY_SEARCH = "xquery version '1.0-ml'; cts:search(/, cts:word-query('aaa'))";
	private static final String EXPECTED_URI_SEARCH = "/aaa.xml";
	private static final String EXPECTED_VALUE_SEARCH = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test>aaa</test>";

	private static final String ABNORMAL_QUERY = "xxx";

	private Result newResult(String uri, String value) {
		Result result = new Result();
		result.setUri(uri);
		result.setValue(value);
		return result;
	}

	@DataProvider(name = "query")
	private Object[][] query() {
		return new Object[][] {
				//
				{ NORMAL_QUERY_LOG, Arrays.asList() },
				//
				{ NORMAL_QUERY_CALCULATE, Arrays.asList(newResult(EXPECTED_URI_CALCULATE, EXPECTED_VALUE_CALCULATE)) },
				//
				{ NORMAL_QUERY_SEARCH, Arrays.asList(newResult(EXPECTED_URI_SEARCH, EXPECTED_VALUE_SEARCH)) }
				//
		};
	}

	@Test(dataProvider = "query")
	public void normal(String query, List<Result> expected) throws Exception {

		QueryRunner runner = new QueryRunnerImpl(NORMAL_SERVER_URI);
		List<Result> actual = runner.exec(query);
		assertEquals(actual.size(), expected.size());
		for (int i = 0; i < actual.size(); i++) {
			assertThat(actual.get(i), is(samePropertyValuesAs(expected.get(i))));
		}
	}

	@Test(expectedExceptions = XccConfigException.class)
	public void testAbnormalConnection() throws Exception {

		new QueryRunnerImpl(ABNORMAL_SERVER_URI);
	}

	@Test(expectedExceptions = RequestException.class)
	public void testAbnormalQuery() throws Exception {

		QueryRunner runner = new QueryRunnerImpl(NORMAL_SERVER_URI);
		runner.exec(ABNORMAL_QUERY);
	}
}
