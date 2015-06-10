package tk.atna.githubers.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static tk.atna.githubers.provider.GithubersContract.*;

public class GithubersDB extends SQLiteOpenHelper {

    static final String DB_NAME = "githubers.db";
    static final int DB_VERSION = 1;

    // table users
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + Users.TABLE + " ("
                    + BaseColumn._ID + " INTEGER PRIMARY KEY, "
                    + Users.USER_ID + " TEXT NOT NULL, "
                    + Users.USER_LOGIN + " TEXT, "
                    + Users.USER_PICTURE + " TEXT, "
                    + Users.USER_URL + " TEXT);";

    private static final String DROP_TABLE_USERS =
            "DROP TABLE IF EXISTS " + Users.TABLE + ";";


	public GithubersDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        createTableUsers(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// nothing here
    }

    void createTableUsers(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_USERS);
    }

    void dropTableUsers(SQLiteDatabase db) {
		db.execSQL(DROP_TABLE_USERS);
    }

}
