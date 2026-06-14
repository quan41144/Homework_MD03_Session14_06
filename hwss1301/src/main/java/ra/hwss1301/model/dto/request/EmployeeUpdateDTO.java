package ra.hwss1301.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeUpdateDTO {
    @NotBlank(message = "Không được để trống tên!")
    @Size(min = 5, message = "Tối thiểu 5 ký tự!")
    private String fullName;
    @NotBlank(message = "Không được để trống email!")
    @Email(message = "Không đúng định dạng email!")
    private String email;
    private Optional<MultipartFile> avatarFile;
}
