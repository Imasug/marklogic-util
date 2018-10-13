package jp.imanaga.marklogic_util;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;

public class QueryRunnerTest {

	private static final String NORMAL_SERVER_URI = "xcc://admin:password@localhost:8000/Documents";
	private static final String ABNORMAL_SERVER_URI = "xxx";

	private static final String NORMAL_QUERY_CALCULATE = "xquery version '1.0-ml'; let $a := 1 return $a";
	private static final String[] EXPECTED_RESULT_CALCULATE = new String[] { "1" };

	private static final String NORMAL_QUERY_LOG = "xquery version '1.0-ml'; xdmp:log('aaa')";
	private static final String[] EXPECTED_RESULT_LOG = new String[] {};

	private static final String NORMAL_QUERY_SEARCH = "xquery version '1.0-ml'; cts:search(/, cts:word-query('aaa'))";
	private static final String[] EXPECTED_RESULT_SERCH = new String[] {
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test>aaa</test>" };

	private static final String ABNORMAL_QUERY = "xxx";

	@DataProvider(name = "query")
	private Object[][] query() {
		return new Object[][] {
				//
				{ NORMAL_QUERY_CALCULATE, EXPECTED_RESULT_CALCULATE },
				//
				{ NORMAL_QUERY_LOG, EXPECTED_RESULT_LOG },
				//
				{ NORMAL_QUERY_SEARCH, EXPECTED_RESULT_SERCH }, };
	}

	@Test(dataProvider = "query")
	public void normal(String query, String[] expected) throws Exception {

		QueryRunner runner = new QueryRunnerImpl(NORMAL_SERVER_URI);
		String[] actual = runner.exec(query);

		assertEquals(actual.length, expected.length);

		for (int i = 0; i < actual.length; i++) {
			assertEquals(actual[i], expected[i]);
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
