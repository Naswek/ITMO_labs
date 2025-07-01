package itmo.programming.creator.fields;

import itmo.programming.ParseLocalDate;
import itmo.programming.input.InputSource;
import java.time.LocalDateTime;

/**
 * The type Creation date.
 */
public class CreationDate {

    private final ParseLocalDate localDateParser;
    private final InputSource input;

    /**
     * Instantiates a new Creation date.
     *
     * @param localDateParser the local date parser
     * @param input           the input
     */
    public CreationDate(ParseLocalDate localDateParser, InputSource input) {
        this.localDateParser = localDateParser;
        this.input = input;
    }


    /**
     * Create creation date local date time.
     *
     * @return the local date time
     */
    public LocalDateTime createCreationDate() {
        return LocalDateTime.now();
    }

    /**
     * Create creation date from script local date time.
     *
     * @return the local date time
     */
    public LocalDateTime createCreationDateFromScript() {
        return localDateParser.parseStringToLocalDate(input.getTextFromUser().toString());
    }
}
