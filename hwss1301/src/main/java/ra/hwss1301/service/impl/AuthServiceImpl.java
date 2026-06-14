package ra.hwss1301.service.impl;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ra.hwss1301.model.dto.request.ActiveUserDTO;
import ra.hwss1301.model.dto.request.LoginRequest;
import ra.hwss1301.model.dto.request.RegisterDTO;
import ra.hwss1301.model.entity.User;
import ra.hwss1301.repository.UserRepository;
import ra.hwss1301.security.jwt.JwtProvider;
import ra.hwss1301.security.principal.UserPrincipal;
import ra.hwss1301.service.AuthService;
import ra.hwss1301.service.EmailService;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public String register(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trên hệ thống!");
        }
        String otpCode = String.format("%06d", new Random().nextInt(999999));
        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(BCrypt.hashpw(registerDTO.getPassword(), BCrypt.gensalt()))
                .fullName(registerDTO.getFullName())
                .role("USER")
                .enabled(false)
                .otpCode(otpCode)
                .otpExpirationDate(LocalDateTime.now().plusMinutes(5))
                .build();
        userRepository.save(user);
        emailService.sendOtpEmail(user.getEmail(), otpCode);
        return "Đăng ký thành công, kiểm tra email để nhận mã OTP!";
    }

    @Override
    public String activeUser(ActiveUserDTO activeUserDTO) {
        User user = userRepository.findByEmail(activeUserDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản có email trên!"));
        if (user.getEnabled()) {
            return "Tài khoản đã được kích hoạt";
        }
        if (user.getOtpCode() == null || !user.getOtpCode().equals(activeUserDTO.getOtp())) {
            throw new RuntimeException("Mã không đúng!");
        }
        if (user.getOtpExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã đã hết hạn!");
        }
        user.setEnabled(true);
        user.setOtpCode(null);
        user.setOtpExpirationDate(null);
        userRepository.save(user);
        return "Kích hoạt tài khoản thành công!";
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String username = userPrincipal.getUsername();
        String role = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return jwtProvider.generateToken(username, role);
    }
}
