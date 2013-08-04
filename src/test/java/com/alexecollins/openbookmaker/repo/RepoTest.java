package com.alexecollins.openbookmaker.repo;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class RepoTest {

	private Repo<String> sut;

	@Before
	public void setUp() throws Exception {
		sut = new Repo<>(new File(System.getProperty("java.io.tmpdir")));
	}

	@Test
	public void testGoodFile() throws Exception {
		final UUID uuid = UUID.randomUUID();
		sut.add(uuid, "test");
		assertEquals("test", sut.find(uuid));

	}

	@Test(expected = IOException.class)
	public void testBadFile() throws Exception {

		sut.find(UUID.randomUUID());

	}

	@Test
	public void testDeleteFile() throws Exception {
		final UUID uuid = UUID.randomUUID();
		sut.add(uuid, "test");
		sut.remove(uuid);
	}
}
