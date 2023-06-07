package nth.controllers;


import lombok.RequiredArgsConstructor;
import nth.books.Book;
import nth.books.BookPrice;
import nth.books.BookPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {


    private final BookPriceService bookService;

    @GetMapping  ("/admin")
    public String bookConfigCreate(Model model){

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

    @PostMapping ("/admin")
    public String bookConfigCreate(@RequestParam("bookPrice") String bookPrice,
                                   @RequestParam("bookName") String bookName ){
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            bookService.create(bookName, bookPrice);
            System.out.println("어드민 권한이 발생함");
            return "redirect:/admin";
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"관리자권한입니다.");
        }
        return "list";
    }



}
