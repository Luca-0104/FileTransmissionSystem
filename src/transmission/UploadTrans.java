package transmission;


import java.io.*;


/**
 * A transmission refers to the transmission of a single file
 * Uploading a single file
 */
public class UploadTrans extends Transmission {

    // constructor
    public UploadTrans(File file) {
        super(file);
    }

    public void startUploadTransmission(BufferedOutputStream bos, boolean isResume){
        // create a BufferedInputStream (read file)
        // this stream is local, not in socket, so we need to close it
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this.getFile()))) {

            // if this trans is resumed from suspending, skip some bytes from beginning
            if (isResume){
                bis.skip(this.finishedLength);
            }

            // the buffer for each time reading
            byte[] buffer = new byte[1024];
            // the length has been read each time
            int readLen = 0;

            // everytime check if the trans is suspended
            while(!this.getStatus().equals(STATUS_SUSPENDED) && ((readLen = bis.read(buffer)) != -1)){

                // for test
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                // write in the new file
                bos.write(buffer, 0, readLen);
                bos.flush();

                // update finished length
                this.finishedLength += readLen;
            }

            // transmission finished,  update status
            // only when coming out from the loop NOT because of the suspending, we can mark this as finished
            if(!this.getStatus().equals(STATUS_SUSPENDED)){
                this.setStatus(STATUS_FINISHED);
            }else{
                // suspended, but, if the file is finished, we still set it set finished
                if(this.finishedLength == this.file.length()){
                    this.setStatus(STATUS_FINISHED);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
