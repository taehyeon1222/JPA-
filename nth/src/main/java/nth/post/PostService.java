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

//    public List<Post> getList() {
//        return this.postRepository.findAll();
//    }

//    public Page<Post> getListVoter(String kw,String category,LocalDateTime time,int page) {
//        List<Sort.Order> sorts1 = new ArrayList<>();
//        sorts1.add(Sort.Order.desc("voter.size"));
//        Sort sort = Sort.by(sorts1);
//        //sorts1.add(Sort.Order.desc("createDate")); //작성시간순
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts1));
//        return this.postRepository.findAllByVoter(kw,category,time,pageable);
//    }

    /**
     *
     * @param page
     * @param kw
     * @param category
     * @return 검색기능제공 10 개 단위로 페이징중
     */
    public Page<Post> getListkw(int page, String kw,String category) {
        List<Sort.Order> sorts1 = new ArrayList<>();
        sorts1.add(Sort.Order.desc("createDate")); //작성시간순
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts1));
        return this.postRepository.findAllByKeyword(kw,pageable);
    }
    /**
     *
     * @param page
     * @param kw
     * @param category
     * @param size : 사이즈 조절
     * @return 검색기능제공 10 개 단위로 페이징중
     */

//    public Page<Post> getListkw(int page,int size,String kw,String category) {
//        List<Sort.Order> sorts1 = new ArrayList<>();
//        sorts1.add(Sort.Order.desc("createDate")); //작성시간순
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts1));
//        return this.postRepository.findAllByKeyword(kw,category,pageable);
//    }
    public Page<Post> getListkws(int page,int size,String category) {
        List<Sort.Order> sorts1 = new ArrayList<>();
        sorts1.add(Sort.Order.desc("createDate")); //작성시간순
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts1));
        return this.postRepository.findAllByKeyword(category,pageable);
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
     *
     * 제목
     * 내용
     * 카테고리
     * 유저정보
     *
     *
     */
    public void create(String subject, String content,Category category,UserInfo userInfo) {
        Post post = new Post();
        post.setSubject(subject);
        post.setContent(content);
        post.setAuthor(userInfo);
        post.setCategory(category); // 카테고리 설정
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





}


