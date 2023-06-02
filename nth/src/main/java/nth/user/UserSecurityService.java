package nth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserInfoRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
         Optional<UserInfo> siteUser = this.userRepository.findByUsername(
                username);
         if (siteUser.isEmpty()) {
             throw new UsernameNotFoundException("사 용 자 를 찾 을 수 없 습 니 다.");
             }
         UserInfo User = siteUser.get();
         List<GrantedAuthority> authorities = new ArrayList<>();

         if ("admin".equals(username)) {
             authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue
                    ()));
             } else {
             authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()
            ));
             }
         return new User(User.getUsername(), User.getPassword(),
                authorities);
         }
 }

