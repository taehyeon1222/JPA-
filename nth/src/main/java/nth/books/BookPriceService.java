package nth.books;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookPriceService {

    private final BookPriceRepository bPRepository;

    private final BookRepository bookRepository;


    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     *
     * @param bookname 책이름
     * @param price 책 가격
     * <br>
     *이름과 가격을 설정함
     *이름은 고유값으로 중복불가능
     */
    public void create(String bookname,String price) {
        /*
        Book book = new Book();
        book.setBookName(bookname); //북 이름 설정
        book = bookRepository.save(book);

        BookPrice bookPrice = new BookPrice();
        bookPrice.setPrice(price);
        bookPrice.setBook(book);
        bPRepository.save(bookPrice);
        오류 코드
        오류자체는 발생하지 않으나 데이터베이스상에서 원하지 않은 값이 저장됨.
        그 이유는 북 테이블이 먼저 저장 되고 그 다음 가격이 저장되기 때문에
        북 테이블에서 북 가격 테이블의 id 값이 들어오지 않음
        아래 코드는 서로 바꿔줌
         */
        BookPrice bookPrice = new BookPrice();
        bookPrice.setPrice(price);
        bookPrice = bPRepository.save(bookPrice);

        Book book = new Book();
        book.setBookName(bookname);
        book.setBookPrice(bookPrice);
        bookRepository.save(book);
    }


}
