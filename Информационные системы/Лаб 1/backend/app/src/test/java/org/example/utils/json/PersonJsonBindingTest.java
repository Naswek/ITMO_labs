package org.example.utils.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.time.Instant;
import org.example.dto.PersonInput;
import org.example.entity.Person;
import org.junit.jupiter.api.Test;

class PersonJsonBindingTest {

    @Test
    void parsesAuthorRequestAndSerializesResponse() throws Exception {
        String json = """
                {"name":"Pair author","eyeColor":"GREEN","nationality":"SPAIN",
                 "birthday":"2000-01-02T00:00:00.000Z",
                 "location":{"x":1,"y":2.5,"z":3,"name":"Madrid"}}
                """;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            Person person = jsonb.fromJson(json, PersonInput.class).toEntity();
            assertEquals(Instant.parse("2000-01-02T00:00:00Z"), person.getBirthday().toInstant());
            assertEquals(2.5, person.getLocation().getY());
            Person serializedAgain = jsonb.fromJson(jsonb.toJson(person), Person.class);
            assertEquals(person.getBirthday(), serializedAgain.getBirthday());
        }
    }

    @Test
    void rejectsInvalidBirthdayAsClientInput() {
        PersonInput input = new PersonInput();
        input.setBirthday("not-a-date");
        assertThrows(IllegalArgumentException.class, input::toEntity);
    }
}
