package R.helper;

/**
 * Created by duynk on 12/29/15.
 */
public class CallbackResult {
    public static class CallbackError {
        int errorCode;
        String message;
        public CallbackError(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
        public String getMessage() {return this.message;}
        public int getCode() {return this.errorCode;}
    }
    CallbackError error;
    Object data;

    public boolean hasError() {
        return error != null;
    }

    public CallbackError getError() {
        return this.error;
    }

    public CallbackResult(CallbackError error, Object data) {
        this.error = error;
        this.data = data;
    }

    public CallbackResult(CallbackError error) {
        this.error = error;
        this.data = null;
    }

    public Object getData() {
        return data;
    }
}
