package nth.question;

import lombok.RequiredArgsConstructor;
import nth.user.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList(){
        return this.questionRepository.findAll();
    }

    /**
     * 등록순
     * @param page
     * @return
     */
    public Page<Question> getList1(int page){
        Pageable pageable = PageRequest.of(page,10);
        return this.questionRepository.findAll(pageable);
    }

    /**
     * 작성시간순
     * @param page
     * @return
     */
    public Page<Question> getList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,10,Sort.by(sorts));
        return this.questionRepository.findAll(pageable);

    }

    /**

     @parm id 값을 받아와서 화면에 상세보기

     */
    public Question getQuestion(Long id){
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()){
            return question.get();
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"question not found");
        }
    }

    /**
     *유저 정보 까지 저장
     * @param 제목
     * @param 내용
     */
    public void create(String s, String c, UserInfo userInfo){
        Question question = new Question();
        question.setSubject(s);
        question.setContent(c);
        question.setAuthor(userInfo);
        question.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    //수정
    public void modify(Question question,String subject,String content){
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question){
        this.questionRepository.delete(question);
    }

    public void vote(Question question, UserInfo userInfo){
        question.getVoter().add(userInfo);
        this.questionRepository.save(question);

    }

}
