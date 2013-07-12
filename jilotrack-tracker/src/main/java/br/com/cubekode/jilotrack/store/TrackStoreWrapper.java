package br.com.cubekode.jilotrack.store;

import java.util.Set;

import br.com.cubekode.jilotrack.track.Track;
import br.com.cubekode.jilotrack.track.TrackStore;
import br.com.cubekode.jilotrack.track.Tracking;

public abstract class TrackStoreWrapper<T extends TrackStore> implements TrackStore {

	public abstract T getWrapped();

	@Override
	public Tracking create(String key) {
		return getWrapped().create(key);
	}

	@Override
	public Set<Tracking> list() {
		return getWrapped().list();
	}

	@Override
	public Tracking find(String id) {
		return getWrapped().find(id);
	}

	@Override
	public Tracking remove(String id) {
		return getWrapped().remove(id);
	}

	@Override
	public Set<Track> listAllTracks(String id) {
		return getWrapped().listAllTracks(id);
	}

	@Override
	public Set<Track> listTracks(String id, Integer parent) {
		return getWrapped().listTracks(id, parent);
	}

	@Override
	public void begin(Tracking tracking) {
		getWrapped().begin(tracking);
	}

	@Override
	public void begin(Tracking tracking, Track track) {
		getWrapped().begin(tracking, track);
	}

	@Override
	public void end(Tracking tracking, Track track) {
		getWrapped().end(tracking, track);
	}

	@Override
	public void end(Tracking tracking) {
		getWrapped().end(tracking);
	}

	@Override
	public void disabled(Tracking tracking) {
		getWrapped().disabled(tracking);
	}
}
