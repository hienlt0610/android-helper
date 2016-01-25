package R.helper;

/**
 * Created by duynk on 1/22/16.
 */
public class CallbackError extends CallbackResult {
    public CallbackError(CallbackErrorInfo info) {
        super(info);
    }
    public CallbackError(int code, String message) {
        super(new CallbackErrorInfo(code, message));
    }
    public CallbackError(int code) {
        super(new CallbackErrorInfo(code, ""));
    }
    public CallbackError(IIErrorX code, String message) {
        super(new CallbackErrorInfo(code.value(), message));
    }
    public CallbackError(IIErrorX code) {
        super(new CallbackErrorInfo(code.value(), ""));
    }
}
