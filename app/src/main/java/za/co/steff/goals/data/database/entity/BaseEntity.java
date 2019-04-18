package za.co.steff.goals.data.database.entity;

import org.joda.time.DateTime;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Id;
import za.co.steff.goals.data.database.converter.DateTimeConverter;

@io.objectbox.annotation.BaseEntity
public class BaseEntity {

    @Id
    private long id;
    @Convert(converter = DateTimeConverter.class, dbType = Long.class)
    private DateTime createdAt = DateTime.now();
    @Convert(converter = DateTimeConverter.class, dbType = Long.class)
    private DateTime updatedAt = DateTime.now();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
