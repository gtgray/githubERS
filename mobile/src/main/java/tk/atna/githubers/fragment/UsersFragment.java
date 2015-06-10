package tk.atna.githubers.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.atna.githubers.R;
import tk.atna.githubers.adapter.UsersCursorAdapter;
import tk.atna.githubers.provider.GithubersContract;
import tk.atna.githubers.stuff.ContentManager;

public class UsersFragment extends BaseFragment
                           implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = UsersFragment.class.getSimpleName();

    public static final int TITLE = R.string.users_title;

    public static final int LOADER_ID = 0x00000cc1;

    private ContentManager contentManager = ContentManager.get();

    private UsersCursorAdapter adapter;

    @InjectView(R.id.users_list)
    ListView usersList;

    // current list position
    private int currItem;

    // flag to prevent multiple loading simultaneously
    private boolean isLoading = false;


    /**
     * Initializes UsersFragment
     *
     * @return instance of retained UsersFragment class
     */
    public static UsersFragment newInstance() {
        UsersFragment fragment = new UsersFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.inject(this, view);

        if(adapter == null)
            adapter = new UsersCursorAdapter(inflater.getContext(), null, contentManager) {
                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    super.bindView(view, context, cursor);

                    // load next page
                    int position = cursor.getPosition();
                    if(position > 0 && position > cursor.getCount() - 10 && !isLoading) {
                        contentManager.getUsersAfter(getLastUserId());
                        isLoading = true;
                    }
                }
            };

        usersList.setAdapter(adapter);
        usersList.setSelection(currItem);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // initializes loader manager for cursor
        if(getActivity() != null)
            getActivity().getSupportLoaderManager()
                         .initLoader(LOADER_ID, null, this);

        // refresh list at start
        if(savedInstanceState == null)
            contentManager.getUsers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // remember list position
        currItem = usersList.getFirstVisiblePosition();
    }

    /**
     * LocalBroadcaster callback to catch and process commands (with data)
     *
     * @param action action to process
     * @param data received data
     */
    @Override
    public void onReceive(int action, Bundle data) {
        switch (action) {
            // users loaded
            case ContentManager.Actions.GET_USERS:
                isLoading = false;
                break;
            // not loaded
            case ContentManager.Actions.GET_USERS_EXCEPTION:
                isLoading = false;
                makeFragmentAction(ACTION_LOADING_EXCEPTION, null);
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                                GithubersContract.Users.CONTENT_URI,
                                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ID) {
            adapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.changeCursor(null);
    }

}
