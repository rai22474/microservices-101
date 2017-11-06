package io.ari.customers.resources;

import io.ari.customers.domain.Customer;
import io.ari.customers.domain.repositories.CustomersRepository;
import io.ari.customers.resources.assemblers.CustomersAssembler;
import io.ari.customers.resources.assemblers.CustomersUpdateAssembler;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.executable.ValidateOnExecution;
import java.util.Map;

@RestController
@RequestMapping("me")
public class CustomerResource {

    @ValidateOnExecution
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity me(@RequestHeader("x-customer-id") @NotEmpty String customerId) {
        Customer customer = customersRepository.findById(customerId);

        if (customer == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(customersAssembler.convertEntityToDto(customer));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteMyCustomer(@RequestHeader("x-customer-id") String customerId) {
        customersRepository.delete(customerId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateCustomer(@RequestHeader("x-customer-id") String customerId,
                                         @RequestBody Map<String, Object> customerData) {
        Customer updatedCustomer = customersAddressAssembler.convertDtoToExistingEntity(customerId, customerData);
        customersRepository.update(customerId, updatedCustomer);

        return ResponseEntity.noContent().build();
    }

    @Autowired
    private CustomersUpdateAssembler customersAddressAssembler;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private CustomersAssembler customersAssembler;

}