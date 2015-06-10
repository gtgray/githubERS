package tk.atna.githubers.stuff;

import android.app.Application;

public class GithubersApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // init content manager
        ContentManager.init(this);
    }

}
