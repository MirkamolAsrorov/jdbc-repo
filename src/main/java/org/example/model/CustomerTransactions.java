package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerTransactions {
    private Integer transaction_id;
    private Double amount;
    private Customer customer;

}
