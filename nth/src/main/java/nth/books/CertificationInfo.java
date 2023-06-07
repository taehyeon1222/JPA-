package nth.books;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class CertificationInfo { // 자격증

    @Id
    private  long id;

    private String CertificationName;

    @ManyToOne
    private Book bookID;



}
