package nth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nth.DataNotFoundException;
import nth.post.Post;
import nth.post.PostForm;
import nth.post.PostService;
import nth.user.UserInfo;
import nth.user.UserInfoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Random;

@Controller
@RequiredArgsConstructor
//@RequestMapping("view/post")
@EnableMethodSecurity(prePostEnabled = true)
public class PostController {


    private final PostService postService;
    private final UserInfoService userInfoService;

    @GetMapping("/list")
    public String list(HttpServletRequest request,Principal principal,Model model,
                       @RequestParam(value = "page",defaultValue = "0") int page,
                       @RequestParam(value = "kw",defaultValue = "") String kw) {

        Page<Post> paging = this.postService.getList(page,kw);//키워드 추가
        model.addAttribute("paging", paging); // paging 추가
        model.addAttribute("kw", kw); // 검색기능 추가

        // 유저 정보를 가져와서 닉네임 추가를 해주기 위함
        addUserInfoNiname(model,principal); //닉네임 추가
        return "post/list";
    }

    @GetMapping("/post/detail/{id}")
    public String listjoin(Model model,Principal principal, @PathVariable("id") Long id) {
        addUserInfoNiname(model,principal);
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        return "post/list_d";
    }

    @PreAuthorize("isAuthenticated()") // 로그인이 아닐경우 로그인으로 리다이렉션
    @GetMapping ("/post_form")
    public String postCreate(PostForm postForm,Model model,Principal principal){
        model.addAttribute("postForm",postForm);;
        addUserInfoNiname(model,principal);
        return "post/post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping ("/post_form")
    public String postCreate(@Valid PostForm postForm,
                                 BindingResult bindingResult,Principal principal){

        if(bindingResult.hasErrors()){
            return "post/post_form";
        }
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        this.postService.create(postForm.getSubject(), postForm.getContent(),userInfo);
        return "redirect:/list";
    }


    //수정버튼 post/modify/${post.id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/modify/{id}")
    public String postModify(PostForm postForm, @PathVariable("id") long id
            , Principal principal){
        Post post = this.postService.getPost(id);

        if (checkAdmin(principal)) { //어드민 수정
            postForm.setSubject(post.getSubject());
            postForm.setContent(post.getContent());
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        } else {// 유저닉네임 및 권한체크
            checkPermission(post, principal.getName());
        }
         postForm.setSubject(post.getSubject());
         postForm.setContent(post.getContent());
        return "post/post_form";
    }

    @PreAuthorize("isAuthenticated()")  // /post/modify
    @PostMapping("/post/modify/{id}")
    public String postModify(@Valid PostForm postForm,
                               BindingResult bindingResult, Principal principal,
                                 @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            return "post/post_form";
        }
        Post post = this.postService.getPost(id);

        if (checkAdmin(principal)){
            this.postService.modify(post, postForm.getSubject(),
                    postForm.getContent());
            return String.format("redirect:/post/detail/%s", id);
        }

        checkPermission(post, principal.getName()); // 유저 권한체크
        this.postService.modify(post, postForm.getSubject(),
                postForm.getContent());
        return String.format("redirect:/post/detail/%s", id);
    }
         //삭제버튼작동
        @PreAuthorize("isAuthenticated()")
        @GetMapping("/post/delete/{id}")
        public String postDelete(Principal principal,@PathVariable("id") long id){
            Post post = this.postService.getPost(id);

        if (checkAdmin(principal)) {
            System.out.println("어드민 권한이 발생함");
            this.postService.delete(post);
            return "redirect:/";
        }
        checkPermission(post, principal.getName());
        this.postService.delete(post);
        return "redirect:/";
         }

         //추천버튼
         @GetMapping("post/detail/post/vote/{id}")
         public String qestionVote(Principal principal, @PathVariable("id") long id){
             Post post = this.postService.getPost(id);
             UserInfo userInfo = this.userInfoService.getUser(principal.getName());
            this.postService.vote(post,userInfo);
            return  String.format("redirect:/post/detail/%s",id);
        }
    /**
     *
     * @param model
     * @param principal
     * @return 회원가입창으로 리턴
     * 모델과 유저정보를 받고 유저가 없을 경우 회원가입창으로 보냄
     * 혹은 유저닉네임을 표시하기 위해 필요한 메서드
     *
     */
    private String addUserInfoNiname(Model model, Principal principal) {
        if (principal != null) {
            try {
                UserInfo userInfo = userInfoService.getUser(principal.getName());
                model.addAttribute("userInfo", userInfo);
            } catch (DataNotFoundException e) {
                System.out.println("처음 실행 시 유저가 없어서 오류 발생");

                return "redirect:/user/signup";
            }
        }
        return "redirect:/user/signup";
    }

    /**
     *
     * @param post : Post post = this.postService.getPost(id) 를 담고 있어야함
     * @param username  : principal.getName  를 담고 있어야함
     *<br>
     * 유저의 아이디와 작성 된 유저아이디를 체크
     */
    private void checkPermission(Post post, String username) {
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");
            //이후 에러메시지 말고 다른 방식으로 구현할것 생각하기.
        }
    }

    /**
     * 관리자 권한체크
     * @return 반환값 어드민인경우 트루
     */
    private boolean checkAdmin(Principal principal){
        return  SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

}




  /*
         if (!post.getAuthor().getUsername().equals(principal.getName())) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
                     }

                     if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            postForm.setSubject(post.getSubject());
            postForm.setContent(post.getContent());
            System.out.println("어드민 권한이 발생함");
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        }



        */

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

