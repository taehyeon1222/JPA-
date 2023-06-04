package nth.question;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import nth.answer.Answer;
import nth.user.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    /**
     * 등록순
     *
     * @param page
     * @return
     */
    public Page<Question> getList1(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.questionRepository.findAll(pageable);
    }

    /**
     * 작성시간순
     *
     * @param page : 페이징
     * @param kw   : kw 문자열 형태
     *             <p>
     *             page : 페이징 @RequestParam(value = "page",defaultValue = "0")
     *             int page
     * @return this.questionRepository.findAll(pageable);
     */
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate")); //작성시간순
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> specification = search(kw);
        return this.questionRepository.findAll(specification, pageable);

    }

    /**
     * @parm id 값을 받아와서 화면에 상세보기
     */
    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "question not found");
        }
    }

    /**
     * 유저 정보 까지 저장
     *
     * @param 제목
     * @param 내용
     */
    public void create(String s, String c, UserInfo userInfo) {
        Question question = new Question();
        question.setSubject(s);
        question.setContent(c);
        question.setAuthor(userInfo);
        question.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    //수정
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    /**
     * @param question : 질문내역
     * @param userInfo : 유저정보
     *                 유저정보로 추천값 상승
     */
    public void vote(Question question, UserInfo userInfo) {
        question.getVoter().add(userInfo);
        this.questionRepository.save(question);

    }



    /**
     * @Parm kw: 키워드
     */
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(false); // 중복을 제거
                Join<Question, UserInfo> u1 = root.join("author", JoinType.LEFT); //
                Join<Question, Answer> a = root.join("answerList", JoinType.LEFT);
                Join<Answer, UserInfo> u2 = a.join("author", JoinType.LEFT);
                return criteriaBuilder.or(
                        criteriaBuilder.isNull(u1.get("username")),
                        criteriaBuilder.like(u1.get("username"), "%" + kw + "%"));
            }
        };
    }
}



//criteriaBuilder.or(criteriaBuilder.like(root.get("subject"),"%"+kw+"%"),//제목
// criteriaBuilder.like(root.get("content"),"%"+kw+"%"), //내용
//criteriaBuilder.like(u1.get("username"),"%"+kw+"%"),//질문

// criteriaBuilder.like(a.get("content"),"%"+kw+"%"), //답변
// criteriaBuilder.like(u2.get("username"),"%"+kw+"%")); //답변

