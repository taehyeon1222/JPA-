package nth.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    //카테고리 레파지토리 06-11
    Optional<Category> findByName(String name);

}
