package br.com.cubekode.jilotrack.agent;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.cubekode.jilotrack.tracker.Tracker;

public class AgentServlet extends HttpServlet {

	private static final String REQUEST_TRACKER_ATTRIBUTE = "br.com.cubekode.apptrack.tracker";

    private static final String SLASH = "/";

	private static final long serialVersionUID = 1L;

	private Map<String, Command> defaultCommands;

	private Map<String, Command> serviceCommands;

	@Override
	public void init() throws ServletException {

		defaultCommands = new TreeMap<String, Command>(String.CASE_INSENSITIVE_ORDER);
		serviceCommands = new TreeMap<String, Command>(String.CASE_INSENSITIVE_ORDER);

		installDefaultCommands();
		installServiceCommands();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Discard tracking commands.
		if (request.getAttribute(REQUEST_TRACKER_ATTRIBUTE) != null) {
			Tracker.disabled();
		}

		String name = getCommandName(request);

		Command command = getInstalledCommand(name);

		if (command != null) {
			command.execute(request, response);
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Command \"" + name + "\" not found");
		}
	}

	protected void installDefaultCommands() {
		addCommand(defaultCommands, new CommandListTracking());
		addCommand(defaultCommands, new CommandListTrack());
	}

	protected void installServiceCommands() {
		for (Command command : ServiceLoader.load(Command.class)) {
			addCommand(serviceCommands, command);
		}
	}

	protected Command getInstalledCommand(String commandName) {
		Command command = getCommand(serviceCommands, commandName);
		if (command == null) {
			return getCommand(defaultCommands, commandName);
		}
		return null;
	}

	protected String getCommandName(HttpServletRequest request) {

		String pathInfo = request.getPathInfo();

		if (pathInfo == null) {
			return SLASH;
		}

		if (pathInfo.endsWith(SLASH)) {
			pathInfo.substring(0, pathInfo.length() - 1);
		}

		if (!pathInfo.startsWith(SLASH)) {
			pathInfo = SLASH.concat(pathInfo);
		}
		return pathInfo;
	}

	protected Command getCommand(Map<String, Command> commands, String commandName) {
		return commandName != null ? commands.get(commandName) : null;
	}

	protected void addCommand(Map<String, Command> commands, Command command) {
		Command existentCommand = commands.get(command.getCommandName());
		if (existentCommand != null) {
			throw new IllegalStateException("Ambiguous Command: " + command.getClass() + " and " + existentCommand.getClass());
		} else {
			commands.put(command.getCommandName(), command);
		}
	}
}
