package nth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nth.question.Question;
import nth.question.QuestionForm;
import nth.question.QuestionRepository;
import nth.question.QuestionService;
import nth.user.UserInfo;
import nth.user.UserInfoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.zip.DataFormatException;

@Controller
@RequiredArgsConstructor
//@RequestMapping("view/question")
@EnableMethodSecurity(prePostEnabled = true)
public class QuestionController {


    private final QuestionService questionService;
    private final UserInfoService userInfoService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page",defaultValue = "0")
            int page)
    {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging",paging);

        //List<Question> questionList = this.questionService.getList();
        //model.addAttribute("questionList", questionList);
        return "list";
    }

    @GetMapping("/question/detail/{id}")
    public String listjoin(Model model, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "list_d";
    }

    @PreAuthorize("isAuthenticated()") // 로그인이 아닐경우 로그인으로 리다이렉션
    @GetMapping ("/question_form")
    public String questionCreate(QuestionForm questionForm){
        // this.questionService.create(subject,content);
        return "question_form";
    }

    //질문저장
    /*
    @PostMapping ("/question/create")
    public String questionCreate(@RequestParam String subject, @RequestParam String content){
        this.questionService.create(subject,content);
        return "redirect:/list";
    }
    */

    /*
    @PostMapping ("/question/create")
    public String questionCreate1(@Valid QuestionForm questionForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        this.questionService.create(questionForm.getContent(),questionForm.getSubject());
        return "redirect:/list";
    }
*/
    @PreAuthorize("isAuthenticated()")
    @PostMapping ("/question/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        this.questionService.create(questionForm.getContent(),questionForm.getSubject(),userInfo);
        return "redirect:/list";
    }


    //수정버튼 /question/detail/question/modify/1552
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/detail/question/modify/{id}")
    public String questionModify(QuestionForm questionForm,@PathVariable("id") long id
            ,Principal principal){
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한 이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/detail/question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm,
                               BindingResult bindingResult,
    Principal principal, @PathVariable("id") long id) {
         if (bindingResult.hasErrors()) {
             return "question_form";
             }
        Question question = this.questionService.getQuestion(id);
         if (!question.getAuthor().getUsername().equals(principal.getName())) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
                     }
         this.questionService.modify(question, questionForm.getSubject(),
                questionForm.getContent());
        return String.format("redirect:/question/detail/%s",id);
         }

         //삭제버튼작동
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/|question/delete/{id}")
    public String questionDelete(Principal principal,@PathVariable("id") long id){
            Question question = this.questionService.getQuestion(id);
            if(!question.getAuthor().getUsername().equals(principal.getName())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
            }
            this.questionService.delete(question);
        return "redirect:/";
         }


 }


