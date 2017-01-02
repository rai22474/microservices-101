package io.ari.customers.resources;

import com.google.common.collect.ImmutableMap;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.customers.resources.assemblers.CustomersAssembler;
import io.ari.customers.resources.assemblers.exceptions.InvalidIdCard;
import io.ari.repositories.entities.EntitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;
import java.util.Map;

@RestController
@RequestMapping("customers")
public class CustomersResource {

    @ValidateOnExecution
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createCustomer(@RequestHeader("x-customer-id") String customerId,
                                         @RequestBody @NotNull Map<String, Object> customerData) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(addCustomer(customerId, customerData));
        } catch (CustomerExists customerExists) {
            return ResponseEntity.
                    status(HttpStatus.CONFLICT)
                    .body(ImmutableMap.of(
                            "code", customerExists.getCode(),
                            "description", customerExists.getDescription()));
        } catch (InvalidIdCard invalidIdCard) {
            return ResponseEntity.status(422)
                    .body(ImmutableMap.of(
                            "code", "invalidIdCard",
                            "description", "Attempting to create a customer with invalid idCard"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteCustomers() {
        customersRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> addCustomer(String customerId, Map<String, Object> customerData) throws CustomerExists, InvalidIdCard {
        Customer newCustomer = customersAssembler.convertDtoToEntity(customerId, customerData);

        customersRepository.update(customerId, newCustomer);
        return customersAssembler.convertEntityToDto();
    }

    @Autowired
    private CustomersAssembler customersAssembler;

    @Autowired
    @Qualifier("customersRepository")
    private EntitiesRepository<Customer> customersRepository;
}
