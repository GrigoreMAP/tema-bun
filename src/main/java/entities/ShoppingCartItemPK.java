package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable

public class ShoppingCartItemPK implements Serializable {

    @Column(name = "isbn_produs")
    private String isbnProdus;

    @Column(name = "id_shopping_cart")
    private Long shoppingCartId;
}
