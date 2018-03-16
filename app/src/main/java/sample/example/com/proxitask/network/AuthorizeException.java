package sample.example.com.proxitask.network;

import java.io.IOException;

class AuthorizedException extends IOException {
    @Override
    public String getMessage() {
        return "Request Unauthorized";
    }
}
