package br.com.cubekode.jilotrack.tracker.jsf;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.cubekode.jilotrack.track.Tracking;
import br.com.cubekode.jilotrack.tracker.Tracker;

public class TrackerPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	public void beforePhase(PhaseEvent event) {

		Tracking tracking = Tracker.get();

		tracking.setTag("JSF", Boolean.TRUE.toString());

		tracking.begin("JSF-" + event.getPhaseId().toString());

	}

	public void afterPhase(PhaseEvent event) {
		
		Tracker.get().end("JSF-" + event.getPhaseId().toString());
	}
}
