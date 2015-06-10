package tk.atna.githubers.stuff;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import java.util.List;

import tk.atna.githubers.http.HttpHelper;
import tk.atna.githubers.http.ServerException;
import tk.atna.githubers.model.ContentMapper;
import tk.atna.githubers.model.Inbound;


public class ContentManager {

    private static ContentManager INSTANCE;

    private Context context;

    private HttpHelper httpHelper;

    private ContentResolver contentResolver;


    private ContentManager(Context context) {
        this.context = context;
        this.httpHelper = new HttpHelper(context);
        this.contentResolver = context.getContentResolver();
    }

    /**
     * Initializes content manager.
     * It is better to give it an application context
     *
     * @param context application context
     */
    public static synchronized void init(Context context) {
        if(context == null)
            throw new NullPointerException("Can't create instance with null context");
        if(INSTANCE != null)
            throw new IllegalStateException("Can't initialize ContentManager twice");

        INSTANCE = new ContentManager(context);
    }

    /**
     * Gets only instance of content manager.
     * Can't be called from non UI thread
     *
     * @return content manager instance
     */
    public static ContentManager get() {
        if(Looper.myLooper() != Looper.getMainLooper())
            throw new IllegalStateException("Must be called from UI thread");

        if(INSTANCE == null)
            throw new IllegalStateException("ContentManager is null. It must have been"
                                          + " created at application init");

        return INSTANCE;
    }

    /**
     * Refreshes users list
     */
    public void getUsers() {
        loadUsersAsync(null);
     }

    /**
     * Retrieves users with ids more than this offset
     *
     * @param offset pagination indicator
     */
    public void getUsersAfter(String offset) {
        loadUsersAsync(offset);
     }

    /**
     * Drops image with url into view
     *
     * @param url image url to get from
     * @param view image view to drop into
     */
    public void getImage(String url, ImageView view) {
        loadImageAsync(url, view);
    }

    /**
     * Makes async GitHub server api call to get users list
     * and pushes it into sqlite
     *
     * @param offset pagination indicator
     */
    private void loadUsersAsync(final String offset) {

        (new Worker.SimpleTask() {
            @Override
            public void run() {
                try {
                    List<Inbound.User> list = httpHelper.getUsers(offset);
                    ContentMapper.pushUsersToProvider(contentResolver, list);

                } catch (ServerException | IllegalArgumentException ex) {
                    ex.printStackTrace();
                    this.exception = ex;
                }
            }
        }).execute(new Worker.SimpleTask.Callback() {
            @Override
            public void onComplete(Exception ex) {
                notifyChanges(ex == null ? Actions.GET_USERS
                                         : Actions.GET_USERS_EXCEPTION);
            }
        });
    }

    /**
     * Loads image with url from cache/sd/server and shows it in view
     *
     * @param url image url to load from
     * @param view image view to load into
     */
    private void loadImageAsync(String url, ImageView view) {
        httpHelper.loadImage(url, view);
    }

    /**
     * Sends local broadcast notification
     *
     * @param action action to process
     */
    private void notifyChanges(int action) {
        LocalBroadcaster.sendLocalBroadcast(action, null, context);
    }


    /**
     * Possible content manager actions
     */
    public interface Actions {

        int GET_USERS = 0x0000ca10;
        int GET_USERS_EXCEPTION = 0x0000ca11;
    }


    /**
     * Content manager callback to return data after async extraction
     *
     * @param <T> Object to receive as a result
     */
    public interface ContentCallback<T> {
        /**
         * Fires when async data extraction is completed and data/exception
         * is ready to be returned
         *
         * @param result received result
         * @param exception possible exception
         */
        void onResult(T result, Exception exception);
    }

}
