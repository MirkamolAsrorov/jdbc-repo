package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    private String customer_id;
    private String first_name;
    private String last_name;

    public Customer(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
