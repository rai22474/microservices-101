package io.ari.customers.resources.assemblers;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static java.lang.Integer.parseUnsignedInt;

@Component
public class IdCardValidator {

	public boolean isValid(String idCard) {
		if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
			return false;
		}

		Character controlChar = idCard.charAt(idCard.length() - 1);
		String cardNumber = idCard.substring(0, idCard.length() - 1);

		return controlChar.equals(calculateExpectedCharacter(cardNumber));
	}

	private Character calculateExpectedCharacter(String idCardPrefix) {

		String idNumber = idCardPrefix
				.replaceFirst("^X", "0")
				.replaceFirst("^Y", "1")
				.replaceFirst("^Z", "2");
		return NIF_CHARS.charAt(parseUnsignedInt(idNumber) % 23);
	}

	private static final String NIF_CHARS = "TRWAGMYFPDXBNJZSQVHLCKE";

	private static final Pattern ID_CARD_PATTERN = Pattern.compile("(([X-Z])(\\d{7})([A-Z]))|((\\d{8})([" + NIF_CHARS + "]))");

}
