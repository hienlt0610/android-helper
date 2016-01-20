package R.helper;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by duynk on 1/20/16.
 */
public class CodecX {
    private final static String LOCALSTORAGE_KEY = "vnwapi_codec";
    private static HashMap<String, String> encodeMap;
    private static HashMap<String, String> decodeMap;

    private static void buildCodec() {
        char[] ch = new char[26*2 + 10];
        int index = 0;
        for (char c = '0'; c <= '9'; c++) {
            ch[index++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            ch[index++] = c;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            ch[index++] = c;
        }
        char[] ch2 = ch.clone();
        Random rnd = new Random();
        for (int i = ch.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            char a = ch[j];
            ch[j] = ch[i];
            ch[i] = a;
        }

        encodeMap = new HashMap<>();
        decodeMap = new HashMap<>();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < ch2.length; i++) {
            String k = ch2[i] + "";
            String v = ch[i] + "";
            encodeMap.put(k, v);
            decodeMap.put(v, k);
            code.append(v);
            code.append(k);
        }
        LocalStorage.set(LOCALSTORAGE_KEY, code.toString());
    }

    private static void loadCodec(String code) {
        if (code.length() != (26*2 + 10)*2) {
            encodeMap = null;
            decodeMap = null;
            LocalStorage.remove(LOCALSTORAGE_KEY);
        } else {
            encodeMap = new HashMap<>();
            decodeMap = new HashMap<>();
            for (int i = 0; i < code.length(); i+=2) {
                String v = code.charAt(i) + "";
                String k = code.charAt(i + 1) + "";
                encodeMap.put(k, v);
                decodeMap.put(v, k);
            }
        }
    }

    @Nullable
    public static String encode(String str) {
        if (encodeMap == null) {
            String code = LocalStorage.getString(LOCALSTORAGE_KEY, null);
            if (code == null) {
                buildCodec();
            } else {
                loadCodec(code);
            }
        }
        if (encodeMap != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                sb.append(encodeMap.get(str.charAt(i) + ""));
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    @Nullable
    public static String decode(String str) {
        if (decodeMap == null) {
            String code = LocalStorage.getString(LOCALSTORAGE_KEY, null);
            if (code == null) {
                buildCodec();
            } else {
                loadCodec(code);
            }
        }
        if (decodeMap != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                sb.append(decodeMap.get(str.charAt(i) + ""));
            }
            return sb.toString();
        } else {
            return null;
        }
    }
}
