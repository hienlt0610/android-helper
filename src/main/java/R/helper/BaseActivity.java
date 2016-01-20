package R.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import vietnamworks.com.helper.R;

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


    public static void pushFragment(android.support.v4.app.Fragment f, int holder_id) {
        sInstance.getSupportFragmentManager().beginTransaction().add(holder_id, f).addToBackStack(null).commit();
    }

    public static void replaceFragment(android.support.v4.app.Fragment f, int holder_id) {
        sInstance.getSupportFragmentManager().popBackStack();
        sInstance.getSupportFragmentManager().beginTransaction().add(holder_id, f).addToBackStack(null).commit();
    }

    public static void openFragment(android.support.v4.app.Fragment f, int holder_id, boolean addToBackStack) {
        if (addToBackStack) {
            sInstance.getSupportFragmentManager().beginTransaction().replace(holder_id, f).addToBackStack(null).commit();
        } else {
            sInstance.getSupportFragmentManager().beginTransaction().replace(holder_id, f).commit();
        }
    }

    public static void openFragmentAndClean(android.support.v4.app.Fragment f, int holder_id) {
        FragmentManager manager = sInstance.getSupportFragmentManager();
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

    public static void openFragment(android.support.v4.app.Fragment f, int holder_id) {
        openFragment(f, holder_id, false);
    }

    static int getTransformAndBoundsTransition() {
        return R.transition.transform_n_bounds;
    }
    public static class ShareAnimationView {
        View ele;
        String anim_uid;
        public ShareAnimationView(View v, String anim_uid) {
            this.ele = v;
            this.anim_uid = anim_uid;
        }
    }

    public static void pushFragmentAnimateTransition(android.support.v4.app.Fragment endFragment, int holder_id, Bundle bundle, ShareAnimationView... sharedViews) {
        pushFragmentAnimateTransition(endFragment, holder_id, bundle, 0, 0, sharedViews);
    }
    public static void pushFragmentAnimateTransition(android.support.v4.app.Fragment endFragment, int holder_id, Bundle bundle, int start_anim, int end_anim, ShareAnimationView... sharedViews) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaseFragment currentFragment = (BaseFragment)sInstance.getSupportFragmentManager().findFragmentById(holder_id);
            if (start_anim <= 0) {
                start_anim = R.transition.transform_n_bounds;
            }
            if (end_anim <= 0) {
                end_anim = android.R.transition.fade;
            }

            currentFragment.setSharedElementReturnTransition(TransitionInflater.from(sInstance).inflateTransition(start_anim));
            currentFragment.setExitTransition(TransitionInflater.from(sInstance).inflateTransition(end_anim));

            endFragment.setSharedElementEnterTransition(TransitionInflater.from(sInstance).inflateTransition(start_anim));
            endFragment.setEnterTransition(TransitionInflater.from(sInstance).inflateTransition(android.R.transition.fade));
            endFragment.setArguments(bundle);

            FragmentTransaction trans = sInstance.getSupportFragmentManager().beginTransaction()
                    .add(holder_id, endFragment)
                    .hide(currentFragment)
                    .addToBackStack(null);
            if (sharedViews != null && sharedViews.length > 0) {
                for (ShareAnimationView v: sharedViews) {
                    v.ele.setTransitionName(v.anim_uid);
                    trans.addSharedElement(v.ele, v.anim_uid);
                }
            }
            trans.commit();
        } else {
            pushFragment(endFragment, holder_id);
        }
    }

    public static void popFragment() {
        FragmentManager manager = sInstance.getSupportFragmentManager();
        manager.popBackStackImmediate();
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

    public static void showActionBar() {
        ActionBar b =  sInstance.getSupportActionBar();
        if (b != null) {
            b.show();
        }
    }

    public static void hideActionBar() {
        ActionBar b =  sInstance.getSupportActionBar();
        if (b != null) {
            b.hide();
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

    public static void hideKeyboard() {
        View view = sInstance.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard() {
        View view = sInstance.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }
}
