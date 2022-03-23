package org.dotpay.challenge.repos;

import org.dotpay.challenge.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, String> {

    Customer findByAccountNumber(String senderAccountNumber);
    
}
