package io.ari.cards.domain.repositories;

import io.ari.cards.domain.Card;
import io.ari.cards.domain.repositories.assemblers.CardsStorageAssembler;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CardsRepository extends EntitiesRepository<Card> {

	public Card findByCustomerIdAndType(String customerId, String cardType) {
		/*Map<String, Object> query = ImmutableMap.of(
				"customerId", customerId,
				"cardType", cardType);
		return convertDtoToEntity(getReadRepository().findByKey(query).get());*/
		return new Card(customerId,cardType,"","","","","","","");
	}

	@Override
	public Card save(Card card) {
		seq++;
		card.setId(seq.toString());

		if (customerCards.get(card.getCustomerId()) != null) {
			customerCards.get(card.getCustomerId()).add(card);
		} else{
			ArrayList<Card> cards = new ArrayList<>();
			cards.add(card);
			customerCards.put(card.getCustomerId(),cards);
		}

		return super.save(card);
	}

	public void deleteCustomer(String customerId) {
		customerCards.get(customerId).stream().forEach(card -> {
			super.delete(card.getId());
		});

		customerCards.remove(customerId);
	}

	public Collection<Card> findByCustomerId(String customerId) {
		return customerCards.get(customerId);
	}

	private Map<String,ArrayList<Card>> customerCards = new HashMap<>();

	private Integer seq = 0;

	@Autowired
	public CardsRepository(@Qualifier("cardsStorageAssembler") CardsStorageAssembler storageAssembler) {
		super(storageAssembler);
	}

}
