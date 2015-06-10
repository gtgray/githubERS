package tk.atna.githubers.http;

import java.io.IOException;

import retrofit.RetrofitError;

public class ServerException extends IOException {

    private RetrofitError cause;


    public ServerException(String message) {
        super(message);
    }

    public ServerException(RetrofitError cause) {
        this.cause = cause;
    }

    public RetrofitError getCause() {
        return cause;
    }

}
