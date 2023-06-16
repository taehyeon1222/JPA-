package nth.controllers.page;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nth.MyUtile;
import nth.post.Post;
import nth.post.PostService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
@RequiredArgsConstructor

public class NoticeController {


    private final PostService postService;
    private final MyUtile myUtile;

    //공지사항
    @GetMapping("/notice")
    public String home(HttpServletRequest request, Principal principal, Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {

        // Page<Post> paging = this.postService.getList(page,kw);//키워드 추가
        Page<Post> paging = this.postService.getfreeList(page, kw);
        model.addAttribute("paging", paging); // paging 추가
        model.addAttribute("kw", kw); // 검색기능 추가

        // 유저 정보를 가져와서 닉네임 추가를 해주기 위함
        myUtile.addUserInfoNiname(model, principal); //닉네임 추가
        return "post/list";


    }
}
