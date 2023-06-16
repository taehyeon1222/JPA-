package nth;

import lombok.RequiredArgsConstructor;
import nth.post.Post;
import nth.post.PostService;
import nth.user.UserInfo;
import nth.user.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
@RequiredArgsConstructor
@Component
public class MyUtile {

    private final PostService postService;
    private final UserInfoService userInfoService;
    /**
     *
     * @param model
     * @param principal
     * @return 회원가입창으로 리턴
     * 모델과 유저정보를 받고 유저가 없을 경우 회원가입창으로 보냄
     * 혹은 유저닉네임을 표시하기 위해 필요한 메서드
     *
     */
    public String addUserInfoNiname(Model model, Principal principal) {
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
    public void checkPermission(Post post, String username) {
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");
            //이후 에러메시지 말고 다른 방식으로 구현할것 생각하기.
        }
    }

    /**
     * 관리자 권한체크
     * @return 반환값 어드민인경우 트루
     */
    public boolean checkAdmin(Principal principal){
        return  SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains
                        (new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
