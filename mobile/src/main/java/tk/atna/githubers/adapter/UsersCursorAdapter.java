package tk.atna.githubers.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.atna.githubers.R;
import tk.atna.githubers.provider.GithubersContract;
import tk.atna.githubers.stuff.ContentManager;


public class UsersCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    private int idIndex;
    private int pictureIndex;
    private int loginIndex;
    private int urlIndex;

    private ContentManager contentManager;


    public UsersCursorAdapter(Context context, Cursor cursor, ContentManager contentManager) {
        super(context, cursor, 0);

        this.inflater = LayoutInflater.from(context);
        this.contentManager = contentManager;

        rememberColumns(cursor);
    }

    @Override
    public View newView(Context context, final Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.fragment_users_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ItemViewHolder holder = (ItemViewHolder) view.getTag();

        if(cursor != null && !cursor.isClosed()) {
            contentManager.getImage(cursor.getString(pictureIndex), holder.ivPicture);

            holder.tvLogin.setText(cursor.getString(loginIndex));
            holder.tvUrl.setText(cursor.getString(urlIndex));
        }
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);

        rememberColumns(cursor);
    }

    /**
     * Gets id of the last user id in list
     *
     * @return last user id
     */
    public String getLastUserId() {
        if(getCursor() != null)
            if(getCursor().moveToLast())
                return getCursor().getString(idIndex);

        return null;
    }

    /**
     * Remembers cursor column indexes for future reuse
     *
     * @param cursor cursor whose columns
     */
    private void rememberColumns(Cursor cursor) {
        if(cursor == null)
            return;

        this.pictureIndex = cursor.getColumnIndex(GithubersContract.Users.USER_PICTURE);
        this.idIndex = cursor.getColumnIndex(GithubersContract.Users.USER_ID);
        this.loginIndex = cursor.getColumnIndex(GithubersContract.Users.USER_LOGIN);
        this.urlIndex = cursor.getColumnIndex(GithubersContract.Users.USER_URL);
    }

    /**
     * View holder class for list view items
     */
    class ItemViewHolder {

        @InjectView(R.id.iv_picture)
        ImageView ivPicture;

        @InjectView(R.id.tv_login)
        TextView tvLogin;

        @InjectView(R.id.tv_url)
        TextView tvUrl;


        ItemViewHolder(View v) {
            ButterKnife.inject(this, v);
        }

    }
}
