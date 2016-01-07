package R.helper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duynk on 12/29/15.
 */
public class BaseActivity extends AppCompatActivity {
    public static BaseActivity sInstance;
    private Handler handler = new Handler();

    //detect layout changed
    private boolean isKeyboardShown;
    private Rect screenRegion;

    public BaseActivity() {
        super();
        BaseActivity.sInstance = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isKeyboardShown = false;

        final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                boolean isSoftKeyShown = r.height() < getScreenHeight()*0.8;
                isKeyboardShown = isSoftKeyShown;
                screenRegion = r;
                onLayoutChanged(r, isSoftKeyShown);
            }
        });
    }

    public void onLayoutChanged(Rect r, boolean isSoftKeyShown) {
    }

    protected void hideSystemUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setTimeout(final Runnable f, long delay) {
        handler.postDelayed(f, delay);
    }

    public void setTimeout(final Runnable f) {
        handler.post(f);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    public static void openActivity(Class<?> cls, @NonNull Bundle bundle) {
        Intent intent = new Intent(BaseActivity.sInstance, cls);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        BaseActivity.sInstance.startActivity(intent);
    }

    public static void openActivity(Class<?> cls) {
        Intent intent = new Intent(BaseActivity.sInstance, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        BaseActivity.sInstance.startActivity(intent);
    }

    public static void timeout(final Runnable f, long delay) {
        sInstance.setTimeout(f, delay);
    }

    public static void timeout(final Runnable f) {
        sInstance.setTimeout(f);
    }

    public static int[] getScreenSize() {
        Display display = BaseActivity.sInstance.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int s[] = new int[2];
        s[0] = size.x;
        s[1] = size.y;
        return s;
    }

    public static int getScreenHeight() {
        Display display = BaseActivity.sInstance.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth() {
        Display display = BaseActivity.sInstance.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = BaseActivity.sInstance.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = BaseActivity.sInstance.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public void pushFragment(android.support.v4.app.Fragment f, int holder_id, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().add(holder_id, f).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(holder_id, f).commit();
        }
    }

    public void pushFragment(android.support.v4.app.Fragment f, int holder_id) {
        pushFragment(f, holder_id, true);
    }

    public void openFragment(android.support.v4.app.Fragment f, int holder_id, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().replace(holder_id, f).addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(holder_id, f).commit();
        }
    }

    public void openFragmentAndClean(android.support.v4.app.Fragment f, int holder_id) {
        FragmentManager manager = getSupportFragmentManager();
        try {
            if (manager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }catch (Exception E) {
            E.printStackTrace();
        }
        manager.beginTransaction().replace(holder_id, f).commit();
    }

    public void openFragment(android.support.v4.app.Fragment f, int holder_id) {
        openFragment(f, holder_id, false);
    }

    public static void setTitleBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sInstance.getWindow().setStatusBarColor(sInstance.getResources().getColor(color, sInstance.getTheme()));
        }
    }

    public void setActivityTitle(String title) {
        ActionBar b =  getSupportActionBar();
        if (b != null) {
            b.setTitle(title);
        }
    }

    public void setActivityTitle(int text) {
        ActionBar b =  getSupportActionBar();
        if (b != null) {
            b.setTitle(getString(text));
        }
    }

    Callback permissionCallback;
    public void askForPermission(String[] permission, Callback callback) {
        List<String> permissions = new ArrayList<String>();
        if (Common.isMarshMallowOrLater()) {
            for (String p:permission) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(p);
                }
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 3333);
                permissionCallback = callback;
            } else {
                callback.onCompleted(this, new CallbackSuccess());
            }
        } else {
            callback.onCompleted(this, new CallbackSuccess());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch ( requestCode ) {
            case 3333: {
                boolean allAccepted = true;
                for( int i = 0; i < permissions.length; i++ ) {
                    if( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if( grantResults[i] == PackageManager.PERMISSION_DENIED ) {
                        Log.d( "Permissions", "Permission Denied: " + permissions[i] );
                        allAccepted = false;
                    }
                }
                if (permissionCallback != null) {
                    if (allAccepted) {
                        permissionCallback.onCompleted(this, new CallbackSuccess());
                    } else {
                        permissionCallback.onCompleted(this, new CallbackResult(new CallbackResult.CallbackError(-1, null)));
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
