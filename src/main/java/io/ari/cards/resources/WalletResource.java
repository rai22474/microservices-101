package io.ari.cards.resources;

import io.ari.cards.domain.Card;
import io.ari.cards.domain.repositories.CardsRepository;
import io.ari.cards.resources.assemblers.CardsAssembler;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.executable.ValidateOnExecution;
import java.util.Collection;

@RestController
@RequestMapping("cards")
public class WalletResource {

    @ValidateOnExecution
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findCards(@RequestHeader("x-customer-id") @NotEmpty String customerId) {
        Collection<Card> cards = cardsRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(cardsAssembler.convertEntitiesToDtos(cards));
    }



    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private CardsAssembler cardsAssembler;
}
