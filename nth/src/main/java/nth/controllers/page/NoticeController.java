package nth.controllers.page;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nth.MyUtile;
import nth.post.Category;
import nth.post.Post;
import nth.post.PostForm;
import nth.post.PostService;
import nth.user.UserInfo;
import nth.user.UserInfoService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
@RequiredArgsConstructor

public class NoticeController {


    private final UserInfoService userInfoService;
    private final MyUtile myUtile;
    private final PostService postService;

    //공지사항

    @GetMapping("/notice")
    public String home(HttpServletRequest request, Principal principal, Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {

        // Page<Post> paging = this.postService.getList(page,kw);//키워드 추가
        //Page<Post> paging = this.postService.getNoticeList(page, kw); // 공지
        Page<Post> paging = this.postService.getListkw(page,kw,"공지");
        model.addAttribute("paging", paging); // paging 추가
        model.addAttribute("kw", kw); // 검색기능 추가

        // 유저 정보를 가져와서 닉네임 추가를 해주기 위함
        myUtile.addUserInfoNiname(model, principal); //닉네임 추가
        return "post/notice";
    }
    @PreAuthorize("isAuthenticated()") // 로그인이 아닐경우 로그인으로 리다이렉션
    @GetMapping ("notice/post_form")
    public String postCreate(PostForm postForm, Model model, Principal principal){

        if(myUtile.checkAdmin(principal)){
            model.addAttribute("postForm",postForm);;
            myUtile.addUserInfoNiname(model,principal);
            return "post/post_form";
        }
        return "redirect:post/home";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/notice/post_form")
    public String postCreate(@Valid PostForm postForm,
                             BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "post/post_form";
        }
       if(myUtile.checkAdmin(principal)){
           UserInfo userInfo = this.userInfoService.getUser(principal.getName());
           Category category = new Category();
           category.setName("공지"); // 카테고리를 지정
           this.postService.create(postForm.getSubject(), postForm.getContent(),category,userInfo);
       }else {
           System.out.println("관리자 권한이 없습니다.");
       }

        return "redirect:/notice";
    }

}
