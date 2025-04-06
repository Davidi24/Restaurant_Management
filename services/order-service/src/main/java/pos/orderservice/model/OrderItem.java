package pos.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID (e.g., auto-increment)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private String specialInstructions;

    @ElementCollection
    @CollectionTable(name = "order_item_customizations", joinColumns = @JoinColumn(name = "order_item_id"))
    @Column(name = "customization")
    private List<String> customizations;


}
