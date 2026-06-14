package ra.hwss1301.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ra.hwss1301.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailFrom;
    @Override
    public void sendOtpEmail(String email, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject("Mã xác thực tài khoản");
        message.setText(otpCode);
        mailSender.send(message);
    }
}
