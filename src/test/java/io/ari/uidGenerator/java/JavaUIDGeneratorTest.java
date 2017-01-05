package io.ari.uidGenerator.java;

import io.ari.uidGenerator.java.JavaUIDGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaUIDGeneratorTest {

	@Test
	public void shouldReturnANotNullString() {
		String generateUID = new JavaUIDGenerator().generateUID();
		assertNotNull("The generated uid must be not null",generateUID);
	}

	@Test
	public void shouldReturnATwentyTwo() {
		String generateUID = new JavaUIDGenerator().generateUID();
		assertEquals("The generated uid must have 22 character",22,generateUID.length());
	}
}
