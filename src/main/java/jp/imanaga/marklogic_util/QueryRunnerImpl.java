package jp.imanaga.marklogic_util;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Request;
import com.marklogic.xcc.ResultItem;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;

public class QueryRunnerImpl implements QueryRunner {

	private Session session;

	public QueryRunnerImpl(String serverURI) throws URISyntaxException, XccConfigException {

		assert serverURI != null;

		URI uri = new URI(serverURI);
		ContentSource contentSource = ContentSourceFactory.newContentSource(uri);
		this.session = contentSource.newSession();
	}

	public ResultSequence submit(String query) throws RequestException {

		assert query != null;

		Request request = this.session.newAdhocQuery(query);

		return session.submitRequest(request);
	}

	@Override
	public List<Result> exec(String query) throws RequestException {

		ResultSequence resultSequence = this.submit(query);

		List<Result> results = new ArrayList<>();
		while (resultSequence.hasNext()) {
			ResultItem item = resultSequence.next();
			Result result = new Result();
			result.setUri(item.getDocumentURI());
			result.setValue(item.asString());
			results.add(result);
		}

		return results;
	}

	public static void main(String[] args) throws Exception {

		// TODO test
		if (args.length != 2) {
			throw new Exception("The usage is wrong! usage: serverURI query");
		}

		String serverURI = args[0];
		String query = args[1];

		QueryRunner queryRunner = new QueryRunnerImpl(serverURI);
		List<Result> results = queryRunner.exec(query);

		// TODO test
		Response response = new Response();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		response.setExecTime(LocalDateTime.now().format(formatter));
		response.setServerURI(serverURI);
		response.setQuery(query);
		response.setResults(results);

		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(response));
	}
}
