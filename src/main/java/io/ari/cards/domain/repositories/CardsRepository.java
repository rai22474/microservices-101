package io.ari.cards.domain.repositories;

import io.ari.cards.domain.Card;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CardsRepository extends EntitiesRepository<Card> {

    public Card findByCustomerIdAndType(String customerId, String cardType) {
        return findByCustomerId(customerId).stream().findFirst().get();
    }

    @Override
    public Card save(Card card) {
        seq++;
        card.setId(seq.toString());

        if (customerCards.get(card.getCustomerId()) != null) {
            customerCards.get(card.getCustomerId()).add(card.getId());
        } else {
            ArrayList<String> cards = new ArrayList<>();
            cards.add(card.getId());
            customerCards.put(card.getCustomerId(), cards);
        }

        return super.save(card);
    }

    public void deleteCustomer(String customerId) {
        customerCards.get(customerId).forEach(super::deleteById);

        customerCards.remove(customerId);
    }

    public Collection<Card> findByCustomerId(String customerId) {
        return customerCards.get(customerId).stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    private Map<String, Collection<String>> customerCards = new HashMap<>();

    private Integer seq = 0;
}
