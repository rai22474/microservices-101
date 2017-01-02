package io.ari.bucks.domain.factories;


import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.cards.domain.factories.CardsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BucksFactory {

    public Bucks createBucks(String customerId, String idCard, String mobilePhone, String customerName, String customerLastName) {
        seq++;
        String agreementId = seq.toString();
        Bucks newBucks = new Bucks(customerId, seq.toString());
        createCard(customerId, customerName, customerLastName,agreementId);

        return bucksRepository.save(newBucks);
    }

    private void createCard(String customerId, String customerName, String customerLastName,String agreementId) {
        /*String cardId = (String) cardData.get("cardId");
		String cardType = (String) cardData.get("type");
		String pan = (String) cardData.get("pan");
		String image = (String) cardData.get("image");
		String status = (String) cardData.get("status");
*/
        String cardId = "9afj98asdf";
        String cardType = "tva";
        String pan = "400000******2419";
        String image = "virtual-card.png";
        String status = "active";

		cardsFactory.createCard(customerId, customerName, customerLastName, agreementId, cardId, cardType, pan, image, status);
    }

    private Integer seq = 0;

    @Autowired
    private BucksRepository bucksRepository;

	@Autowired
	private CardsFactory cardsFactory;

}
