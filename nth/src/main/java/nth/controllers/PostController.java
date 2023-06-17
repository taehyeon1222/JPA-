package nth.controllers;

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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
//@RequestMapping("view/post")
@EnableMethodSecurity(prePostEnabled = true)
public class PostController {


    private final PostService postService;
    private final UserInfoService userInfoService;
    private final MyUtile myUtile;


    @GetMapping("/free")
    public String list(Principal principal, Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {

        Page<Post> paging = this.postService.getListkw(page, kw, "자유"); // 카테고리에서 검색
        model.addAttribute("paging", paging); // paging 추가
        model.addAttribute("kw", kw); // 검색기능 추가

        // 유저 정보를 가져와서 닉네임 추가를 해주기 위함
        myUtile.addUserInfoNiname(model, principal); //닉네임 추가

        System.out.println(model.getAttribute("err"));
        return "post/free";
    }
    @GetMapping("/post/detail/{id}")
    public String listjoin(Model model, Principal principal, @PathVariable("id") Long id) {
        myUtile.addUserInfoNiname(model, principal);
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        return "post/post_Details";
    }
    @PreAuthorize("isAuthenticated()") // 로그인이 아닐경우 로그인으로 리다이렉션
    @GetMapping("/post_form")
    public String postCreate(PostForm postForm, Model model, Principal principal) {
        model.addAttribute("postForm", postForm);
        myUtile.addUserInfoNiname(model, principal);
        return "post/post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post_form")
    public String postCreate(@Valid PostForm postForm,
                             BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post/post_form";
        }
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        Category category = new Category();
        category.setName("자유"); // 카테고리를 지정
        this.postService.create(postForm.getSubject(), postForm.getContent(), category, userInfo);
        return "redirect:/free";
    }


    //수정버튼 post/modify/${post.id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/modify/{id}")
    public String postModify(Model model,PostForm postForm, @PathVariable("id") long id
            , Principal principal) {
        Post post = this.postService.getPost(id);

        if (myUtile.checkAdmin(principal)) { //어드민권한 체크후 수정 가능
            postForm.setSubject(post.getSubject());
            postForm.setContent(post.getContent());
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        } else {// 유저닉네임 및 권한체크
            //myUtile.checkPermission(model,post, principal.getName());
        }
        if(!myUtile.checkUsername(post,principal.getName())){ // 유저 권한체크
            model.addAttribute("err","자신의 게시글만 수정 가능합니다.");
            System.out.println("유저권한이 일치 하지 않습니다.");
            return "user/login_form";
        }
        postForm.setSubject(post.getSubject());
        postForm.setContent(post.getContent());
        return "post/post_form";
    }

    //수정
    @PreAuthorize("isAuthenticated()")  // /post/modify
    @PostMapping("/post/modify/{id}")
    public String postModify(@Valid PostForm postForm,
                             BindingResult bindingResult, Principal principal,
                             @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            return "post/post_form";
        }
        Post post = this.postService.getPost(id);
        if (myUtile.checkAdmin(principal)) {
            this.postService.modify(post, postForm.getSubject(),postForm.getContent());
            return String.format("redirect:/post/detail/%s", id);
        }
       // myUtile.checkPermission(model,post, principal.getName()); // 유저 권한체크 후 오류 발생
        this.postService.modify(post, postForm.getSubject(),postForm.getContent());
        return String.format("redirect:/post/detail/%s", id);
    }

    //삭제버튼작동
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/delete/{id}")
    public String postDelete(Principal principal, @PathVariable("id") long id) {
        Post post = this.postService.getPost(id);

        if (myUtile.checkAdmin(principal)) { // 어드민권한 체크
            System.out.println("어드민 권한이 발생함");
            this.postService.delete(post);
            return "redirect:/";
        }
           // myUtile.checkPermission(post, principal.getName()); // 유저 권한체크 후 오류 발생

            this.postService.delete(post);
            return "redirect:/";
    }

    //추천버튼
    @GetMapping("post/detail/post/vote/{id}")
    public String qestionVote(Model model,Principal principal, @PathVariable("id") long id) {

        if(principal==null){
            model.addAttribute("err","로그인후이용해주세요"); //실행안됨
           return "user/login_form";
        }
        Post post = this.postService.getPost(id);
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        this.postService.vote(post, userInfo);
        return String.format("redirect:/post/detail/%s", id);
    }
}

//랜덤 userID 문자열 반환
            /* 개발일시 중지
        public String randomUserIdCreate(){
        String randomId = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String result = ""; //
        Random random = new Random(); //
        for(int i = 0; i < 5; i++) {
            int randomIndex = random.nextInt(randomId.length()); //
            char randomChar = randomId.charAt(randomIndex);
            result = result + randomChar; //
        }

        System.out.println(result);
        return result;
    }
*/

