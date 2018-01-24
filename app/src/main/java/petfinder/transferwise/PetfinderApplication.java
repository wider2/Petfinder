package petfinder.transferwise;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import petfinder.transferwise.dao.DaoMaster;
import petfinder.transferwise.dao.DaoSession;
import petfinder.transferwise.dao.managers.MyOpenHelper;
import petfinder.transferwise.utils.GlobalConstants;


public class PetfinderApplication extends Application {

    SQLiteDatabase db;

    private static DaoSession mDaoSession;
    DaoMaster.OpenHelper mInstance;
    //private RefWatcher refWatcher;
    @NonNull
    private static PetfinderApplication instance;

    public PetfinderApplication() {
        instance = this;
    }

    @NonNull
    public static PetfinderApplication get() {
        return instance;
    }


    public DaoSession initializeDB() {

        mInstance = new MyOpenHelper(getApplicationContext(), GlobalConstants.SQL_DB_NAME, null);
        if (db == null) {
            db = mInstance.getWritableDatabase();
            System.out.println("initializeDB->: db==null");
        } else {
            if (!db.isOpen()) {
                db = mInstance.getWritableDatabase();
                System.out.println("initializeDB->: db!=null && !db.isOpen()");
            } else {
                System.out.println("initializeDB->: db!=null && db.isOpen()");
            }
        }

        //clearDatabase(getBaseContext());

        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        return mDaoSession;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public DaoSession getOrCreateDaoSession() {
        DaoSession mDaoSession = PetfinderApplication.get().getDaoSession();
        if (mDaoSession == null) mDaoSession = PetfinderApplication.get().initializeDB();
        return mDaoSession;
    }


    public static void clearDatabase(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                context.getApplicationContext(), GlobalConstants.SQL_DB_NAME, null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        devOpenHelper.onUpgrade(db, 0, 0);
        devOpenHelper.close();
    }


    public void closeDatabase() {
        if (db != null) db.close();
        mDaoSession = null;
    }

    @Override
    protected void finalize() throws Throwable {
        Log.v("VircaApplication", "finalize is call");
        if (db != null) db.close();
        super.finalize();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //refWatcher = LeakCanary.install(this);
        //https://convertcase.net/
        try {
            SQLiteDatabase dbo = openOrCreateDatabase(GlobalConstants.SQL_DB_NAME, Context.MODE_PRIVATE, null);

            dbo.execSQL("CREATE TABLE IF NOT EXISTS CATALOGUE(_id INTEGER PRIMARY KEY AUTOINCREMENT, BREED VARCHAR, ANIMAL VARCHAR);");

            dbo.execSQL("CREATE TABLE IF NOT EXISTS PET(_id INTEGER PRIMARY KEY AUTOINCREMENT, TIMESTAMP INTEGER, PET_ID INTEGER, AGE VARCHAR, SIZE VARCHAR, NAME VARCHAR, SEX VARCHAR, DESCRIPTION VARCHAR, BREED VARCHAR, ANIMAL VARCHAR, LAST_UPDATE VARCHAR);");

            dbo.execSQL("CREATE TABLE IF NOT EXISTS PET__MEDIA(_id INTEGER PRIMARY KEY AUTOINCREMENT, PET_ID INTEGER, PHOTO VARCHAR, SIZE VARCHAR);");

            dbo.execSQL("CREATE TABLE IF NOT EXISTS PET__CONTACT(_id INTEGER PRIMARY KEY AUTOINCREMENT, PET_ID INTEGER, STATE VARCHAR, CITY VARCHAR, ZIP VARCHAR, ADDRESS VARCHAR, PHONE VARCHAR, EMAIL VARCHAR);");

            dbo.execSQL("CREATE TABLE IF NOT EXISTS PET__BREED(_id INTEGER PRIMARY KEY AUTOINCREMENT, PET_ID INTEGER, BREED VARCHAR);");
            dbo.close();;

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
*/

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
