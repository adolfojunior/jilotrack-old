package br.com.cubekode.jilotrack.util;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JSON<T extends JSON<T>> implements Appendable {

	private Appendable appendable;

	public JSON(Appendable appendable) {
		this.appendable = appendable;
	}

	public Appendable getAppendable() {
		return appendable;
	}

	public T openObject() throws IOException {
		return append('{');
	}

	public T string(Object obj) throws IOException {
		if (obj == null) {
			return append("null");
		}
		return string(obj.toString());
	}

	public T string(CharSequence sequence) throws IOException {
		append('"');
		if (sequence != null) {
			append(sequence.toString().replace("\\", "\\" + "\\").replace("\"", "\\" + "\""));
		}
		return append('"');
	}

	public T property(Object property) throws IOException {
		return string(property).append(':');
	}

	public T separator() throws IOException {
		return append(',');
	}

	public T closeObject() throws IOException {
		return append('}');
	}

	public T nullRef() throws IOException {
		return append("null");
	}
	
	public T openArray() throws IOException {
		return append('[');
	}

	public T closeArray() throws IOException {
		return append(']');
	}

	public T map(Map<?, ?> map) throws IOException {
		openObject();
		int count = 0;
		for (Entry<?, ?> info : map.entrySet()) {
			if (count++ > 0) {
				separator();
			}
			property(info.getKey()).string(info.getValue());
		}
		return closeObject();
	}

	public T numberArray(List<? extends Number> list) throws IOException {
		openArray();
		int count = 0;
		for (Number item : list) {
			if (count++ > 0) {
				separator();
			}
			number(item);
		}
		return closeArray();
	}

	public T stringArray(Collection<?> list) throws IOException {
		openArray();
		int count = 0;
		for (Object item : list) {
			if (count++ > 0) {
				separator();
			}
			string(item);
		}
		return closeArray();
	}

	public T number(Number n) throws IOException {
		return append(n == null ? "0" : n.toString());
	}

	@SuppressWarnings("unchecked")
	public T append(CharSequence csq) throws IOException {
		appendable.append(csq);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T append(CharSequence csq, int start, int end) throws IOException {
		appendable.append(csq, start, end);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T append(char c) throws IOException {
		appendable.append(c);
		return (T) this;
	}

	@Override
	public String toString() {
		return appendable.toString();
	}
}
