package ra.hwss1301.service;

public interface EmailService {
    void sendOtpEmail(String email, String otpCode);
}
