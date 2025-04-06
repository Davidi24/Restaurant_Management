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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String productId;

    private String name;

    private String description;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private boolean isAvailable;

    @ElementCollection
    @CollectionTable(name = "product_customizations", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> customizationsAvailable;

    private String imageUrl;

    private int preparationTimeMinutes;
}
