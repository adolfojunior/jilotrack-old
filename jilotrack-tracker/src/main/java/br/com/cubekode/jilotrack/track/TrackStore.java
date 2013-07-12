package br.com.cubekode.jilotrack.track;

import java.util.Set;

public interface TrackStore {

	Tracking create(String key);

	Set<Tracking> list();

	Tracking find(String id);

	Tracking remove(String id);

	Set<Track> listAllTracks(String id);

	Set<Track> listTracks(String id, Integer parent);

	void begin(Tracking tracking);

	void begin(Tracking tracking, Track track);

	void end(Tracking tracking, Track track);

	void end(Tracking tracking);

	void disabled(Tracking tracking);

}
