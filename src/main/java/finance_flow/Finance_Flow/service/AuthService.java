package finance_flow.Finance_Flow.service;

import finance_flow.Finance_Flow.dto.request.LoginRequest;
import finance_flow.Finance_Flow.dto.request.RegisterRequest;
import finance_flow.Finance_Flow.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    //void logout();
    //AuthResponse refreshToken(String token);
}
