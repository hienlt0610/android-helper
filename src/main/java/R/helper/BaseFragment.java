package R.helper;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by duynk on 1/4/16.
 */
public class BaseFragment extends Fragment {
    public BaseFragment() {
        super();
    }

    public <T extends BaseActivity> T getActivityRef(Class<T> cls) {
        Activity act = this.getActivity();
        if (act != null && (act instanceof BaseActivity)) {
            return (T)act;
        }
        return null;
    }

    public void onResumeFromBackStack() {}
}
