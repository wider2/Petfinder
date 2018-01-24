package petfinder.transferwise.dao.managers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.Random;

import petfinder.transferwise.PetfinderApplication;
import petfinder.transferwise.dao.Catalogue;
import petfinder.transferwise.dao.CatalogueDao;
import petfinder.transferwise.dao.DaoMaster;
import petfinder.transferwise.dao.DaoSession;
import petfinder.transferwise.dao.Pet;
import petfinder.transferwise.dao.PetDao;
import petfinder.transferwise.dao.Pet_Breed;
import petfinder.transferwise.dao.Pet_BreedDao;
import petfinder.transferwise.dao.Pet_Contact;
import petfinder.transferwise.dao.Pet_ContactDao;
import petfinder.transferwise.dao.Pet_Media;
import petfinder.transferwise.dao.Pet_MediaDao;

public class MyOpenHelper extends DaoMaster.OpenHelper {

    private Context mContext;

    public MyOpenHelper(Context context, String name) {
        super(context, name);
        mContext = context;
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion != newVersion) {
                MigrationHelper.migrate(db,
                        CatalogueDao.class, PetDao.class, Pet_BreedDao.class, Pet_MediaDao.class, Pet_ContactDao.class
                );
            }
        } catch (Exception e) {
            if (oldVersion != newVersion) {
                for (int migrateVersion = oldVersion + 1; migrateVersion <= newVersion; migrateVersion++) {
                    upgrade(db, migrateVersion);
                }
            }
            e.printStackTrace();
        }
    }


    /**
     * in case of android.database.sqlite.SQLiteException, the schema version is
     * left untouched just fix the code in the version case and push a new
     * release
     *
     * @param db
     * @param migrateVersion
     */
    private void upgrade(SQLiteDatabase db, int migrateVersion) {
        switch (migrateVersion) {
            case 2:
                try {
                    //db.execSQL("ALTER TABLE USER_PROFILE ADD COLUMN 'PHONE_BOOK_NAME' TEXT DEFAULT '" + Build.MODEL + "';");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    protected void finalize() throws Throwable {
        Log.v("MyOpenHelper", "finalize is call");
        this.close();
        super.finalize();
    }


    public static Pet getPetById(Integer petId) {
        QueryBuilder<Pet> qb = PetfinderApplication.get().getDaoSession().getPetDao().queryBuilder();
        qb.where(PetDao.Properties.Pet_id.eq(petId));
        List<Pet> list = qb.list();
        if (list.size()>0) {
            return list.get(0);
        } else {
            return qb.unique();
        }
    }


    public static Pet getBreedRandom(String breed) {

        Integer rIndex = 0, max=0, petId=0;
        QueryBuilder<Pet> qb = PetfinderApplication.get().getDaoSession().getPetDao().queryBuilder();
        qb.where(PetDao.Properties.Breed.eq(breed));
        qb.build();

        max = qb.list().size();
        List<Pet> list = qb.list();
        if (!list.isEmpty()) {
            Random r = new Random();
            if (max >1) rIndex = r.nextInt((max - 1) + 1);
            petId = list.get(rIndex).getPet_id();
        }

        QueryBuilder<Pet> qbr = PetfinderApplication.get().getDaoSession().getPetDao().queryBuilder();
        qbr.where(PetDao.Properties.Pet_id.eq(petId));
        qbr.build();


        List<Pet> listPet = qbr.list();
        if (listPet.size()>0) {
            return listPet.get(0);
        } else {
            return new Pet();
        }
    }


    public static List<Pet_Media> getPetMediaById(Integer petId) {
        QueryBuilder<Pet_Media> qb = PetfinderApplication.get().getDaoSession().getPet_MediaDao().queryBuilder();
        qb.where(Pet_MediaDao.Properties.Pet_id.eq(petId));
        return qb.list();
    }


    public static List<Pet_Breed> getPetBreedById(Integer petId) {
        QueryBuilder<Pet_Breed> qb = PetfinderApplication.get().getDaoSession().getPet_BreedDao().queryBuilder();
        qb.where(Pet_BreedDao.Properties.Pet_id.eq(petId));
        return qb.list();
    }


    public static Pet_Contact getPetContactById(Integer petId) {
        QueryBuilder<Pet_Contact> qb = PetfinderApplication.get().getDaoSession().getPet_ContactDao().queryBuilder();
        qb.where(Pet_ContactDao.Properties.Pet_id.eq(petId));
        List<Pet_Contact> listPet = qb.list();
        if (listPet.size()>0) {
            return listPet.get(0);
        } else {
            return new Pet_Contact();
        }
    }


    public static List<Catalogue> getCatalogue() {
        PetfinderApplication instance = PetfinderApplication.get();
        DaoSession dao = instance.getDaoSession();
        CatalogueDao catDao = dao.getCatalogueDao();
        QueryBuilder<Catalogue> qb = catDao.queryBuilder();

        qb.where(CatalogueDao.Properties.Breed.notEq(""));
        return qb.list();
    }

}