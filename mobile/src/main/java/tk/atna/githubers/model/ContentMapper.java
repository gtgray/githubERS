package tk.atna.githubers.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import java.util.List;

import tk.atna.githubers.provider.GithubersContract;

public class ContentMapper {

    /**
     * Pushes users list into sqlite
     *
     * @param cr ContentResolver object
     * @param list users list model object
     */
    public static void pushUsersToProvider(ContentResolver cr, List<Inbound.User> list) {
        if(cr == null || list == null)
            throw new IllegalArgumentException("One or more arguments are null");

        for(Inbound.User user : list) {
            pushUserToProvider(cr, user);
        }
    }

    /**
     * Pushes single user into sqlite
     *
     * @param cr ContentResolver object
     * @param user single user model object
     */
     private static void pushUserToProvider(ContentResolver cr, Inbound.User user) {
        if(cr == null || user == null)
            throw new IllegalArgumentException("One or more arguments are null");

        ContentValues cv = new ContentValues();
        cv.put(GithubersContract.Users.USER_ID, user.id);
        cv.put(GithubersContract.Users.USER_LOGIN, user.login);
        cv.put(GithubersContract.Users.USER_PICTURE, user.avatarUrl);
        cv.put(GithubersContract.Users.USER_URL, user.htmlUrl);
        // update values
        cr.update(Uri.withAppendedPath(GithubersContract.Users.CONTENT_URI,
                                       String.valueOf(user.id)),
                  cv, null, null);
    }

}
