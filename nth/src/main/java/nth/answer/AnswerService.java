package nth.answer;

import lombok.RequiredArgsConstructor;
import nth.question.Question;
import nth.user.UserInfo;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Question question, String content , UserInfo userInfo){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(userInfo);
        this.answerRepository.save(answer);
    }
}
