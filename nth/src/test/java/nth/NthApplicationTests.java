package nth;

import nth.question.Question;
import nth.question.QuestionRepository;
import nth.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NthApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void test(){

		Question q1 = new Question();

		q1.setSubject("첫번째 질문이에요");
		q1.setContent("첫번째 질문의 상세글이에요");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);
	}

	@Test
	void test2(){

		Question q2 = this.questionRepository.findBySubject("첫번째 질문이에요");
		assertEquals(101,q2.getId());
	}

	/*
	@Test
	void test3(){
		for(int i = 1; i<300; i++){
			String subject1 = String.format("테스트 제목[%03d]",i);
			String content1 = "내용무";
			this.questionService.create(subject1,content1);
		}
	}
*/




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
