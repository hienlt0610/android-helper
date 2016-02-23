package R.helper;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    public static <T extends BaseFragment> T newInstance(Class<T> clazz) {
        return BaseFragment.newInstance(clazz, null);
    }

    public static <T extends BaseFragment> T newInstance(Class<T> clazz, @Nullable Bundle b) {
        try {
            T s = (T) clazz.newInstance();
            s.setArguments(b == null?new Bundle():b);
            return s;
        }catch (Exception E) {
            return null;
        }
    }

    public void onResumeFromBackStack() {}

    public Drawable getDrawableResource(int resId) {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
        {
            return getContext().getDrawable(resId);
        }
        else
        {
            return getResources().getDrawable(resId);
        }
    }
}
