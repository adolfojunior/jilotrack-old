package br.com.cubekode.jilotrack.tracker;

import br.com.cubekode.jilotrack.track.Track;
import br.com.cubekode.jilotrack.track.TrackStore;
import br.com.cubekode.jilotrack.track.Tracking;

/**
 * @author adolfojunior
 */
public class Tracker {

	private static final TrackerThread THREAD = new TrackerThread();

	public static TrackStore store() {
		return THREAD.getStore();
	}

	public static Tracking get() {
		return get(true);
	}

	public static Tracking get(boolean create) {
		return THREAD.get(create);
	}

	public static Track begin(String key) {
		return get().begin(key);
	}

	public static Track end(String key) {
		return get().end(key);
	}

	public static Track end(String key, Throwable e) {
		return get().end(key, e);
	}

	public static Tracking push(String key) {
		return THREAD.push(key);
	}

	public static Tracking pop() {
		return THREAD.pop();
	}

	public static void disabled() {
		Tracking tracking = get(false);
		if (tracking != null) {
			tracking.disabled();
		}
	}
}
