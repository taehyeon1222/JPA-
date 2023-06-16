package nth.configs;

import lombok.RequiredArgsConstructor;
import nth.post.Category;
import nth.post.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    // 데이터 초기설정 엔티티 및 웹이 구동될때마다 자동실행됨


    private final CategoryRepository categoryRepository;
    @Override
    public void run(String... args) throws Exception {

        /*
        if(categoryRepository.findByName("공지사항").isEmpty()){
            System.out.println("*********자동실행됨*******");
            System.out.println("*********엔티티 생성후*******");
            System.out.println("*********생성됨*******");
            Category notice = new Category();
            notice.setName("공지사항");
            categoryRepository.save(notice);
            Category freeBoard = new Category();
            freeBoard.setName("자유게시판");
            categoryRepository.save(freeBoard);
            System.out.println("*********공지사항 카테고리 생성*******");
            System.out.println("*********자유게시판 카테고리 생성함*******");
            // 공지사항이 없으면 자유게시판도 없음

         */
        }



//        System.out.println("*********자동실행됨*******");
//        System.out.println("*********엔티티 생성후*******");
//        System.out.println("*********생성됨*******");
//
//        Category notice = new Category();
//        notice.setName("공지사항");
//        categoryRepository.save(notice);
//
//        Category freeBoard = new Category();
//        freeBoard.setName("자유게시판");
//        categoryRepository.save(freeBoard);
//
//        System.out.println("*********공지사항 카테고리 생성*******");
//        System.out.println("*********자유게시판 카테고리 생성함*******");
    }



