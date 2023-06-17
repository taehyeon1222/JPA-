package nth.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nth.books.Book;
import nth.books.BookDTO;
import nth.books.BookService;
import nth.books.CertificationInfoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    //개발 일시중단
    private final BookService bookService;
    private final CertificationInfoService certificationInfoService;

    @GetMapping  ("/admin")
    @PreAuthorize("isAuthenticated()")
    public String bookConfigCreate(BookDTO bookDTO,Model model){
        List<Book> book = bookService.findAll();
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("어드민 권한이 발생함");
            model.addAttribute("book", book); //가격
            return "admin/admin_book_config";
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        //return "admin/admin_book_config";
    }


// 수정
@PostMapping ("/admin")
@PreAuthorize("isAuthenticated()")
public String bookConfigCreate(@ModelAttribute("bookDTO") @Valid BookDTO bookDTO,
                               BindingResult bindingResult,Model model){
    List<Book> books = bookService.findAll();
    if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
            SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                    (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        if(bindingResult.hasErrors()){
            model.addAttribute("books", books);
            return "admin/admin_book_config";
        }
        try{
            Book book = bookService.create(bookDTO);
            certificationInfoService.create(bookDTO,book);

        }catch (DataIntegrityViolationException e){
            System.out.println(e.getMessage());// Or log the message
            bindingResult.reject("signupFailed","중복된값입니다.");
            model.addAttribute("books", books);
            e.printStackTrace();
            return "admin/admin_book_config";
        }
        System.out.printf("값: s , %s",bookDTO.getBookName(),bookDTO.getPrice());
        System.out.println("어드민 권한이 발생함");
        return "redirect:/admin";
    }
    return "list";
}

}
