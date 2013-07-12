package br.com.cubekode.jilotrack.store;

public class TrackStoreBlockSync extends TrackStoreWrapper<TrackStoreBlock> {

	private Object mutex = new Object();

	private TrackStoreBlock sync;

	public TrackStoreBlock renew() {
		synchronized (mutex) {
			TrackStoreBlock consumed = sync;
			sync = null;
			return consumed;
		}
	}

	@Override
	public TrackStoreBlock getWrapped() {
		synchronized (mutex) {
			if (sync == null) {
				sync = new TrackStoreBlock();
			}
			return sync;
		}
	}
}
