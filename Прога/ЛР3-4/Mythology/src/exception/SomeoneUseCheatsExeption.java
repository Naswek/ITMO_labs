package exception;

import java.io.IOException;

public class SomeoneUseCheatsExeption extends IOException {
    public SomeoneUseCheatsExeption(String message) {
        super(message);
    }
}
