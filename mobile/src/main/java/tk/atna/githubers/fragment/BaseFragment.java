package tk.atna.githubers.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import tk.atna.githubers.stuff.LocalBroadcaster;

public abstract class BaseFragment extends Fragment
                                   implements LocalBroadcaster.LocalActionListener {

    /**
     * Actions to use in activity-fragment communication.
     * As usual represents fragment clicks
     */
    public static final int ACTION_LOADING_EXCEPTION = 0x00000a1;

    private LocalBroadcaster broadcaster;

    /**
     * Fragment action listener
     */
	private FragmentActionCallback callback;


    /**
     * Invokes fragment action callback
     *
     * @param action needed fragment command
     * @param data additional data to send
     */
	public void makeFragmentAction(int action, Bundle data) {
		if (callback != null)
			callback.onAction(action, data);
	}

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // initializes actions callback and
        // proves that hoster activity implements it
		try {
			callback = (FragmentActionCallback) getActivity();

		} catch (ClassCastException e) {
			e.printStackTrace();
			Log.d("myLogs", BaseFragment.class.getSimpleName()
                    + ".onActivityCreated: activity must implement "
                    + FragmentActionCallback.class.getSimpleName());
		}

        broadcaster = new LocalBroadcaster(this);
        // start listen to local broadcaster
        if(getActivity() != null)
            getActivity().registerReceiver(broadcaster,
                    new IntentFilter(LocalBroadcaster.LOCAL_BROADCAST_FILTER));
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // forget local broadcaster
        if(getActivity() != null)
            getActivity().unregisterReceiver(broadcaster);
    }

    /**
     * Callback interface to deliver fragment actions to activity
     */
    public interface FragmentActionCallback {

        /**
         * Called on fragment action event
         *
         * @param action needed command
         * @param data additional data to send
         */
		void onAction(int action, Bundle data);
	}

}
