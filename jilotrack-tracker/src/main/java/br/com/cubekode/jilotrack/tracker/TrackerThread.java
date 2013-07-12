package br.com.cubekode.jilotrack.tracker;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Deque;
import java.util.LinkedList;

import br.com.cubekode.jilotrack.store.TrackStoreBlockLRU;
import br.com.cubekode.jilotrack.track.TrackStore;
import br.com.cubekode.jilotrack.track.Tracking;

/**
 * @author adolfojunior
 */
public class TrackerThread {

	private static String HOST_NAME = "Unknow";
	private static String HOST_ADDRESS = "Unknow";

	private TrackStore store = new TrackStoreBlockLRU(100);

	static {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			HOST_NAME = localHost.getHostName();
			HOST_ADDRESS = localHost.getHostAddress();
		} catch (UnknownHostException e) {
			// shhhh
		}
	}
	
	private final ThreadLocal<Deque<Tracking>> threadStack = new ThreadLocal<Deque<Tracking>>() {
		protected Deque<Tracking> initialValue() {
			return new LinkedList<Tracking>();
		};
	};

	public TrackStore getStore() {
		return store;
	}

	public void setStore(TrackStore store) {
		this.store = store;
	}

	public Tracking push(String key) {

		Deque<Tracking> deque = threadStack.get();

		Tracking tracking = create(key);

		Tracking parent = deque.peek();

		if (parent != null) {
			parent.begin(tracking);
		}

		deque.push(tracking);

		return tracking;
	}

	public Tracking pop() {

		Deque<Tracking> deque = threadStack.get();

		if (!deque.isEmpty()) {

			Tracking tracking = deque.pop();

			tracking.end();

			Tracking parent = deque.peek();

			if (parent != null) {
				parent.end(tracking);
			}
			return tracking;
		}
		return null;
	}

	public Tracking get(boolean create) {

		Tracking current = threadStack.get().peek();

		if (current == null && create) {
			return push(null);
		}
		return current;
	}

	protected Tracking create(String key) {

		Thread thread = Thread.currentThread();

		Tracking tracking = store.create(key != null ? key : thread.getName());
		tracking.begin();

		tracking.setTag("Host.Name", HOST_NAME);
		tracking.setTag("Host.Address", HOST_ADDRESS);
		tracking.setTag("Thread.id", Long.toString(thread.getId()));
		tracking.setTag("Thread.name", thread.getName());
		tracking.setTag("Thread.priority", Integer.toString(thread.getPriority()));

		return tracking;
	}
}
