package br.com.cubekode.jilotrack.track;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import br.com.cubekode.jilotrack.util.IOUtil;

public class Tracking implements Externalizable {

	private TrackStore store;

	private String id;

	private Track root;

	private Map<String, String> tags;

	private int trackIndex;

	private Deque<Track> stack;

	public Tracking(TrackStore store, String id, String key) {
		this.id = id;
		this.store = store;
		this.trackIndex = 0;
		this.root = new Track(this.id, this.trackIndex++, key);
		this.stack = new LinkedList<Track>();
	}
	
	public void setTag(String name, String value) {
		if (tags == null) {
			tags = new HashMap<String, String>();
		}
		tags.put(name, value);
	}

	public String getTag(String name) {
		if (tags == null) {
			return null;
		}
		return tags.get(name);
	}

	public Track begin() {
		if (store != null) {
			store.begin(this);
		}
		return begin(root);
	}

	public Track begin(String key) {
		// O parent eh o ultimo na pilha de execucao ou root.
		Track parent = stack.isEmpty() ? root : stack.peek();
		// Cria um novo track, adicionando no topo da pilha ou ao root.
		return begin(new Track(id, trackIndex++, key, parent));
	}

	public Track begin(Track track) {
		// inicializa o elemento.
		track.begin();
		// first track is root.
		if (root != track) {
			stack.push(track);
		}
		// chama o listener para registrar o evento.
		if (store != null) {
			store.begin(this, track);
		}
		return track;
	}

	public Track begin(Tracking tracking) {
		return begin(tracking.id);
	}

	public Track end(String key) {
		return end(key, null);
	}

	public Track end(String key, Throwable e) {

		Track track = null;

		// Busca pela execucao na pilha.
		for (Track t : stack) {
			if (key.equals(t.getKey())) {
				track = t;
				break;
			}
		}

		// Desempilha ate o ponto de termino.
		if (track != null) {

			String errorString = IOUtil.getErrorString(e);

			while (!stack.isEmpty()) {
				Track t = stack.pop();
				end(t, errorString);
				if (t == track) {
					break;
				}
			}
		}

		if (e != null) {
			setTag("error", Boolean.TRUE.toString());
		}

		return track;
	}

	public void end(Track track, String error) {
		track.setError(error);
		track.end();
		// chama o listener para registrar o evento.
		if (store != null) {
			store.end(this, track);
			// quando finaliza o root, o tracking tbm finaliza.
			if (track == root) {
				store.end(this);
			}
		}
	}

	public void end(Tracking tracking) {
		end(tracking.id);
	}

	public void end() {
		if (!stack.isEmpty()) {
			for (Track t = stack.pop(); !stack.isEmpty(); t = stack.pop()) {
				end(t, null);
			}
		}
		end(root, null);
	}

	public String getId() {
		return id;
	}

	public Map<String, String> getTags() {
		if (tags == null || tags.isEmpty()) {
			return Collections.emptyMap();
		}
		return new HashMap<String, String>(tags);
	}

	public Track getRoot() {
		return root;
	}

	public String getKey() {
		return root.getKey();
	}

	public long getBeginTime() {
		return root.getBeginTime();
	}

	public long getEndTime() {
		return root.getEndTime();
	}

	public long getTime() {
		return root.getTime();
	}

	public boolean isRunning() {
		return root.isRunning();
	}

	@Override
	public String toString() {
		return "Tracking[id=" + id + ", stack=" + stack.size() + ", data=" + tags.size() + "]";
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		IOUtil.writeFieldString(out, id);
		IOUtil.writeFieldObject(out, root);
		IOUtil.writeFieldObject(out, tags);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = IOUtil.readFieldString(in);
		root = (Track) IOUtil.readFieldObject(in);
		tags = (Map<String, String>) IOUtil.readFieldObject(in);
	}

	public void disabled() {
		if (store != null) {
			store.disabled(this);
			store = null;
		}
	}
}
