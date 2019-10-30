package generateAPPID;

import android.util.Base64;
import android.util.Log;

public class APPIDTest {

    public String generateAppId(String packageName) {
        String appId = null;

        if (packageName != null) {
            byte[] encryptKey = CryptUtils.initCryptKey();
            try {
                byte[] encryptedData = CryptUtils.encrypt(encryptKey, packageName.getBytes());
                String base64String = Base64.encodeToString(encryptedData, Base64.NO_WRAP);
                appId = base64String.toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("APPID", "appid:" + appId);

        return appId;
    }
}
