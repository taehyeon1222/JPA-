package nth.books;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nth.answer.Answer;

import java.util.List;

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

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_price_id")
    private BookPrice bookPrice;

    @OneToMany(mappedBy = "bookID", cascade = CascadeType.REMOVE)
    private List<CertificationInfo> certificationInfoList;
}
