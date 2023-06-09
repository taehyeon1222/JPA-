package nth;

import nth.books.*;
import nth.post.Post;
import nth.post.PostRepository;
import nth.post.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class NthApplicationTests {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private BookRepository bookRepository;

	private BookService bookService;

	@Autowired
	private CertificationInfoRepository certificationInfoRepository;


	@Autowired
	private PostService postService;

	@Test
	void test(){

		Post q1 = new Post();

		q1.setSubject("첫번째 질문이에요");
		q1.setContent("첫번째 질문의 상세글이에요");
		q1.setCreateDate(LocalDateTime.now());
		postRepository.save(q1);
	}

	@Test
	void test2(){

		Post q2 = this.postRepository.findBySubject("첫번째 질문이에요");
		assertEquals(101,q2.getId());
	}

	/*
	@Test
	void test3(){
		for(int i = 1; i<300; i++){
			String subject1 = String.format("테스트 제목[%03d]",i);
			String content1 = "내용무";
			this.postService.create(subject1,content1);
		}
	}
*/

//	@Test
//	public void create(BookDTO bookDTO) {
//		// 책 정보 저장
//		Book book = new Book();
//		book.setBookName(bookDTO.getBookName());
//		book.setPrice(bookDTO.getPrice());
//		bookRepository.save(book);
//
//		// 자격증 정보 저장
//		CertificationInfo certificationInfo1 = new CertificationInfo();
//		certificationInfo1.setCertificationName("자격증1");
//		certificationInfo1.setBookID(book);
//
//		CertificationInfo certificationInfo2 = new CertificationInfo();
//		certificationInfo2.setCertificationName("자격증2");
//		certificationInfo2.setBookID(book);
//
//		certificationInfoRepository.saveAll(Arrays.asList(certificationInfo1, certificationInfo2));
//	}


	@Test
	void test3(){
	String randomId = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	String result = ""; //
	Random random = new Random(); //
        for(int i = 0; i < 5; i++) {
		int randomIndex = random.nextInt(randomId.length()); //
		char randomChar = randomId.charAt(randomIndex);
		result = result + randomChar; //
	}

        System.out.println(result);
}

}
