package transmission;

import interfaces.IConnection;
import interfaces.IProgressControllable;
import interfaces.IQueryable;
import interfaces.IStatus;

import java.io.File;
import java.io.Serializable;

/**
 * A transmission refers to the transmission of a single file
 */
public class Transmission implements IQueryable, IProgressControllable, IStatus, IConnection, Serializable {

    // The file in this transmission
    File file;

    // the transmission progress, measured in bytes
    long finishedLength = 0;

    // Possible status are "ongoing"， “suspended”, "finished"
    private String status;

    // constructor
    public Transmission(File file){
        this.file = file;
        this.finishedLength = 0;
        // update status as long as the transmission is created
        this.setStatus(STATUS_ONGOING);
    }

    @Override
    public void query() {
        // get basic info of this file (trans)
        // filename, status, progress, progress%
        String fileName = file.getName();
        String progress = finishedLength + "/" + file.length();
        double progressPer = ((double) finishedLength / (double) file.length()) * 100;

        // printout the result
        System.out.printf("%-15s %-30s %-20.2f %s \n", status, progress, progressPer, fileName);
    }

    @Override
    public void suspend() {
        // set the status as suspended
        this.setStatus(STATUS_SUSPENDED);
    }

    @Override
    public void resume() {
        // set the status as ongoing
        this.setStatus(STATUS_ONGOING);
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String s) {
        this.status = s;
    }

    public File getFile() {
        return file;
    }

    public long getFinishedLength() {
        return finishedLength;
    }

}
