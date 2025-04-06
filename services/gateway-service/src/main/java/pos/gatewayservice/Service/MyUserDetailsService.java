package pos.gatewayservice.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pos.gatewayservice.Repository.UserRepository;
import reactor.core.publisher.Mono;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import pos.gatewayservice.Model.UserPrincipal;

@Service
public class MyUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::new);
    }

}
