package nth.question;

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

public class Question {
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

    @OneToMany(mappedBy = "question" , cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private UserInfo author;

    @ManyToMany
    Set<UserInfo> voter;
}
