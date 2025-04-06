package pos.userservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pos.userservice.DTO.UserRequest;
import pos.userservice.DTO.UserResponse;

@RestController
@RequestMapping("superadmin")
public class SuperAdmin {

    @PostMapping
    public ResponseEntity<UserResponse> userResponseResponseEntity(UserRequest userRequest) {
        return null;
    }
}
