package io.ari.moneyOrders.domain.recipients.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.bucks.domain.repositories.assemblers.BucksRecipientsStorageAssembler;
import io.ari.moneyOrders.domain.recipients.BucksRecipient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class BucksRecipientsStorageAssemblerDtoToEntityTest {

	@Test
	public void shouldNotReturnNullRecipient() {
		BucksRecipient recipient = bucksRecipientsStorageAssembler.convertDtoToDomainEntity(getRecipientData(), BUCKS_ID);

		assertNotNull("The recipient must be not null", recipient);
	}

	@Test
	public void shouldHaveABucks() {
		BucksRecipient recipient = bucksRecipientsStorageAssembler.convertDtoToDomainEntity(getRecipientData(), BUCKS_ID);

		assertEquals("The recipient must have the right bucks", BUCKS_ID, recipient.getTargetBucksId());
	}

	@Test
	public void shouldHaveAdditionalData() {
		Map<String, Object> recipientDto = getRecipientData();

		BucksRecipient recipient = bucksRecipientsStorageAssembler.convertDtoToDomainEntity(recipientDto, BUCKS_ID);

		assertEquals("The recipient must have the expected data", recipientDto, recipient.getData());
	}

	private Map<String, Object> getRecipientData() {
		HashMap<String, Object> recipientData = Maps.newHashMap();
		recipientData.put("name", "Tippy");
		recipientData.put("lastName", "Tap");
		recipientData.put("mobilePhone", "600151248");
		recipientData.put("targetBucksId", "potatoe");
		recipientData.put("id", "b7d4a7d4555ad5");
		return ImmutableMap.copyOf(recipientData);
	}

	@InjectMocks
	private BucksRecipientsStorageAssembler bucksRecipientsStorageAssembler;

	private static final String BUCKS_ID = "df9a75a49a4d9ad479";

}
