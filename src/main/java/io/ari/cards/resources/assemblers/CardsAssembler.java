package io.ari.cards.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.cards.domain.Card;
import io.ari.cards.domain.factories.CardsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CardsAssembler {

    public Map<String, Object> convertEntityToDto(Card card) {
        String cardId = card.getId();

        HashMap<String, Object> cardDto = new HashMap<>();
        cardDto.put("type", card.getType());
        cardDto.put("id", cardId);
        cardDto.put("bankingServiceCardId", card.getBankingServiceCardId());
        cardDto.put("pan", card.getPan());
        cardDto.put("image", card.getImage());
        cardDto.put("name", card.getCustomerName());
        cardDto.put("lastName", card.getCustomerLastname());
        cardDto.put("status", card.getStatus().toString());
        cardDto.put("_links", hypermediaAssembler.createHypermedia("api/cards/" + cardId, "ari-read"));

        return cardDto;
    }

    public Map<String, Object> convertEntitiesToDtos(Collection<Card> cards) {
        Collection<Map<String, Object>> cardsDto = cards
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
        return ImmutableMap.of("items", cardsDto);
    }

    @Autowired
    private HypermediaAssembler hypermediaAssembler;

    @Autowired
    private CardsFactory cardsFactory;
}
