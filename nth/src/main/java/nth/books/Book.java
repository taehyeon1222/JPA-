package nth.books;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;

    @Column(unique = true)
    private String bookName;

    private String publisher; //출판사

    private String link; // 구매연결링크

    @OneToOne
    @JoinColumn(name = "book_price_id")
    private BookPrice bookPrice;
    //추후 이미지 추가
}
