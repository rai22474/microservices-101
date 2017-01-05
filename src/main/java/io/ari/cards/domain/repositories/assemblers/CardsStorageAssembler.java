package io.ari.cards.domain.repositories.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.cards.domain.Card;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Component
public class CardsStorageAssembler extends StorageAssembler<Card> {

	@Override
	public Map<String, Object> convertEntityToDto(Card card) {
		Map<String, Object> cardDto = newHashMap(jacksonStorageAssembler.convertEntityToDto(card));
		cardDto.put("participants", ImmutableList.of(ImmutableMap.of("id", card.getCustomerId())));
		cardDto.put("type", "card");

		return ImmutableMap.copyOf(cardDto);
	}

	@Override
	public Card convertDtoToEntity(Map<String, Object> cardDto) {
		return  jacksonStorageAssembler.convertDtoToEntity(cardDto);
	}

	@Autowired
	@Qualifier("cardsStorageAssemblerJackson")
	private StorageAssembler<Card> jacksonStorageAssembler;
}
