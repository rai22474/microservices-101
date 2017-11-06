package io.ari.customers.domain.repositories;

import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.cards.domain.repositories.CardsRepository;
import io.ari.customers.domain.Customer;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CustomersRepository extends EntitiesRepository<Customer> {

    public Optional<Customer> findByMobilePhone(String mobilePhone) {
        return Optional.ofNullable(super.findById(mobilePhones.get(mobilePhone)));
    }

    public Optional<Customer> findByIdCard(String idCard) {
        return Optional.ofNullable(super.findById(idCards.get(idCard)));
    }

    public Customer save(Customer customer) {
        idCards.put(customer.getIdCard(), customer.getId());
        mobilePhones.put(customer.getMobilePhone(), customer.getId());

        return super.save(customer);
    }

    public void delete(String id) {
        Customer customer = findById(id);

        if (customer == null){
            return;
        }

        bucksRepository.deleteCustomer(id);
        cardsRepository.deleteCustomer(id);

        idCards.remove(customer.getIdCard());
        mobilePhones.remove(customer.getMobilePhone());
        super.deleteById(id);
    }

    @Autowired
    private BucksRepository bucksRepository;

    @Autowired
    private CardsRepository cardsRepository;

    private Map<String, String> idCards = new HashMap<>();

    private Map<String, String> mobilePhones = new HashMap<>();
}
