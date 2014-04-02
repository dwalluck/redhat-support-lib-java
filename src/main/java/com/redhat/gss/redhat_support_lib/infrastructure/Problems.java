package com.redhat.gss.redhat_support_lib.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.redhat.gss.redhat_support_lib.errors.RequestException;
import com.redhat.gss.redhat_support_lib.helpers.ParseHelper;
import com.redhat.gss.redhat_support_lib.parsers.Link;
import com.redhat.gss.redhat_support_lib.web.ConnectionManager;

public class Problems extends BaseQuery {
    private final static Logger LOGGER = Logger
            .getLogger(Problems.class.getName());
	ConnectionManager connectionManager = null;
	static String url = "/rs/problems/";

	public Problems(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	/**
	 * Queries the problems RESTful interface with a given string. RESTful
	 * method: https://api.access.redhat.com/rs/problems
	 * 
	 * @param content
	 *            string whose content you want diagnosed.
	 * @return An array of problems.
	 * @throws RequestException
	 *             An exception if there was a connection related issue.
	 * @throws MalformedURLException 
	 */
	public List<Link> diagnoseStr(String content) throws RequestException, MalformedURLException {

		WebResource webResource = connectionManager.getConnection().resource(
				connectionManager.getConfig().getUrl() + url);
		ClientResponse resp = add(webResource, content);
		com.redhat.gss.redhat_support_lib.parsers.Problems probs = resp
				.getEntity(com.redhat.gss.redhat_support_lib.parsers.Problems.class);
		return ParseHelper.getLinksFromProblems(probs);
	}

	/**
	 * Queries the problems RESTful interface with a given file. RESTful method:
	 * https://api.access.redhat.com/rs/problems
	 * 
	 * @param fileName
	 *            File name whose content you want diagnosed.
	 * @return An array of problems.
	 * @throws Exception 
	 */
	public List<Link> diagnoseFile(String fileName) throws Exception {
		WebResource webResource = connectionManager.getConnection().resource(
				connectionManager.getConfig().getUrl() + url);

		ClientResponse resp = upload(webResource, new File(fileName), fileName);
		com.redhat.gss.redhat_support_lib.parsers.Problems probs = resp
				.getEntity(com.redhat.gss.redhat_support_lib.parsers.Problems.class);
		return ParseHelper.getLinksFromProblems(probs);
	}

}
