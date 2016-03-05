package R.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import vietnamworks.com.helper.R;

/**
 * Created by duynk on 12/29/15.
 */
public class Common {
    public final static String DEFAULT_DATETIME_FORMAT = "EEE, d MMM yyyy, HH:mm";

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

    public static long getMillis() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
    }

    public final static long ONE_SEC = 1000;
    public final static long ONE_MINUTE = ONE_SEC*60;
    public final static long HALF_HOUR = ONE_MINUTE*30;
    public final static long ONE_HOUR = ONE_MINUTE*60;
    public final static long HALF_DAY = ONE_HOUR*12;
    public final static long ONE_DAY = ONE_HOUR*24;
    public final static long ONE_WEEK = ONE_DAY*7;
    public final static long ONE_WORKING_WEEK = ONE_DAY*5;
    public final static long HALF_MONTH = ONE_DAY*15;
    public final static long ONE_MONTH = ONE_DAY*30;

    public static class Dialog {
        public static void alert(final Context ctx, String message, final Callback callback) {
            new AlertDialog.Builder(ctx)
                    .setTitle("Alert")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callback.onCompleted(ctx, CallbackResult.success());
                        }
                    }).create().show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String r13(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }

    public static String nowString() {
        return Common.nowString(DEFAULT_DATETIME_FORMAT);
    }


    public static String nowString(String format) {
        java.text.DateFormat df = new SimpleDateFormat(format);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getDateString(Date date) {
        return getDateString(date, DEFAULT_DATETIME_FORMAT);
    }

    public static String getDateString(long timestamp) {
        Date date = new Date(timestamp);
        return getDateString(date, DEFAULT_DATETIME_FORMAT);
    }

    public static String getDateString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDuration(long timestamp) {
        long now = getMillis();
        long minutes = (Math.max(now - timestamp, 0)/1000)/60;

        if (minutes <= 1) { //less than 1 min
            return String.format(BaseActivity.sInstance.getString(R.string.time_1_min_ago), minutes);
        } else if (minutes < 60) {
            return String.format(BaseActivity.sInstance.getString(R.string.time_n_min_ago), minutes);
        } else if (minutes < 2*60) {
            long min = (minutes%60);
            if (min > 2) {
                return String.format(BaseActivity.sInstance.getString(R.string.time_1_h_n_min_ago), min);
            } else {
                return String.format(BaseActivity.sInstance.getString(R.string.time_1_h_1_min_ago), min);
            }
        } else if (minutes < 24*2*60) {
            return String.format(BaseActivity.sInstance.getString(R.string.time_n_h_ago), Math.round(minutes / 60f));
        } else if (minutes < 7*24*60) {
            return String.format(BaseActivity.sInstance.getString(R.string.time_n_d_ago), Math.round(minutes/(24*60f)));
        } else {
            return getDateString(timestamp);
        }
    }
}
