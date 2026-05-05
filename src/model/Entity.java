package model;

/**
 * Entity — Abstract root untuk semua domain model.
 * Menyediakan id sebagai primary key universal.
 *
 * POLYMORPHISM: toBytes()/fromBytes() di-override per subclass untuk
 * serialisasi generik via RMSUtil.
 */
public abstract class Entity {

    protected String id;

    public Entity() {}

    public Entity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract String toString();
}
