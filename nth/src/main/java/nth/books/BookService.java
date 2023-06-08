package nth.books;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {


    private final BookRepository bookRepository;


    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     *
     * @param bookName 책이름
     * @param price 책 가격
     * <br>
     *이름과 가격을 설정함
     *이름은 고유값으로 중복불가능
     */
    public void create(String bookName,String price) {

        Book book = new Book();
        book.setBookName(bookName);
        book.setPrice(price);
        bookRepository.save(book);
    }


}
