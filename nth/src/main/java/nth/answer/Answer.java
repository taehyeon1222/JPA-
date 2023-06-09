package nth.answer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nth.post.Post;
import nth.user.UserInfo;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "CLOB")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    private Post post;

    @ManyToOne
    private UserInfo author;

    @ManyToMany
    Set<UserInfo> voter;



}
