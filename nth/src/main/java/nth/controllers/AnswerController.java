package nth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nth.answer.AnswerForm;
import nth.answer.AnswerService;
import nth.post.Post;
import nth.post.PostService;
import nth.user.UserInfo;
import nth.user.UserInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final PostService postService;
    private final AnswerService answerService;
    private final UserInfoService userInfoService;

    //작성자 저장
    @PostMapping("/post/create/{id}")
    //@PreAuthorize("isAuthenticated()")
    public String createAnswer(Model model, @PathVariable("id") long id,
                               @Valid AnswerForm answerForm, BindingResult
                                           bindingResult, Principal principal){
        if(principal == null){
            model.addAttribute("err","로그인후이용해주세요"); //실행안됨
            return "user/login_form";
        }
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        Post post = this.postService.getPost(id);

        if(bindingResult.hasErrors()){
            model.addAttribute("post", post);
            return "post/post_Details";
        }

        //this.answerService.create(post, answerForm.getContent());
        this.answerService.create(post,answerForm.getContent(),userInfo);
            return String.format("redirect:/post/detail/%s",id);
    }


    }


