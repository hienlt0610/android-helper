package R.helper;

import android.support.annotation.NonNull;

/**
 * Created by duynk on 1/25/16.
 */
public class CallbackResult<T> {
    protected CallbackResult() {
    }

    public static <T> CallbackResult<T> error() {
        CallbackResult<T> obj = new CallbackResult<>();
        obj.data = null;
        obj.error = new ICallbackError() {
            @Override
            public int getCode() {
                return -1;
            }

            @Override
            public String getMessage() {
                return "";
            }

            @Override
            public boolean is(ICallbackError error ) {return getCode() == error.getCode();}

            @Override
            public boolean is(int error ) {return getCode() == error;}

        };
        return obj;
    }

    public static <T> CallbackResult<T> error(ICallbackError error) {
        CallbackResult<T> obj = new CallbackResult<>();
        obj.data = null;
        obj.error = error;
        return obj;
    }

    public static <T> CallbackResult<T> error(final int error) {
        CallbackResult<T> obj = new CallbackResult<>();
        obj.data = null;
        obj.error = new ICallbackError() {
            @Override
            public int getCode() {
                return error;
            }

            @Override
            public String getMessage() {
                return "";
            }

            @Override
            public boolean is(ICallbackError error ) {return getCode() == error.getCode();}

            @Override
            public boolean is(int error ) {return getCode() == error;}
        };
        return obj;
    }

    public static <T> CallbackResult<T> error(final int error,@NonNull final String message) {
        CallbackResult<T> obj = new CallbackResult<>();
        obj.data = null;
        obj.error = new ICallbackError() {
            @Override
            public int getCode() {
                return error;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public boolean is(ICallbackError error ) {return getCode() == error.getCode();}

            @Override
            public boolean is(int error ) {return getCode() == error;}
        };
        return obj;
    }

    public static <T> CallbackResult<T> error(@NonNull final String message) {
        CallbackResult<T> obj = new CallbackResult<>();
        obj.data = null;
        obj.error = new ICallbackError() {
            @Override
            public int getCode() {
                return -1;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public boolean is(ICallbackError error ) {return getCode() == error.getCode();}

            @Override
            public boolean is(int error ) {return getCode() == error;}
        };
        return obj;
    }


    public static <T> CallbackResult<T> success(@NonNull T data) {
        CallbackResult<T> obj = new CallbackResult<>();
        obj.data = data;
        obj.error = null;
        return obj;
    }

    public static <T> CallbackResult<T> success() {
        CallbackResult<T> obj = new CallbackResult<>();
        obj.data = null;
        obj.error = null;
        return obj;
    }


    public interface ICallbackError {
        int getCode();
        String getMessage();
        boolean is(ICallbackError error);
        boolean is(int error);
    }

    protected T data;
    protected ICallbackError error;

    public boolean hasError() {
        return error != null;
    }

    public T getData() {
        return data;
    }

    public T getData(Class<T> clz) {
        return data;
    }

    public ICallbackError getError() {
        return error;
    }
}
