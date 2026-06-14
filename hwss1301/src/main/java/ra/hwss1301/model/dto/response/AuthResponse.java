package ra.hwss1301.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthResponse<T> {
    private String username;
    private String type = "Bearer";
    private T data;
    private String accessToken;
}
