package nth.books;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
     * @param certificationName 관련자격증정보 저장
     * <br>
     *이름과 가격을 설정함
     *이름은 고유값으로 중복불가능
     */

    public Book create(BookDTO bookDTO) {
        Book book = new Book();
        book.setBookName(bookDTO.getBookName());
        book.setPrice(bookDTO.getPrice());
        return bookRepository.save(book); // 생성된 Book 객체 반환
    }



    }




