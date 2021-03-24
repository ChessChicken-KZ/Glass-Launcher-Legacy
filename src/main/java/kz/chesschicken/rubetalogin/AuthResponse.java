package kz.chesschicken.rubetalogin;

public class AuthResponse {
    private String username;
    private String token;
    private AuthStatus status;
    public AuthResponse(String u)
    {
        username = u;
    }

    public String getUsername() {
        return username;
    }

    public void setStatus(AuthStatus status) {
        this.status = status;
        if(this.status != AuthStatus.SUCCESS)
            token = "";
    }

    public AuthStatus getStatus() {
        return status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
