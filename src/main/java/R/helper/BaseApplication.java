package R.helper;

import android.support.annotation.CallSuper;

/**
 * Created by duynk on 12/29/15.
 */
public class BaseApplication  extends android.app.Application {

    public enum FontType {
        sDefaults("sDefaults"),
        DEFAULT("DEFAULT"),
        DEFAULT_BOLD("DEFAULT_BOLD"),
        MONOSPACE("MONOSPACE"),
        SERIF("SERIF"),
        SANS_SERIF("SANS_SERIF");

        private final String value;

        private FontType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }
    static BaseApplication sInstance;

    public BaseApplication() {
        super();
        //Test.testEntity();
        sInstance = this;
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        LocalStorage.init(this);
    }

    @CallSuper
    @Override
    public void onTerminate() {
        LocalStorage.close();
        super.onTerminate();
    }


    public static void setDefaultFont(FontType type, String filename) {
        FontsOverride.setDefaultFont(sInstance, type.toString(), filename);
    }
}
