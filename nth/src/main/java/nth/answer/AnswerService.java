package nth.answer;

import lombok.RequiredArgsConstructor;
import nth.post.Post;
import nth.user.UserInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Post post, String content , UserInfo userInfo){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setPost(post);
        answer.setAuthor(userInfo);
        this.answerRepository.save(answer);
    }
}
