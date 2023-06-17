package nth.controllers.page;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nth.MyUtile;
import nth.post.Post;
import nth.post.PostService;
import org.springframework.data.domain.Page;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class HomeController {


    private final PostService postService;
    private final MyUtile myUtile;

    //공지사항
    @GetMapping("/home")
    public String home(HttpServletRequest request, Principal principal, Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {


        Page<Post> paging1 = this.postService.getListkw(page,5,kw,"공지");
        model.addAttribute("paging1", paging1); // paging 추가

        //Page<Post> paging2 = this.postService.getListVoter(kw,"자유", LocalDateTime.now(),page);
        //model.addAttribute("paging2", paging2); // paging 추가
       // model.addAttribute("kw", kw); // 검색기능 추가

        // 유저 정보를 가져와서 닉네임 추가를 해주기 위함
        myUtile.addUserInfoNiname(model, principal); //닉네임 추가
        return "post/home";
    }


}