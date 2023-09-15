package task;

import interfaces.IConnection;
import interfaces.IProgressControllable;
import interfaces.IQueryable;
import interfaces.IStatus;
import transmission.Transmission;

import java.io.Serializable;
import java.util.List;

/**
 * A task contains multiple transmissions of file
 */
public class Task implements IQueryable, IProgressControllable, IStatus, IConnection, Serializable {

    // task id
    private int id;

    // a list of transmissions (files) in this task
    private List<Transmission> transmissionList;

    // possible status are  "ongoing"， “suspended”, "finished"
    private String status;

    // constructor
    public Task(List<Transmission> transmissionList){
        this.transmissionList = transmissionList;
        // update status as long as the task is created
        this.setStatus(STATUS_ONGOING);
    }

    /**
     * Check whether this task finished,
     * i.e. whether all the trans (files) in this task is finished
     */
    private void updateStatus(){
        boolean isTaskFinished = true;

        // loop through all the trans (files) in this task
        for (Transmission trans : this.transmissionList){

            // if one of the transmission not finished, the task not finished
            if (!trans.getStatus().equals(STATUS_FINISHED)){
                isTaskFinished = false;

                // check if this is suspended or ongoing
                if(trans.getStatus().equals(STATUS_SUSPENDED)){
                    this.setStatus(STATUS_SUSPENDED);
                }else{
                    // this should be ongoing
                    this.setStatus(STATUS_ONGOING);
                }

                // no longer need to check the rest of the trans
                break;
            }
        }

        // check whether the task should be updated
        if (isTaskFinished){
            this.setStatus(STATUS_FINISHED);
        }
    }

    @Override
    public String getStatus() {
        // update the status first
        this.updateStatus();
        return this.status;
    }

    @Override
    public void setStatus(String s) {
        this.status = s;
    }

    @Override
    public void query() {
        // loop through all the trans (files) in this task
        for (Transmission trans : this.transmissionList){
            // query this trans
            trans.query();
        }
    }

    @Override
    public void suspend() {
        // set the status of this task as suspended
        this.setStatus(STATUS_SUSPENDED);
        // suspend all the trans (files) in this task
        for (Transmission trans : this.transmissionList){
            // suspend this trans
            trans.suspend();
        }
    }

    @Override
    public void resume() {
        // set the status of this task as ongoing
        this.setStatus(STATUS_ONGOING);
        // let all the trans (files) in this task be "ongoing" status
        for (Transmission trans : this.transmissionList){
            // resume this trans
            trans.resume();
        }
    }

    public List<Transmission> getTransmissionList() {
        return transmissionList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the number of finished files in this task
     */
    public int getFinishedFileNumber(){
        int count = 0;

        // loop through all the trans (files) in this task
        for (Transmission trans : this.transmissionList){
            if (trans.getStatus().equals(STATUS_FINISHED)){
                count += 1;
            }
        }

        return count;
    }

}
