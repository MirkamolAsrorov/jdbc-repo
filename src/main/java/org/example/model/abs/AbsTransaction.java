package org.example.model.abs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class AbsTransaction {
    private Long transaction_id;
    private Double amount;
}
