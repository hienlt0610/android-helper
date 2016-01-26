package R.helper;

import android.content.Context;

/**
 * Created by duynk on 12/29/15.
 */
public interface Callback<T> {
    void onCompleted(Context context, CallbackResult<T> result);
}
