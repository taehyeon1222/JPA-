package nth.answer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import nth.question.Question;
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
    private Question question;

    @ManyToOne
    private UserInfo author;

    @ManyToMany
    Set<UserInfo> voter;



}
