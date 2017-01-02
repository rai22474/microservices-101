package io.ari.cards.domain.factories;

import io.ari.cards.domain.Card;
import io.ari.cards.domain.repositories.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardsFactory {

	public Card createCard(String customerId, String customerName, String customerLastName, String bankingServiceAgreementId, String cardId, String type, String pan, String image, String status) {
		return createSavedCard(customerId, customerName, customerLastName, type, bankingServiceAgreementId, cardId, pan, image, status);
	}

	private Card createSavedCard(String customerId, String customerName, String customerLastName, String cardType, String bankingServiceAgreementId, String cardId, String cardPan, String cardImage, String status) {
		Card card = new Card(customerId, customerName, customerLastName, bankingServiceAgreementId, cardType, cardId, cardPan, cardImage,status);
		return cardsRepository.save(card);
	}

	@Autowired
	private CardsRepository cardsRepository;
}
