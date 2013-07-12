package br.com.cubekode.jilotrack.agent;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import br.com.cubekode.jilotrack.track.Track;
import br.com.cubekode.jilotrack.track.Tracking;
import br.com.cubekode.jilotrack.util.JSON;

public class TrackJSON extends JSON<TrackJSON> {

	public TrackJSON() {
		this(new StringBuilder(2048));
	}

	public TrackJSON(Appendable appendable) {
		super(appendable);
	}

	public TrackJSON trackingArray(Collection<Tracking> trackings) throws IOException {
		openArray();
		int count = 0;
		for (Tracking tracking : trackings) {
			if (count > 0) {
				separator();
			}
			object(tracking);
			count++;
		}
		return closeArray();
	}

	public TrackJSON trackingMap(Collection<Tracking> trackings) throws IOException {
		openObject();
		int count = 0;
		for (Tracking tracking : trackings) {
			if (count > 0) {
				separator();
			}
			property(tracking.getId());
			object(tracking);
			count++;
		}
		return closeObject();
	}

	public TrackJSON trackArray(Collection<Track> tracks) throws IOException {
		openArray();
		int count = 0;
		for (Track track : tracks) {
			if (track != null) {
				if (count > 0) {
					separator();
				}
				object(track);
				count++;
			}
		}
		return closeArray();
	}

	public TrackJSON trackMap(Map<Integer, Track> tracks) throws IOException {
		openObject();
		int count = 0;
		for (Entry<Integer, Track> t : tracks.entrySet()) {
			if (count > 0) {
				separator();
			}
			property(t.getKey()).object(t.getValue());
			count++;
		}
		return closeObject();
	}

	public TrackJSON object(Tracking tracking) throws IOException {
		if (tracking != null) {
			openObject();
			property("id").string(tracking.getId()).separator();
			property("key").string(tracking.getKey()).separator();
			property("time").number(tracking.getTime()).separator();
			property("beginTime").number(tracking.getBeginTime()).separator();
			property("endTime").number(tracking.getEndTime()).separator();
			property("time").number(tracking.getTime()).separator();
			property("root").object(tracking.getRoot()).separator();
			property("tags").map(tracking.getTags());
			return closeObject();
		}
		return nullRef();
	}

	public TrackJSON object(Track track) throws IOException {
		if (track != null) {
			openObject();
			property("tracking").string(track.getTracking()).separator();
			property("key").string(track.getKey()).separator();
			property("index").number(track.getIndex()).separator();
			property("deep").number(track.getDeep()).separator();
			property("beginTime").number(track.getBeginTime()).separator();
			property("endTime").number(track.getEndTime()).separator();
			property("time").number(track.getTime()).separator();
			property("parent").number(track.getParent()).separator();
			property("children").numberArray(track.getChildren()).separator();
			property("data").string(track.getData()).separator();
			property("error").string(track.getError());
			return closeObject();
		}
		return nullRef();
	}
}
