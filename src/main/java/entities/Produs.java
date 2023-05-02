package entities;

import exception.InsufficientStockException;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "produs")
public class Produs {

    @Id
    private String isbn;

    private String title;

    private double price;

    private int stock;

    @Column(name = "year_fabrication")
    private LocalDate yearfabrication;


//    @Column(name = "cover_type")
//    @Enumerated(EnumType.STRING)
//    private CoverType coverType;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "producators_produs",
            joinColumns = {@JoinColumn(name = "isbn_produs", referencedColumnName = "isbn")},
            inverseJoinColumns = {@JoinColumn(name = "id_producator", referencedColumnName = "id")}
    )
    private Set<Producator> producators = new LinkedHashSet<>();

    public void checkStock(int quantity) throws InsufficientStockException {
        if (this.stock + quantity < 0) {
            throw new InsufficientStockException("Insufficient stock.");
        }
    }
}
