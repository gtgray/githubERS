package tk.atna.githubers.model;

import com.google.gson.annotations.SerializedName;

public class Inbound {


    public static class User {

        String id;
        String login;

        @SerializedName("avatar_url")
        String avatarUrl;

        @SerializedName("html_url")
        String htmlUrl;

    }

}
