package org.example.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.utils.Email;

@Converter
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email != null ? email.getValue() : null;
    }

    @Override
    public Email convertToEntityAttribute(String email) {
        return email != null ? new Email(email) : null;
    }
}
