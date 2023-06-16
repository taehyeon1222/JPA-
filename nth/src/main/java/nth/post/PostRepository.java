package nth.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findBySubject(String subject);
    Post findBySubjectAndContent(String subject, String content);
    List<Post> findBySubjectLike(String subject); // 특정 문자열
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);//검색

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.category c WHERE c.name = 'notice'",
            countQuery = "SELECT COUNT(p) FROM Post p JOIN p.category c WHERE c.name = 'notice'")
    Page<Post> findFreePosts(Specification<Post> specification, Pageable pageable);


//    @Query("SELECT p FROM Post p JOIN FETCH p.category c WHERE c.name = 'free'")
//    Page<Post> findFreePosts();

}
