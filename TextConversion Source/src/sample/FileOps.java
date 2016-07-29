package sample;

import java.util.ArrayList;

/**
 * Created by alperen on 28.07.2016.
 */
public class FileOps {

    DbObserver dbObserver;
    TCDatabase tcDatabase;

    public FileOps(DbObserver dbObserver){
        this.dbObserver=dbObserver;
        tcDatabase=new TCDatabase();

        new Thread(){
            @Override
            public void run() {
                super.run();
                if(tcDatabase.checkDb()) dbObserver.dbIsReady();
            }
        }.start();
    }

    public void writeCheckedsToFile(ArrayList<String> strings){
        new Thread(){
            @Override
            public void run() {
                super.run();
                tcDatabase.deleteAllEntries();
                tcDatabase.addEntries(strings);
                dbObserver.dbResultsWritten();
            }
        }.start();
    }

    public void readCheckedsFromFile(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                dbObserver.dbResultsReady(tcDatabase.getAllIds());
            }
        }.start();
    }

}
