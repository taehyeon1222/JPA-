package nth.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

   // Post findBySubject(String subject);

    Page<Post> findAll(Pageable pageable);

    /**
     *
     * @param keyword
     * @param categoryName
     * @param pageable
     * @return 입력한 카테고리로 제목과 내용 아이디 검색
     */

    // N+1 해결
    @EntityGraph(attributePaths = {"author", "category", "answerList", "answerList.author", "voter"})
    @Query("SELECT p FROM Post p WHERE p.category.name = :categoryName")
    Page<Post> findAllByKeyword(@Param("categoryName") String categoryName, Pageable pageable);




//    @Query("SELECT p FROM Post p " +
//            "WHERE (p.author.username " +
//            "LIKE %:kw% OR p.author.nickname " +
//            "LIKE %:kw% OR p.subject " +
//            "LIKE %:kw% OR p.content " +
//            "LIKE %:kw%) " +
//            "AND p.category.name = :categoryName")
//    Page<Post> findAllByKeyword(@Param("kw") String keyword, @Param("categoryName") String categoryName, Pageable pageable);





}
