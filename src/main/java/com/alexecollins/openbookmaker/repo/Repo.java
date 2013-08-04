package com.alexecollins.openbookmaker.repo;

import java.io.*;
import java.util.UUID;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class Repo<T extends Serializable> {

	private final File dir;

	public Repo() {
		this.dir = new File(".");
	}

	public Repo(File dir) {
		if (dir == null) {throw new IllegalArgumentException();}
		this.dir = dir;
	}

	public void add(UUID uuid, T placement) throws IOException {
		final File name = fileOf(uuid);
		try (final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name))) {
			out.writeObject(placement);
		}
	}

	@SuppressWarnings("unchecked")
	public T find(UUID uuid) throws IOException, ClassNotFoundException {
		final File name = fileOf(uuid);
		if (!name.exists()) {
			throw new FileNotFoundException(name + " not found");
		}

		try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(name))) {
			return (T) in.readObject();
		}
	}

	public void remove(UUID uuid) throws IOException {
		final File name = fileOf(uuid);
		if (!name.exists()) {
			throw new IOException(name + " does not exist");
		}
		if (!name.delete()) {
			throw new IOException("failed to delete " + name);
		}
	}

	private File fileOf(UUID uuid) {
		return new File(dir, uuid + ".ser");
	}

}
