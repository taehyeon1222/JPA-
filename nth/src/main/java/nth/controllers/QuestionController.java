package nth.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Random;
import java.util.zip.DataFormatException;

@Controller
@RequiredArgsConstructor
//@RequestMapping("view/question")
@EnableMethodSecurity(prePostEnabled = true)
public class QuestionController {


    private final QuestionService questionService;
    private final UserInfoService userInfoService;

    @GetMapping("/list")
    public String list(HttpServletRequest request,Principal principal,Model model, @RequestParam(value = "page",defaultValue = "0")
            int page,@RequestParam(value = "kw",defaultValue = "") String kw)
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
        Page<Question> paging = this.questionService.getList(page,kw);//키워드 추가
        model.addAttribute("paging", paging); // paging 추가
        model.addAttribute("kw", kw); // 검색기능 추가

        // 유저 정보를 가져와서 닉네임 추가를 해주기 위함
        if(principal != null) {
            UserInfo userInfo = this.userInfoService.getUser(principal.getName());
            model.addAttribute("UserInfo", userInfo);
        }
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping ("/question_form")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult,Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(),questionForm.getContent(),userInfo);
        return "redirect:/list";
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



    //수정버튼 question/modify/${question.id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/modify/{id}")
    public String questionModify(QuestionForm questionForm,@PathVariable("id") long id
            ,Principal principal){
        Question question = this.questionService.getQuestion(id);

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("어드민 권한이 발생함");
            questionForm.setSubject(question.getSubject());
            questionForm.setContent(question.getContent());
            return "question_form";
           // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        }
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            //비정상적인 경로로 들어올 경우 유저 이름검사
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한 이없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")  // /question/modify
    @PostMapping("/question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm,
                               BindingResult bindingResult,
    Principal principal, @PathVariable("id") long id) {
         if (bindingResult.hasErrors()) {
             return "question_form";
             }
        Question question = this.questionService.getQuestion(id);
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("어드민 권한이 발생함");
            this.questionService.modify(question, questionForm.getSubject(),
                    questionForm.getContent());
            return String.format("redirect:/question/detail/%s",id);

        }

         if (!question.getAuthor().getUsername().equals(principal.getName())) {
             System.out.println("수정 권한이 발생함");
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
                     }

         this.questionService.modify(question, questionForm.getSubject(),
                questionForm.getContent());
        return String.format("redirect:/question/detail/%s",id);
         }


         //삭제버튼작동 /|question/delete/{id}   /question/delete/${question.id}|}"
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/delete/{id}")
    public String questionDelete(Principal principal,@PathVariable("id") long id){
            Question question = this.questionService.getQuestion(id);
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("어드민 권한이 발생함");
            this.questionService.delete(question);
            return "redirect:/";
        }
            if(!question.getAuthor().getUsername().equals(principal.getName())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
            }
            this.questionService.delete(question);
        return "redirect:/";
         }

         //추천버튼
         @GetMapping("question/detail/question/vote/{id}")
         public String qestionVote(Principal principal, @PathVariable("id") long id){
        Question question = this.questionService.getQuestion(id);
        UserInfo userInfo = this.userInfoService.getUser(principal.getName());
        this.questionService.vote(question,userInfo);
        return  String.format("redirect:/question/detail/%s",id);
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


