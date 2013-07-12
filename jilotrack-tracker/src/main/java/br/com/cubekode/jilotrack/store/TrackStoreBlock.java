package br.com.cubekode.jilotrack.store;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.cubekode.jilotrack.track.Track;
import br.com.cubekode.jilotrack.track.TrackStore;
import br.com.cubekode.jilotrack.track.Tracking;
import br.com.cubekode.jilotrack.util.IdentityUtil;

public class TrackStoreBlock implements TrackStore {

	private TrackingMap trackingMap = createTrackingMap();

	private TrackMap trackMap = createTrackMap();

	private int elementCount = 0;

	private int storeCount = 0;

	private String storeIdentity;

	public TrackStoreBlock() {
		// Eh possivel duas instancias para o mesmo Host com o memso endereco
		// fisico de memoria alocados no mesmo timeMillis??
		// host_hash_timeMillis
		storeIdentity = new String(String.format("%s_%s_%s_", IdentityUtil.host(), IdentityUtil.object(this), IdentityUtil.time()));
	}

	protected TrackingMap createTrackingMap() {
		return new TrackingMap();
	}

	protected TrackMap createTrackMap() {
		return new TrackMap();
	}

	protected String createId() {
		synchronized (storeIdentity) {
			return storeIdentity.concat(IdentityUtil.number(storeCount++));
		}
	}

	public Tracking create(String key) {
		return new Tracking(this, createId(), key);
	}

	@Override
	public void begin(Tracking tracking) {
		trackingMap.add(tracking);
	}

	@Override
	public void begin(Tracking tracking, Track track) {
		addTrack(tracking, track);
	}

	@Override
	public void end(Tracking tracking, Track track) {
		addTrack(tracking, track);
	}

	@Override
	public void end(Tracking tracking) {
		trackingMap.add(tracking);
	}

	@Override
	public void disabled(Tracking tracking) {
		trackingMap.remove(tracking);
	}

	protected void addTrack(Tracking tracking, Track track) {
		trackMap.add(tracking, track);
	}

	@Override
	public Set<Tracking> list() {
		return new LinkedHashSet<Tracking>(trackingMap.values());
	}

	@Override
	public Tracking find(String id) {
		return trackingMap.get(id);
	}

	@Override
	public Tracking remove(String id) {
		return trackingMap.remove(id);
	}

	@Override
	public Set<Track> listAllTracks(String id) {
		List<Track> c = trackMap.get(id);
		if (c == null) {
			return Collections.emptySet();
		}
		return new LinkedHashSet<Track>(c);
	}

	@Override
	public Set<Track> listTracks(String id, Integer parent) {
		List<Track> c = trackMap.get(id);
		if (c == null) {
			return Collections.emptySet();
		}
		Set<Track> tracks = new LinkedHashSet<Track>();
		for (Track track : c) {
			if (parent == track.getParent() || parent.equals(track.getParent())) {
				tracks.add(track);
			}
		}
		return tracks;
	}

	public int size() {
		return elementCount;
	}

	protected class TrackingMap extends LinkedHashMap<String, Tracking> {

		private static final long serialVersionUID = 1L;

		public TrackingMap() {
			super();
		}

		public TrackingMap(int initialCapacity, float loadFactor, boolean accessOrder) {
			super(initialCapacity, loadFactor, accessOrder);
		}

		public TrackingMap(Map<? extends String, ? extends Tracking> m) {
			super(m);
		}

		public Tracking add(Tracking tracking) {
			Tracking removed = put(tracking.getId(), tracking);
			if (removed == null) {
				elementCount++;
			}
			return removed;
		}

		public Tracking remove(Tracking tracking) {
			return remove(tracking.getId());
		}

		@Override
		public Tracking remove(Object key) {
			Tracking removed = super.remove(key);
			if (removed != null) {
				elementCount--;
				trackMap.remove(key);
			}
			return removed;
		}
	}

	protected class TrackMap extends LinkedHashMap<String, List<Track>> {

		private static final long serialVersionUID = 1L;

		public TrackMap() {
			super();
		}

		public TrackMap(int initialCapacity, float loadFactor, boolean accessOrder) {
			super(initialCapacity, loadFactor, accessOrder);
		}

		public TrackMap(Map<? extends String, ? extends List<Track>> m) {
			super(m);
		}

		public void add(Tracking tracking, Track track) {
			List<Track> list = get(tracking.getId());
			if (list == null) {
				put(tracking.getId(), list = new LinkedList<Track>());
			}
			list.add(track);
			elementCount++;
		}

		@Override
		public List<Track> remove(Object key) {
			List<Track> removed = super.remove(key);
			if (removed != null) {
				elementCount -= removed.size();
			}
			return removed;
		}
	}
}
