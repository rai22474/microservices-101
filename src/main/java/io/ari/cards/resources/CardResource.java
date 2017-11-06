package io.ari.cards.resources;

import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.cards.domain.Card;
import io.ari.cards.domain.repositories.CardsRepository;
import io.ari.cards.resources.assemblers.CardsAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.executable.ValidateOnExecution;
import java.util.Collection;

@RestController
@RequestMapping("cards/{id}")
public class CardResource {

    @RequestMapping(path = "/blocks", method = RequestMethod.POST)
    @ValidateOnExecution
    public ResponseEntity blockCard(@RequestHeader("x-customer-id") @NotEmpty String customerId,
                                    @PathVariable("id") String cardId) {
        Card card = cardsRepository.findById(cardId);

        if (card == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!card.getCustomerId().equals(customerId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Collection<Violation> canBeBlockedViolations = canBeBlocked(card);
        if (!canBeBlockedViolations.isEmpty()) {
            return resumeWithBlockViolations(canBeBlockedViolations);
        }

        card.block();
        Card blockedCard = cardsRepository.update(cardId, card);

        return ResponseEntity.status(HttpStatus.CREATED).body(cardsAssembler.convertEntityToDto(blockedCard));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ValidateOnExecution
    public ResponseEntity getCard(@RequestHeader("x-customer-id") @NotEmpty String customerId,
                                  @PathVariable("id") String cardId) {
        Card card = cardsRepository.findById(cardId);
        return ResponseEntity.ok(cardsAssembler.convertEntityToDto(card));
    }


    private Collection<Violation> canBeBlocked(Card card) {
        return blockValidator.validate(card);
    }

    private ResponseEntity resumeWithBlockViolations(Collection<Violation> violations) {
        if (violations.stream().anyMatch(violation -> "NoValidCardForBlocking".equals(violation.getCode()))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (violations.stream().anyMatch(violation -> "CardAlreadyBlocked".equals(violation.getCode()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private CardsAssembler cardsAssembler;

    @Autowired
    @Qualifier("cardBlockValidator")
    private BusinessRulesValidator<Card> blockValidator;
}