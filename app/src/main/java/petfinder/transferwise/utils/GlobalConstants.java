package petfinder.transferwise.utils;

import android.content.Context;
import android.os.Environment;

public class GlobalConstants {

    public static final String SERVER_SSL_URL = "http://api.petfinder.com";
    public static final String SQL_DB_NAME = "pets_database";
    public static final String PETFINDER_APIKEY = "9b7dfd63eac20a8eb8cf5e183691d5f9";

    public static String getCacheFolder(Context context) {
        return context.getCacheDir().getPath();
    }

}
