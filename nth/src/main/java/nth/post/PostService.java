package nth.post;

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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getList() {
        return this.postRepository.findAll();
    }

    /**
     * 등록순
     *
     * @param page
     * @return
     */
    public Page<Post> getList1(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.postRepository.findAll(pageable);
    }

    /**
     * 작성시간순
     *
     * @param page : 페이징
     * @param kw   : kw 문자열 형태
     *             <p>
     *             page : 페이징 @RequestParam(value = "page",defaultValue = "0")
     *             int page
     * @return this.postRepository.findAll(pageable);
     */
    public Page<Post> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate")); //작성시간순
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Post> specification = search(kw);
        return this.postRepository.findAll(specification, pageable);

    }

    /**
     * @parm id 값을 받아와서 화면에 상세보기
     */
    public Post getPost(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "post not found");
        }
    }

    /**
     * 유저 정보 까지 저장
     *
     * @param 제목
     * @param 내용
     */
    public void create(String s, String c, UserInfo userInfo) {
        Post post = new Post();
        post.setSubject(s);
        post.setContent(c);
        post.setAuthor(userInfo);
        post.setCreateDate(LocalDateTime.now());
        this.postRepository.save(post);
    }
    //수정
    public void modify(Post post, String subject, String content) {
        post.setSubject(subject);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        this.postRepository.save(post);
    }

    public void delete(Post post) {
        this.postRepository.delete(post);
    }

    /**
     * @param post : 질문내역
     * @param userInfo : 유저정보
     *                 유저정보로 추천값 상승
     */
    public void vote(Post post, UserInfo userInfo) {
        post.getVoter().add(userInfo);
        this.postRepository.save(post);

    }



    /**
     * @Parm kw: 키워드
     */
    private Specification<Post> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(false); // 중복을 제거
                Join<Post, UserInfo> u1 = root.join("author", JoinType.LEFT); //
                Join<Post, Answer> a = root.join("answerList", JoinType.LEFT);
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

