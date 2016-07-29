package sample;

import java.util.ArrayList;

/**
 * Created by alperen on 29.07.2016.
 */
public interface DbObserver {
    void dbIsReady();
    void dbResultsReady(ArrayList<String> strings);
    void dbResultsWritten();

}
