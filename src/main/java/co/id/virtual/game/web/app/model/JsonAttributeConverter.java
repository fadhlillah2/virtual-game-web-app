package co.id.virtual.game.web.app.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.hibernate.HibernateException;

/**
 * JPA AttributeConverter for handling JSON serialization/deserialization.
 * This allows storing complex objects as JSON in the database using @Convert annotation.
 */
@Converter
public class JsonAttributeConverter implements AttributeConverter<Object, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null;
        }
        
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error serializing object to JSON", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            return objectMapper.readValue(dbData, Object.class);
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error deserializing JSON", e);
        }
    }
}