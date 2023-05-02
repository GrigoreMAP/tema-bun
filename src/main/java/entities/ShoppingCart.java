package entities;

import jakarta.persistence.*;
import lombok.*;
import model.ShoppingCartStatus;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
private Long id;

    @Column(name = "last_edited")
    private LocalDateTime lastEdited;

    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus status;

    @ToString.Exclude
    //lazy by default
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ShoppingCartItem> items = new LinkedHashSet<>();

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Customer customer;


}

