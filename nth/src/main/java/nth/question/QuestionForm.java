package nth.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {

    @Size
    @NotBlank(message = "제목은필수")
    private String subject;

    @NotBlank(message = "내용은필수")
    private String content;
}
