package za.co.steff.goals.data.database.converter;

import org.joda.time.DateTime;

import io.objectbox.converter.PropertyConverter;

public class DateTimeConverter implements PropertyConverter<DateTime, Long> {

    @Override
    public DateTime convertToEntityProperty(Long databaseValue) {
        return new DateTime(databaseValue);
    }

    @Override
    public Long convertToDatabaseValue(DateTime entityProperty) {
        return entityProperty.getMillis();
    }
}
