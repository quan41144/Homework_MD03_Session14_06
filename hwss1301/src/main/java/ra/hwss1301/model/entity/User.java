package ra.hwss1301.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username", length = 100, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "full_name", length = 100)
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "role", length = 100, nullable = false)
    private String role;
    @Builder.Default
    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default false")
    private Boolean enabled = false;
    @Column(name = "otp_code", length = 6)
    private String otpCode;
    @Column(name = "otp_expiration_date")
    private LocalDateTime otpExpirationDate;
}
