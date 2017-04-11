package catan.settlers.common.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class File {

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String path;

	/**
	 * Represents a file. Encapsulates the I/O operations
	 * 
	 * @param path
	 * @throws IOException
	 */
	public File(String path) throws IOException {
		this.path = path;
	}

	public void write(Serializable obj) {
		try {
			out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(obj);
			out.close();
		} catch (Exception e) {
			// Ignore
		}
	}

	public Object read() {
		try {
			in = new ObjectInputStream(new FileInputStream(path));
			Object obj = in.readObject();
			in.close();
			return obj;
		} catch (Exception e) {
			return null;
		}

	}
}
