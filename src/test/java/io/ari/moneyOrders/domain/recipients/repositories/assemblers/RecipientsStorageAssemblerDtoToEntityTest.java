package io.ari.moneyOrders.domain.recipients.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.repositories.assemblers.BucksRecipientsStorageAssembler;
import io.ari.moneyOrders.domain.recipients.BucksRecipient;
import io.ari.moneyOrders.domain.recipients.Recipient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipientsStorageAssemblerDtoToEntityTest {

	@Test
	public void shouldReturnTheBucksRecipient()  {
		when(bucksRecipientsStorageAssembler.convertDtoToDomainEntity(bucksRecipientData, BUCKS_ID)).thenReturn(bucksRecipient);

		Recipient returnedRecipient = assembler.convertDtoToEntity(bucksRecipientData);

		assertNotNull("Returned recipient cannot be null.", returnedRecipient);
		assertEquals("Returned recipient must be the bucksRecipient.", bucksRecipient, returnedRecipient);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowErrorWhenNoTypeIsDefined() {
		assembler.convertDtoToEntity(noTypeRecipientData);
	}

	@InjectMocks
	private RecipientsStorageAssembler assembler;

	private Map<String, Object> bucksRecipientData = ImmutableMap.of("targetBucksId",BUCKS_ID ,"type","bucksRecipient");

	private Map<String, Object> noTypeRecipientData = ImmutableMap.of("email", "anEmail@aDomain.com");
	
	@Mock
	private BucksRecipientsStorageAssembler bucksRecipientsStorageAssembler;

	@Mock
	private BucksRecipient bucksRecipient;

	private static final String BUCKS_ID = "fda759da7509ad50d80";

}