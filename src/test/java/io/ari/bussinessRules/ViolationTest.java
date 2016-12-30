package io.ari.bussinessRules;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class ViolationTest {
	
	@Test
	public void shouldBeEquals(){
		EqualsVerifier.forClass(Violation.class)
		.usingGetClass()
		.verify();
	}
}
