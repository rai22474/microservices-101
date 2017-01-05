package io.ari.moneyOrders.domain.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyOrders.domain.recipients.repositories.assemblers.RecipientsStorageAssembler;
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
public class RecipientsStorageAssemblerEntityToDtoTest {

	@Test
	public void shouldNotReturnNull() {
		when(recipient.getType()).thenReturn(RECIPENT_TYPE);
		Map<String, Object> returnedDto = assembler.convertEntityToDto(recipient);

		assertNotNull("Returned dto cannot be null.", returnedDto);
	}

	@Test
	public void shouldReturnTheRightRecipientDto() {
		when(recipient.getType()).thenReturn(RECIPENT_TYPE);
		when(recipient.getData()).thenReturn(recipientData);
		
		Map<String, Object> returnedDto = assembler.convertEntityToDto(recipient);

		assertEquals("Returned dto must be equals to the recipient additional data.", expectedData, returnedDto);
	}

	@InjectMocks
	private RecipientsStorageAssembler assembler;

	@Mock
	private Recipient recipient;

	private Map<String, Object> recipientData = ImmutableMap.of("mobilePhone", "685685685");

	private Map<String, Object> expectedData = ImmutableMap.of("mobilePhone", "685685685","type",RECIPENT_TYPE);
	
	private static final String RECIPENT_TYPE = "recipentType";

}