package R.helper;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * Created by duynk on 12/29/15.
 */
public class Common {
    public static boolean isLollipopOrLater() {
        return Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isMarshMallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isVersionOrLater(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static int sign(float a) {
        return a > 0?1:(a<0?-1:0);
    }

    public static float lerp(float start, float end, float percent) {
        float dt = Math.abs(start - end);
        float min = Math.max(Math.abs(start) * 0.1f, Math.abs(end) * 0.1f);
        if (dt <= min) {
            start = end;
        }
        return (start + percent * (end - start));
    }

    public static int lerp(int start, int end, float percent) {
        float dt = Math.abs(start - end);
        float min = Math.max(Math.abs(start) * 0.1f, Math.abs(end) * 0.1f);
        if (dt <= min) {
            start = end;
        }
        return (int) (start + percent * (end - start));
    }

    public static float convertDpToPixel(float dp) {
        Resources resources = BaseApplication.sInstance.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = BaseApplication.sInstance.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static void acceptAllSSL() {
        NukeSSLCerts.nuke();
    }

    public static String join(List<String> list,@NonNull String delim) {
        String tmp_delim = "";
        StringBuilder sb = new StringBuilder();
        for (String s:list) {
            sb.append(tmp_delim);
            sb.append(s);
            tmp_delim = delim;
        }
        return sb.toString();
    }
}
