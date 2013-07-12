package br.com.cubekode.jilotrack.agent;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Command {

	public abstract String getCommandName();

	public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws IOException;

	protected String getStringParam(HttpServletRequest request, String name, String defaultValue) {
		String value = request.getParameter(name);
		return value != null ? value : defaultValue;
	}

	protected long getLongParam(HttpServletRequest request, String name, long defaultValue) {
		String value = request.getParameter(name);
		return value != null ? Long.parseLong(value) : defaultValue;
	}

	protected long getIntParam(HttpServletRequest request, String name, int defaultValue) {
		String value = request.getParameter(name);
		return value != null ? Integer.parseInt(value) : defaultValue;
	}

	protected boolean getBoolParam(HttpServletRequest request, String name, boolean defaultValue) {
		String value = request.getParameter(name);
		return value != null ? Boolean.parseBoolean(value) : defaultValue;
	}

	protected void responseOk(HttpServletRequest request, HttpServletResponse response) throws IOException {
		responseText(response, ":)");
	}

	protected void responseNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND, ":(");
	}

	protected void responseJson(HttpServletRequest request, HttpServletResponse response, CharSequence json) throws IOException {
		response.setContentType("application/json");
		String callback = request.getParameter("callback");
		PrintWriter writer = response.getWriter();
		if (callback != null) {
			writer.append(callback).append("(");
		}
		writer.append(json);
		if (callback != null) {
			writer.append(")");
		}
		writer.flush();
	}

	protected void responseText(HttpServletResponse response, CharSequence text) throws IOException {
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		writer.append(text);
		writer.flush();
	}

	protected void responseHtml(HttpServletResponse response, CharSequence html) throws IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.append(html);
		writer.flush();
	}
}
