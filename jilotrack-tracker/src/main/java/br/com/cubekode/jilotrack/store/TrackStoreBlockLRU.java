package br.com.cubekode.jilotrack.store;

import br.com.cubekode.jilotrack.track.Tracking;

public class TrackStoreBlockLRU extends TrackStoreBlock {

	private int maxCount;

	public TrackStoreBlockLRU(int maxCount) {
		this.maxCount = maxCount;
	}

	@Override
	protected TrackingMap createTrackingMap() {

		return new TrackingMap(maxCount + 1, 1.1f, false) {

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<String, Tracking> eldest) {
				return size() > maxCount;
			}
		};
	}
}
