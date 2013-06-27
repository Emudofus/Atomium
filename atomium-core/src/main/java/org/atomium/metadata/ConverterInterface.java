package org.atomium.metadata;

import org.atomium.NamedValues;
import org.atomium.metadata.ColumnMetadata;

/**
 * @author Blackrush
 */
public interface ConverterInterface {
    /**
     * this method will extract values from the database and convert it to the proper type
     * @param column entity's column that will be set
     * @param instance instance that will receive the values
     * @param input the values read from the database
     * @param <T> entity's type
     * @return <code>true</code> if this method has extracted any values, <code>false</code> otherwise
     */
    <T> boolean extract(ColumnMetadata<T> column, T instance, NamedValues input);

    /**
     * this method will export values from the entity to the proper database type
     * @param column enttiy's column that will be exported
     * @param instance instance that will have exported values
     * @param output the exported values
     * @param <T> entity's type
     * @return <code>true</code> if this method has exported any values, <code>false</code> otherwise
     */
    <T> boolean export(ColumnMetadata<T> column, T instance, NamedValues output);
}
