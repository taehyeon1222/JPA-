package nth.books;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String Price;

    @OneToOne(mappedBy = "bookPrice", cascade = CascadeType.ALL)
    private Book book;


}
