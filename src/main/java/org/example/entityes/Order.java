package org.example.entityes;

import lombok.*;
import javax.persistence.*;

@ToString
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "product_id")
    private Long productId;

    public Order(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
}
