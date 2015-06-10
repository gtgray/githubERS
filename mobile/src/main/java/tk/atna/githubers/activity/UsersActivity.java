package tk.atna.githubers.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import tk.atna.githubers.R;
import tk.atna.githubers.fragment.BaseFragment;
import tk.atna.githubers.fragment.UsersFragment;
import tk.atna.githubers.stuff.Utils;


public class UsersActivity extends AppCompatActivity
                           implements BaseFragment.FragmentActionCallback {


    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar toolbar= ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(false);
            // lets toolbar title to be visible
            actionbar.setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle(R.string.users_title);

        // shadow under toolbar on new devices
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View shadow = findViewById(R.id.shadow_prelollipop);
            shadow.setVisibility(View.GONE);
            toolbar.setElevation(8);
        }

        if (savedInstanceState == null) {
            Utils.parkFragment(getSupportFragmentManager(),
                                R.id.content,
                                UsersFragment.class,
                                null, false);
        }

    }

    @Override
    public void onAction(int action, Bundle data) {
        switch (action) {
            case BaseFragment.ACTION_LOADING_EXCEPTION:
                Toast.makeText(this, R.string.loading_failed, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
