package petfinder.transferwise.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PET".
*/
public class PetDao extends AbstractDao<Pet, Long> {

    public static final String TABLENAME = "PET";

    /**
     * Properties of entity Pet.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Pet_id = new Property(1, Integer.class, "pet_id", false, "PET_ID");
        public final static Property Age = new Property(2, String.class, "age", false, "AGE");
        public final static Property Size = new Property(3, String.class, "size", false, "SIZE");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property Sex = new Property(5, String.class, "sex", false, "SEX");
        public final static Property Description = new Property(6, String.class, "description", false, "DESCRIPTION");
        public final static Property Breed = new Property(7, String.class, "breed", false, "BREED");
        public final static Property Animal = new Property(8, String.class, "animal", false, "ANIMAL");
        public final static Property Last_update = new Property(9, String.class, "last_update", false, "LAST_UPDATE");
    }

    private DaoSession daoSession;


    public PetDao(DaoConfig config) {
        super(config);
    }
    
    public PetDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PET\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PET_ID\" INTEGER," + // 1: pet_id
                "\"AGE\" TEXT," + // 2: age
                "\"SIZE\" TEXT," + // 3: size
                "\"NAME\" TEXT," + // 4: name
                "\"SEX\" TEXT," + // 5: sex
                "\"DESCRIPTION\" TEXT," + // 6: description
                "\"BREED\" TEXT," + // 7: breed
                "\"ANIMAL\" TEXT," + // 8: animal
                "\"LAST_UPDATE\" TEXT);"); // 9: last_update
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PET\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Pet entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer pet_id = entity.getPet_id();
        if (pet_id != null) {
            stmt.bindLong(2, pet_id);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(3, age);
        }
 
        String size = entity.getSize();
        if (size != null) {
            stmt.bindString(4, size);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(6, sex);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        String breed = entity.getBreed();
        if (breed != null) {
            stmt.bindString(8, breed);
        }
 
        String animal = entity.getAnimal();
        if (animal != null) {
            stmt.bindString(9, animal);
        }
 
        String last_update = entity.getLast_update();
        if (last_update != null) {
            stmt.bindString(10, last_update);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Pet entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer pet_id = entity.getPet_id();
        if (pet_id != null) {
            stmt.bindLong(2, pet_id);
        }
 
        String age = entity.getAge();
        if (age != null) {
            stmt.bindString(3, age);
        }
 
        String size = entity.getSize();
        if (size != null) {
            stmt.bindString(4, size);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(5, name);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(6, sex);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(7, description);
        }
 
        String breed = entity.getBreed();
        if (breed != null) {
            stmt.bindString(8, breed);
        }
 
        String animal = entity.getAnimal();
        if (animal != null) {
            stmt.bindString(9, animal);
        }
 
        String last_update = entity.getLast_update();
        if (last_update != null) {
            stmt.bindString(10, last_update);
        }
    }

    @Override
    protected final void attachEntity(Pet entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Pet readEntity(Cursor cursor, int offset) {
        Pet entity = new Pet( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // pet_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // age
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // size
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // sex
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // description
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // breed
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // animal
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // last_update
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Pet entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPet_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setAge(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSize(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSex(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDescription(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setBreed(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAnimal(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLast_update(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Pet entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Pet entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Pet entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
