package nth.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerForm {

    @NotEmpty(message = "내용은 필수 입니다.")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;


}
