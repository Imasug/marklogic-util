package jp.imanaga.marklogic_util;

import java.util.List;

public class Response {

	private String serverURI;

	private String query;

	private List<Result> results;

	private String execTime;

	public String getServerURI() {
		return serverURI;
	}

	public void setServerURI(String serverURI) {
		this.serverURI = serverURI;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
}
