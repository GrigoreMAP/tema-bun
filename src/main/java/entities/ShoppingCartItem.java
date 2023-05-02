package entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
//TODO - please read
//there is a bidirectional relationship between ShoppingCartItem and ShoppingCart
//=> when calling equals or hashcode on any of the sides of the relationship,
//it will generate StackOverflow error -> equals/hashcode will be called infinitely :)
//that is why we should be mindful about the fields that we use on equals and hashcode
@EqualsAndHashCode(of="id") //uses just the id (which is unique) field to generate hashcode and equals
@Getter
@Setter
@Entity
@Table(name = "shopping_cart_items")
@NamedQueries({
        @NamedQuery(name = "getQuantity",
                query = "select sci.quantity from ShoppingCartItem sci where sci.produs.isbn=:isbn and sci.shoppingCart.customer.id=:id")
})
public class ShoppingCartItem {
    @EmbeddedId
    private ShoppingCartItemPK id;

    private int quantity;

    @ToString.Exclude
    @ManyToOne
    @MapsId("shoppingCartId")
    @JoinColumn(name = "id_shopping_cart")
    private ShoppingCart shoppingCart;

    @ToString.Exclude
    @ManyToOne
    @MapsId("isbnProdus")
    @JoinColumn(name = "isbn_produs")
    private Produs produs;

}

