package co.id.virtual.game.web.app.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

/**
 * Custom Hibernate type for handling JSON serialization/deserialization.
 * This allows storing complex objects as JSON in the database.
 */
public class JsonType implements UserType<Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public Class<Object> returnedClass() {
        return Object.class;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) {
        return Objects.hashCode(x);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) 
            throws SQLException {
        String json = rs.getString(position);
        if (json == null) {
            return null;
        }
        
        try {
            return objectMapper.readValue(json, returnedClass());
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error deserializing JSON", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) 
            throws SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }
        
        try {
            String json = objectMapper.writeValueAsString(value);
            st.setObject(index, json, Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error serializing object to JSON", e);
        }
    }

    @Override
    public Object deepCopy(Object value) {
        if (value == null) {
            return null;
        }
        
        try {
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, returnedClass());
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error during deep copy", e);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) {
        if (value == null) {
            return null;
        }
        
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error disassembling value", e);
        }
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        if (cached == null) {
            return null;
        }
        
        try {
            return objectMapper.readValue(cached.toString(), returnedClass());
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error assembling value", e);
        }
    }

    @Override
    public Object replace(Object original, Object target, Object owner) {
        return deepCopy(original);
    }
}
