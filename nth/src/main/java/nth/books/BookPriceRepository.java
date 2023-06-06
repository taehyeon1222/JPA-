package nth.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface BookPriceRepository extends JpaRepository<BookPrice,Long> {

    //List<BookPrice> findByBook_BookName(String bookName);


}
