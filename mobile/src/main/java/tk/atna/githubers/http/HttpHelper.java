package tk.atna.githubers.http;


import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tk.atna.githubers.R;
import tk.atna.githubers.model.Inbound;

public class HttpHelper {

    private final static int DEFAULT_PICTURE = R.drawable.ic_picture_default;

    private ServerApi api;

    private final Context context;


    public HttpHelper(Context context) {
        this.context = context;
        // setup retrofit
        RestAdapter ra = new RestAdapter.Builder()
                .setEndpoint(ServerApi.SERVER_URL)
                .setErrorHandler(new ServerErrorHandler())
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        // retrofit server api inflater
        api = ra.create(ServerApi.class);
    }

    /**
     * Makes GitHub server api call to get users list
     *
     * @param since pagination indicator
     * @return users list model object (parsed from json)
     */
    public List<Inbound.User> getUsers(String since) throws ServerException {
        return api.getUsers(since);
    }

    /**
     * Loads image with url from cache/sd/server and shows it in view
     *
     * @param url image url to load from
     * @param view image view to load into
     */
    public void loadImage(String url, ImageView view) {

        Picasso.with(context)
               .load(url)
               .placeholder(DEFAULT_PICTURE)
               .error(DEFAULT_PICTURE)
               .into(view);
    }


    private static class ServerErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            Response response = cause.getResponse();
            if(response != null) {
                switch (response.getStatus()) {
                    case HttpCodes.SERVICE_UNAVAILABLE:
                        break;

                    case HttpCodes.UNAUTHORIZED:
                        break;

                    case HttpCodes.INTERNAL_SERVER_ERROR:
                        break;

                    case HttpCodes.NOT_FOUND:
                        break;

                    case HttpCodes.FORBIDDEN:
                        break;

                    case HttpCodes.BAD_REQUEST:
                        break;
                }
                return new ServerException(cause);
            }
//            return cause;
            return new ServerException(cause);
        }
    }


}

