package tk.atna.githubers.model;

/**
 * Internal representation of a single user
 */
public class User {

    private String id;
    private String login;
    private String pictureUrl;
    private String htmlUrl;


    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public User setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public User setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
        return this;
    }
}
