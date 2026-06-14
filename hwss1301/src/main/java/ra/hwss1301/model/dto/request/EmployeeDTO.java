package ra.hwss1301.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO {
    @NotBlank(message = "Không được để trống tên nhân viên!")
    private String fullName;
    private Double salary;
    @NotBlank(message = "Không được để trống email!")
    private String email;
    @NotBlank(message = "Không được để trống department!")
    private String department;
    private MultipartFile avatarFile;
}
