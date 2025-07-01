package itmo.programming.creator;

import itmo.programming.ParseLocalDate;
import itmo.programming.creator.fields.CarCreate;
import itmo.programming.creator.fields.CoordinatesValue;
import itmo.programming.creator.fields.CreationDate;
import itmo.programming.creator.fields.ImpactSpeed;
import itmo.programming.creator.fields.IsHasToothpick;
import itmo.programming.creator.fields.IsHero;
import itmo.programming.creator.fields.MoodCreate;
import itmo.programming.creator.fields.NamePerson;
import itmo.programming.creator.fields.Soundtrack;
import itmo.programming.creator.fields.Validation;
import itmo.programming.creator.fields.Weapon;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.exceptions.InvalidScriptInputException;
import itmo.programming.input.ConsoleInput;
import itmo.programming.input.InputSource;
import itmo.programming.object.HumanBeing;
import itmo.programming.output.console.ConsoleOutput;
import java.util.NoSuchElementException;

/**
 * The type Prompt objects.
 */
public class PromptObjects {

    private final NamePerson name;
    private final CoordinatesValue coordinatesValue;
    private final IsHero isHero;
    private final IsHasToothpick isHasToothpick;
    private final ImpactSpeed impactSpeed;
    private final Soundtrack soundtrack;
    private final Weapon weapon;
    private final MoodCreate moodCreate;
    private final CarCreate carCreate;
    private final CreationDate creationDate;
    private final ConsoleInput humanReader;


    /**
     * Instantiates a new Prompt objects.
     *
     * @param printer the printer
     * @param reader  the reader
     */
    public PromptObjects(ConsoleOutput printer, ConsoleInput reader) {
        this.humanReader = reader;
        final Validation validation = new Validation();
        final InputSource inputSource = new InputSource(humanReader);
        this.name = new NamePerson(inputSource, printer);
        this.coordinatesValue = new CoordinatesValue(inputSource, validation, printer);
        this.isHero = new IsHero(inputSource, validation, printer);
        this.isHasToothpick = new IsHasToothpick(inputSource, validation, printer);
        this.impactSpeed = new ImpactSpeed(inputSource, validation, printer);
        this.soundtrack = new Soundtrack(inputSource, printer);
        this.weapon = new Weapon(inputSource, validation, printer);
        this.moodCreate = new MoodCreate(printer, inputSource, validation);
        this.carCreate = new CarCreate(inputSource, validation, printer);
        this.creationDate = new CreationDate(new ParseLocalDate(), inputSource);
    }

    /**
     * Create human human being.
     *
     * @return the human being
     * @throws FailedExecution the failed execution
     */
    public HumanBeing createHuman() throws FailedExecution {
        if (humanReader.isSimulatedStream()) {
            return createFromScript();
        }
        return createHumanFromConsole();
    }


    /**
     * Create human from console human being.
     *
     * @return the human being
     */
    public HumanBeing createHumanFromConsole() {
        return new HumanBeing(0,
                name.createNameField(),
                coordinatesValue.createCoordinatesField(),
                creationDate.createCreationDate(),
                isHero.createIsHeroField(),
                isHasToothpick.createIsToothpickField(),
                impactSpeed.createImpactSpeedField(),
                soundtrack.createSoundtrackField(),
                weapon.createWeaponField(),
                moodCreate.createMoodField(),
                carCreate.createCarField(),
                0);
    }

    /**
     * Create from script human being.
     *
     * @return the human being
     * @throws InvalidScriptInputException the invalid script input exception
     * @throws FailedExecution             the failed execution
     */
    public HumanBeing createFromScript() throws InvalidScriptInputException, FailedExecution {
        try {
            return new HumanBeing(
                    0,
                    name.createNameFromScript(),
                    coordinatesValue.createCoordinatesFromScript(),
                    creationDate.createCreationDateFromScript(),
                    isHero.createIsHeroFieldFromScript(),
                    isHasToothpick.createIsHasToothpickFromScript(),
                    impactSpeed.createImpactSpeedFromScript(),
                    soundtrack.createSoundtrackFromScript(),
                    weapon.createWeaponTypeFieldFromScript(),
                    moodCreate.createMoodFieldFromScript(),
                    carCreate.createCarFromScript(),
                    0);

        } catch (NoSuchElementException | InvalidScriptInputException e) {
            throw new FailedExecution("Не удалось выполнить команду: " + e.getMessage());
        }
    }
}
