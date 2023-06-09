package nth.books;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookDTO {


    @NotEmpty(message = "내용은 필수 입니다.")
    private String bookName;

    //private String publisher; //출판사

    //private String link;

    @NotEmpty(message = "내용은 필수 입니다.")
    private String Price;

    private String certificationName;

}
