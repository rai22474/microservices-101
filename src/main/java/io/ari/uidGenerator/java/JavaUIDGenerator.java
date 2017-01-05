package io.ari.uidGenerator.java;


import io.ari.uidGenerator.UIDGenerator;

import java.util.UUID;

public class JavaUIDGenerator implements UIDGenerator {

	@Override
	public String generateUID() {
		return Long.toHexString(UUID.randomUUID().getMostSignificantBits()) + 
				Long.toHexString(UUID.randomUUID().getLeastSignificantBits()).substring(10);
	}
}
