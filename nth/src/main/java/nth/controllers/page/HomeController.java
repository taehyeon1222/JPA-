package nth.controllers.page;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nth.post.Post;
import nth.post.PostService;
import org.springframework.data.domain.Page;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class HomeController {


    private final PostService postService;

    //공지사항
    @GetMapping("/home")
    public String home(){
        return "post/home";


    }
}