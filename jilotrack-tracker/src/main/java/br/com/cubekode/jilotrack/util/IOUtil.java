package br.com.cubekode.jilotrack.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class IOUtil {

	public static String getErrorString(Throwable e) {
		return e != null ? e.toString() : null;
	}

	public static String getStackTraceString(Throwable e) {
		if (e != null) {
			StringWriter s = new StringWriter(2048);
			e.printStackTrace(new PrintWriter(s));
			return s.toString();
		}
		return null;
	}

	public static Object readObjectFromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
		return readObject(new ByteArrayInputStream(bytes));
	}

	public static Object readObject(InputStream in) throws IOException, ClassNotFoundException {
		return (new ObjectInputStream(in)).readObject();
	}

	public static byte[] writeObjectToByteArray(Object object) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		writeObject(bytes, object);
		return bytes.toByteArray();
	}

	public static void writeObject(OutputStream out, Object object) throws IOException {
		(new ObjectOutputStream(out)).writeObject(object);
	}

	public static void writeFieldObject(ObjectOutput out, Object obj) throws IOException {
		if (writeField(out, obj)) {
			out.writeObject(obj);
		}
	}

	public static Object readFieldObject(ObjectInput in) throws IOException, ClassNotFoundException {
		return readField(in) ? in.readObject() : null;
	}

	public static void writeFieldInteger(ObjectOutput out, Integer i) throws IOException {
		if (writeField(out, i)) {
			out.writeInt(i);
		}
	}

	public static Integer readFieldInteger(ObjectInput in) throws IOException {
		return readField(in) ? in.readInt() : null;
	}

	public static void writeFieldString(ObjectOutput out, String str) throws IOException {
		if (writeField(out, str)) {
			out.writeUTF(str);
		}
	}

	public static String readFieldString(ObjectInput in) throws IOException {
		return readField(in) ? in.readUTF() : null;
	}

	public static boolean writeField(ObjectOutput out, Object value) throws IOException {
		boolean hasValue = (value != null);
		out.writeByte(hasValue ? 1 : 0);
		return hasValue;
	}

	public static boolean readField(ObjectInput in) throws IOException {
		return in.readByte() == 1;
	}

	public static InputStream getResourceStream(String name) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
	}

	public static List<URL> getResourceList(String name) throws IOException {
		return Collections.list(Thread.currentThread().getContextClassLoader().getResources(name));
	}

	public static URL getResourceURL(String name) throws IOException {
		return Thread.currentThread().getContextClassLoader().getResource(name);
	}
}
