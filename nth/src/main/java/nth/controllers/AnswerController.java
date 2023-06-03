package nth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nth.answer.Answer;
import nth.answer.AnswerForm;
import nth.answer.AnswerService;
import nth.question.Question;
import nth.question.QuestionService;
import nth.user.UserInfo;
import nth.user.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserInfoService userInfoService;

/*
        @PostMapping("/question/detail/create/{id}")
        public String createAnswer(Model model, @PathVariable("id") long id,
                                   @RequestParam String content){
            Question question = this.questionService.getQuestion(id);
            this.answerService.create(question,content);
            return String.format("redirect:/question/detail/%s",id);
        }
*/
/*
    @PostMapping("/question/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") long id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult) {
        Question question = this.questionService.getQuestion(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "list_d";
        }
        this.answerService.create(question, answerForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
/*

 */
    //작성자 저장
    @PostMapping("/question/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") long id,
                               @Valid AnswerForm answerForm, BindingResult
                                           bindingResult, Principal principal,
                               HttpServletRequest request){
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        Question question = this.questionService.getQuestion(id);
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        if(bindingResult.hasErrors()){
            model.addAttribute("question",question);
            return "list_d";
        }
        if(principal == null){
            System.out.println("널");
            return "list";

        }

        //this.answerService.create(question, answerForm.getContent());
        this.answerService.create(question,answerForm.getContent(),userInfo);
            return String.format("redirect:/question/detail/%s",id);
    }


    }


