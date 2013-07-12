package br.com.cubekode.jilotrack.agent;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.cubekode.jilotrack.track.Track;
import br.com.cubekode.jilotrack.tracker.Tracker;

public class CommandListTrack extends Command {

	@Override
	public String getCommandName() {
		return "/list/track";
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String tracking = request.getParameter("tracking");
		String parent = request.getParameter("parent");

		Set<Track> tracks = Collections.emptySet();

		if (parent != null) {
			tracks = Tracker.store().listTracks(tracking, Integer.valueOf(parent));
		}

		responseJson(request, response, new TrackJSON().trackArray(tracks).toString());
	}
}
