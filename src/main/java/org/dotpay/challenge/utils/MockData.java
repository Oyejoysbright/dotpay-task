package org.dotpay.challenge.utils;

import java.util.LinkedList;

import org.dotpay.challenge.dtos.TransactionRequest;
import org.dotpay.challenge.entities.Customer;
import org.dotpay.challenge.enums.CurrencyEnum;

public class MockData {

    public static LinkedList<Customer> getCustomers() {
        LinkedList<Customer> customers = new LinkedList<>();
        customers.add(new Customer(1000000001, 000001, "First Bank", 50000.0));
        customers.add(new Customer(1000000002, 000001, "First Bank", 2000.0));
        customers.add(new Customer(1000000003, 000002, "GT Bank", 140074.25));
        return customers;
    }

    public static Customer getCustomer(int index) {
        return getCustomers().get(index);
    }

    public static TransactionRequest getIntraTransferRequestInstance() {
        return new TransactionRequest(
            1000, 
            "This is an intra bank transfer", 
            CurrencyEnum.NGN, 
            getCustomer(0).getAccountNumber(),
            getCustomer(1).getAccountNumber()
        );
    }

    public static TransactionRequest getInterTransferRequestInstance() {
        return new TransactionRequest(
            1000, 
            "This is an inter bank transfer", 
            CurrencyEnum.NGN, 
            getCustomer(0).getAccountNumber(),
            getCustomer(2).getAccountNumber()
        );
    }
}
