package tk.atna.githubers.http;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import tk.atna.githubers.model.Inbound;

interface ServerApi {

    String SERVER_URL = "https://api.github.com";
    String USERS_ENDPOINT = "/users";

    String SINCE = "since";


    @GET(USERS_ENDPOINT)
    List<Inbound.User> getUsers(
            @Query(SINCE) String since
    ) throws ServerException;

}
