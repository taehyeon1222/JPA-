package nth.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {

    @Size
    @NotBlank(message = "제목은 필수")
    private String subject;

    @NotBlank(message = "내용은 필수")
    private String content;
}
