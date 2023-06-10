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
                       @RequestParam(value = "kw",defaultValue = "") String kw)
    {
        /*
        String userId;
        if (principal != null) {
            userId = principal.getName();
        } else {
            HttpSession session = request.getSession();
            userId = (String) session.getAttribute("userId");
            if (userId == null) {
                // 세션에 익명 사용자 아이디 생성 및 저장
                userId = randomUserIdCreate();
                session.setAttribute("userId", userId);
            }
        }
          model.addAttribute("UserInfo", userId);
        model.addAttribute("userId", userId); // userId를 먼저 추가
        익명아이디 아직 미구현
        */
        Page<Post> paging = this.postService.getList(page,kw);//키워드 추가
        model.addAttribute("paging", paging); // paging 추가
        model.addAttribute("kw", kw); // 검색기능 추가

        // 유저 정보를 가져와서 닉네임 추가를 해주기 위함
        if(principal != null) {
            try {
                UserInfo userInfo = this.userInfoService.getUser(principal.getName());
                // 처음실행시 유저 가 없어서 오류 발생시킴
                model.addAttribute("UserInfo", userInfo);
            }catch (DataNotFoundException e){
                return "redirect:/user/signup";
            }
        }
        return "post/list";
    }

    @GetMapping("/post/detail/{id}")
    public String listjoin(Model model, @PathVariable("id") Long id) {
        Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        return "post/list_d";
    }

    @PreAuthorize("isAuthenticated()") // 로그인이 아닐경우 로그인으로 리다이렉션
    @GetMapping ("/post_form")
    public String postCreate(PostForm postForm){
        // this.postService.create(subject,content);
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

    //질문저장
    /*
    @PostMapping ("/post/create")
    public String postCreate(@RequestParam String subject, @RequestParam String content){
        this.postService.create(subject,content);
        return "redirect:/list";
    }
    */

    /*
    @PostMapping ("/post/create")
    public String postCreate1(@Valid PostForm postForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "post_form";
        }
        this.postService.create(postForm.getContent(),postForm.getSubject());
        return "redirect:/list";
    }
*/



    //수정버튼 post/modify/${post.id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/modify/{id}")
    public String postModify(PostForm postForm, @PathVariable("id") long id
            , Principal principal){
        Post post = this.postService.getPost(id);

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("어드민 권한이 발생함");
            postForm.setSubject(post.getSubject());
            postForm.setContent(post.getContent());
            return "post/post_form";
           // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        }
        if(!post.getAuthor().getUsername().equals(principal.getName())){
            //비정상적인 경로로 들어올 경우 유저 이름검사
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한 이없습니다.");
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
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("어드민 권한이 발생함");
            this.postService.modify(post, postForm.getSubject(),
                    postForm.getContent());
            return String.format("redirect:/post/detail/%s",id);

        }

         if (!post.getAuthor().getUsername().equals(principal.getName())) {
             System.out.println("수정 권한이 발생함");
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
                     }

         this.postService.modify(post, postForm.getSubject(),
                postForm.getContent());
        return String.format("redirect:/post/detail/%s",id);
         }

         //삭제버튼작동
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/delete/{id}")
    public String postDelete(Principal principal,@PathVariable("id") long id){
            Post post = this.postService.getPost(id);
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("어드민 권한이 발생함");
            this.postService.delete(post);
            return "redirect:/";
        }
            if(!post.getAuthor().getUsername().equals(principal.getName())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
            }
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


         //랜덤 userID 문자열 반환
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


 }


