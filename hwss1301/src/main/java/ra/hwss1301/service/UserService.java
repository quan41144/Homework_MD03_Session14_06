package ra.hwss1301.service;

import ra.hwss1301.model.dto.request.LoginRequest;
import ra.hwss1301.model.dto.request.RegisterDTO;
import ra.hwss1301.model.entity.User;

public interface UserService {
    User registerUser(RegisterDTO registerDTO);
    User login(LoginRequest loginRequest);
}
