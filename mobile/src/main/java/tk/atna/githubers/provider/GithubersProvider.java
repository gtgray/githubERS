package tk.atna.githubers.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class GithubersProvider extends ContentProvider {

    private static final String AUTHORITY = GithubersContract.AUTHORITY;

    private static final int MATCH_USERS = 0x00000011;
    private static final int MATCH_USERS_ITEM = 0x00000012;

    private GithubersDB db;

    private static final UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, GithubersContract.Users.TABLE, MATCH_USERS);
        uriMatcher.addURI(AUTHORITY, GithubersContract.Users.TABLE + "/*", MATCH_USERS_ITEM);
    }


    @Override
    public boolean onCreate() {
        db = new GithubersDB(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {

            case MATCH_USERS:
                return GithubersContract.Users.CONTENT_TYPE;

            case MATCH_USERS_ITEM:
                return GithubersContract.Users.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        String table = GithubersContract.Users.TABLE;
        String where = null;

        switch (uriMatcher.match(uri)) {

            case MATCH_USERS:
                sortOrder = GithubersContract.Users._ID + " ASC";
                break;

            case MATCH_USERS_ITEM:
                where = GithubersContract.Users.USER_ID + " = '" + uri.getLastPathSegment() + "'";
                break;

            default:
                return null;
        }

        Cursor cursor = db.getWritableDatabase()
                          .query(table, null, where, null, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (uriMatcher.match(uri)) {
            case MATCH_USERS:
            default:
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);

            case MATCH_USERS_ITEM:
                break;
        }

        String table = GithubersContract.Users.TABLE;
        // insert row
        long row = db.getWritableDatabase().insert(table, null, values);

        return ContentUris.withAppendedId(uri, row);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase dBase = db.getWritableDatabase();
        String table;
        String where;

        switch (uriMatcher.match(uri)) {
            case MATCH_USERS:
                db.dropTableUsers(dBase);
                db.createTableUsers(dBase);
                return 0;

            case MATCH_USERS_ITEM:
                table = GithubersContract.Users.TABLE;
                where = GithubersContract.Users.USER_ID + " = '" + uri.getLastPathSegment() + "'";
                break;

            default:
                throw new UnsupportedOperationException("Unknown delete uri: " + uri);
        }

        selection = (selection == null || selection.length() == 0)
                ? where : selection + " AND " + where;

        return dBase.delete(table, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case MATCH_USERS:
            default:
                throw new UnsupportedOperationException("Unknown update uri: " + uri);

            case MATCH_USERS_ITEM:
                break;
        }
            int rows = delete(uri, selection, selectionArgs);
            insert(uri, values);
            notifyChange(uri);
            return rows;
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

}
