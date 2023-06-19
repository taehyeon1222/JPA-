package nth.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nth.answer.Answer;
import nth.user.UserInfo;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
//빌더로 바꾸기
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "CLOB")
    private String content;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "post" , cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo author;

    @ManyToOne(cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    private Category category; //카테고리 지정

    @ManyToMany
    Set<UserInfo> voter;
}
