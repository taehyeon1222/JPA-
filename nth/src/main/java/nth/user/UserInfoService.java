package nth.user;


import lombok.RequiredArgsConstructor;
import nth.DataNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    private final PasswordEncoder passwordEncoder;

    public UserInfo create(String username,String password,String email,String nickname){
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setEmail(email);
        user.setNickname(nickname);
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
       // user.setCreatDate(LocalDateTime.now()); // 나중에 변경해야함
        this.userInfoRepository.save(user);
        return user;

    }

    public UserInfo getUser(String username){
        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);
        if(userInfo.isPresent()){
            return userInfo.get();
        }
        else throw new DataNotFoundException("유저가 없습니다.");
    }
}
