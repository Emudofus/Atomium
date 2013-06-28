package org.atomium.models;

import org.atomium.annotations.Column;
import org.atomium.annotations.PrimaryKey;
import org.atomium.annotations.Table;

/**
 * @author Blackrush
 */
@Table
public class MyDummyModel {
    @Column
    @PrimaryKey
    private int id;
}
