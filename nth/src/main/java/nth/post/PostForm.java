package nth.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {

    @NotBlank(message = "제목은 필수 입니다.")
    private String subject;

    @NotBlank(message = "내용은 필수에용")
    private String content;
}
