package org.atomium;

import org.atomium.annotation.Column;
import org.atomium.annotation.Table;
import org.atomium.entity.Model;
import org.atomium.pkey.IntKey;
import org.joda.time.Instant;

/**
 * @author blackrush
 */
@Table("my_custom_model")
public class MyModel extends Model<IntKey> {

    @Column("my_custom_name")
    private String name;

    /**
     * default constructor
     *
     * @param pk        entity's primary key (must not be null)
     * @param createdAt the instant when this instance has been created (must not be null)
     * @param name      entity's name
     */
    public MyModel(IntKey pk, Instant createdAt, String name) {
        super(pk, createdAt);
        this.name = name;
    }

    public Integer getId() {
        return primaryKey.toNumber();
    }

    public void setId(Integer id) {
        this.primaryKey = IntKey.of(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
