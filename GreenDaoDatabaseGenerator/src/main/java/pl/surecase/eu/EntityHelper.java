package pl.surecase.eu;


import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class EntityHelper {

    private final Entity entity;

    public EntityHelper(Schema schema, String entityName) {
        entity = schema.addEntity(entityName);
    }

    public EntityHelper setIntegerId(boolean autoIncrement) {
        if (autoIncrement) {
            entity.addIdProperty().autoincrement();
            //entity.addIdProperty().autoincrement().primaryKey();
        } else {
            entity.addIdProperty();
        }
        return this;
    }

    public EntityHelper setStringId(String idName) {
        entity.addStringProperty(idName).notNull().unique().primaryKey();
        //entity.addStringProperty(idName).unique().primaryKey(); //.notNull()
        return this;
    }

    public EntityHelper addBooleanProperties(String... properties) {
        for (String propertyName : properties) {
            entity.addBooleanProperty(propertyName);
        }
        return this;
    }

    public EntityHelper addByteArrayProperties(String... properties) {
        for (String propertyName : properties) {
            entity.addByteArrayProperty(propertyName);
        }
        return this;
    }

    public EntityHelper addIntegerProperties(String... properties) {
        for (String propertyName : properties) {
            entity.addIntProperty(propertyName);
        }
        return this;
    }

    public EntityHelper addLongProperties(String... properties) {
        for (String propertyName : properties) {
            entity.addLongProperty(propertyName);
        }
        return this;
    }

    public EntityHelper addStringProperties(String... properties) {
        for (String propertyName : properties) {
            entity.addStringProperty(propertyName);
        }
        return this;
    }

    public Entity getEntity() {
        return entity;
    }

    public Property addForeignKeyProperty(String propertyName, boolean isNullable) {
        if (isNullable) {
            return entity.addStringProperty(propertyName).getProperty();
        } else {
            return entity.addStringProperty(propertyName).notNull().getProperty();
        }
    }

    public Property addForeignKeyLongProperty(String propertyName, boolean isNullable) {
        if (isNullable) {
            return entity.addLongProperty(propertyName).getProperty();
        } else {
            return entity.addLongProperty(propertyName).notNull().getProperty();
        }
    }


    public void singleEntityMapping(Entity target, Property foreignKeyProperty) {
        entity.addToOne(target, foreignKeyProperty);
    }

    public void multipleEntityMapping(Entity target, Property foreignKeyProperty, String mappingName) {
        target.addToMany(entity, foreignKeyProperty, mappingName);
    }

}
