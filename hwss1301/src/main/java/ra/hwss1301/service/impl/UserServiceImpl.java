package ra.hwss1301.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.hwss1301.exception.UserExistedException;
import ra.hwss1301.model.dto.request.LoginRequest;
import ra.hwss1301.model.dto.request.RegisterDTO;
import ra.hwss1301.model.entity.User;
import ra.hwss1301.repository.UserRepository;
import ra.hwss1301.security.principal.UserPrincipal;
import ra.hwss1301.service.UserService;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public User registerUser(RegisterDTO registerDTO) {
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new UserExistedException("Username " + registerDTO.getFullName() + " already exists");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role("USER")
                .fullName(registerDTO.getFullName())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("username or password incorrect"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("username or password incorrect");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        }
        catch (AuthenticationException e) {
            throw new RuntimeException("username or password incorrect");
        }
    }
}
