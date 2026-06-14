package ra.hwss1301.service;

import ra.hwss1301.model.dto.request.ActiveUserDTO;
import ra.hwss1301.model.dto.request.LoginRequest;
import ra.hwss1301.model.dto.request.RegisterDTO;

public interface AuthService {
    String register(RegisterDTO registerDTO);
    String activeUser(ActiveUserDTO activeUserDTO);
    String login(LoginRequest loginRequest);
}
