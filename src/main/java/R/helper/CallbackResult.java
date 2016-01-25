package R.helper;

/**
 * Created by duynk on 12/29/15.
 */
public class CallbackResult {
    public static class CallbackErrorInfo {
        int errorCode;
        String message;
        public CallbackErrorInfo(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
        public CallbackErrorInfo(int errorCode) {
            this.errorCode = errorCode;
            this.message = "";
        }
        public CallbackErrorInfo(IIErrorX error, String message) {
            this.errorCode = error.value();
            this.message = message;
        }
        public CallbackErrorInfo(IIErrorX error) {
            this.errorCode = error.value();
            this.message = "";
        }
        public String getMessage() {return this.message;}
        public int getCode() {return this.errorCode;}
    }
    CallbackErrorInfo error;
    protected Object data;

    public boolean hasError() {
        return error != null;
    }

    public CallbackErrorInfo getError() {
        return this.error;
    }

    public CallbackResult(CallbackErrorInfo error, Object data) {
        this.error = error;
        this.data = data;
    }

    public CallbackResult(CallbackErrorInfo error) {
        this.error = error;
        this.data = null;
    }

    public Object getData() {
        return data;
    }
}
