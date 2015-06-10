package tk.atna.githubers.provider;

import android.net.Uri;

public final class GithubersContract {

    public static final String AUTHORITY = "tk.atna.githubers.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String TYPE_PREFIX = "vnd.android.cursor.dir/vnd.githubers.";
    public static final String ITEM_TYPE_PREFIX = "vnd.android.cursor.item/vnd.githubers.";


    private GithubersContract() {
        // nothing here
    }


    interface BaseColumn {

        String _ID = "_id";
    }


    interface UsersColumns {

        String USER_ID = "user_id";
        String USER_LOGIN = "user_login";
        String USER_PICTURE = "user_picture";
        String USER_URL = "user_url";
    }


    public static class Users implements BaseColumn, UsersColumns {

        public static final String TABLE = "users";

        public static final String CONTENT_TYPE = getContentType(TABLE);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(TABLE);

        public static final Uri CONTENT_URI = getContentUri(TABLE);
    }

    private static String getContentType(String table) {
        return TYPE_PREFIX + table;
    }

    private static String getContentItemType(String table) {
        return ITEM_TYPE_PREFIX + table;
    }

    private static Uri getContentUri(String table) {
        return BASE_CONTENT_URI.buildUpon().appendPath(table).build();
    }
}
