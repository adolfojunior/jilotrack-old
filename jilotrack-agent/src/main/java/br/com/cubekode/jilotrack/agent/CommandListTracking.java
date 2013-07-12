package br.com.cubekode.jilotrack.agent;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.cubekode.jilotrack.track.Tracking;
import br.com.cubekode.jilotrack.tracker.Tracker;

public class CommandListTracking extends Command {

	@Override
	public String getCommandName() {
		return "/list/tracking";
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

		long currentTime = System.currentTimeMillis();

		// Default 5 minutes with range of 10 seconds.
		long begin = getLongParam(request, "begin", currentTime - TimeUnit.MINUTES.toMillis(5));
		long end = getLongParam(request, "end", currentTime);

		Collection<Tracking> trackings = findTrackings(begin, end);

		TrackJSON json = new TrackJSON();

		json.openObject();
		json.property("trackings").trackingMap(trackings);
		json.closeObject();

		responseJson(request, response, json.toString());
	}

	private Set<Tracking> findTrackings(long begin, long end) {
		return Tracker.store().list();
	}
}
