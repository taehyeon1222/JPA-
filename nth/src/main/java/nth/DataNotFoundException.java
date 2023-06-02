package nth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//사용하지 않음 111p

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "entity not found")
public class DataNotFoundException extends RuntimeException{

    private static final long serivalVersionUID = 1L;

    public DataNotFoundException(String meg){
        super(meg);
    }
}
