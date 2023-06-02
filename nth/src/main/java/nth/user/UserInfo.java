package nth.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class UserInfo { // 이름 바꿔야함

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(length = 60)
    private String password;

    private String email;

   // @CreatedDate
  //  private LocalDateTime creatDate;
}
