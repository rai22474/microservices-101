package io.ari.bucks.domain.factories;


import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BucksFactory {

    public Bucks createBucks(String customerId, String idCard, String mobilePhone, String customerName, String customerLastName) {
        //Map<String, Object> bankingServiceAgreementData = bankingService.createAgreement(idCard, mobilePhone);
        seq++;
        Bucks newBucks = new Bucks(customerId, seq.toString());
        //createCard(customerId, customerName, customerLastName, bankingServiceAgreementData);

        return bucksRepository.save(newBucks);

    }

    private void createCard(String customerId, String customerName, String customerLastName, Map<String, Object> bankingServiceAgreementData) {
/*
        String agreementId = (String) bankingServiceAgreementData.get("agreementId");
		Map<String, Object> cardData = (Map<String, Object>) bankingServiceAgreementData.get("card");
		String cardId = (String) cardData.get("cardId");
		String cardType = (String) cardData.get("type");
		String pan = (String) cardData.get("pan");
		String image = (String) cardData.get("image");
		String status = (String) cardData.get("status");

		cardsFactory.createCard(customerId, customerName, customerLastName, agreementId, cardId, cardType, pan, image, status);
		*/

    }

    private Integer seq = 0;

    @Autowired
    private BucksRepository bucksRepository;

	/*
	@Autowired
	private BankingService bankingService;

	@Autowired
	private CardsFactory cardsFactory;
*/
}
