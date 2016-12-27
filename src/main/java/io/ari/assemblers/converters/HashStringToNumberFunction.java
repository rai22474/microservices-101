package io.ari.assemblers.converters;

import com.google.common.base.Function;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashStringToNumberFunction implements Function<String, Integer> {

	private static final Integer UPPER_LIMIT = 1000;

	@Override
	public Integer apply(String initialString) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(initialString.getBytes());
			ByteBuffer wrapped = ByteBuffer.wrap(digest);
			Integer hashInInteger = Math.abs(wrapped.getInt());

			return adaptNumberToUpperLimit(hashInInteger);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	Integer adaptNumberToUpperLimit(Integer hashInInteger) {
		return hashInInteger < UPPER_LIMIT ? hashInInteger : truncateHash(hashInInteger);
	}

	private Integer truncateHash(Integer hashInInteger) {
		return hashInInteger % UPPER_LIMIT;
	}
}
