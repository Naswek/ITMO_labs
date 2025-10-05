package org.example.storage;

import org.example.cords.Result;
import java.util.ArrayList;


public class AppStorage {
    private final ArrayList<Result> results = new ArrayList<Result>();

    public synchronized void addResult(Result result) {
        results.add(result);
    }

    public synchronized ArrayList<Result> getResults() {
        return new ArrayList<>(results);
    }

    public synchronized void clearResults() {
        results.clear();
    }
}
