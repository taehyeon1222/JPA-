package nth.books;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class CertificationInfo { // 자격증

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long id;

    private String CertificationName;

    @ManyToOne
    private Book book;


}
