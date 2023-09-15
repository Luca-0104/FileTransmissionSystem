package transmission;

import java.io.*;


/**
 * A transmission refers to the transmission of a single file
 * Downloading a single file
 */
public class DownloadTrans extends Transmission {

    // The length of the file that should be downloaded
    long fileLength;

    // constructor
    public DownloadTrans(File file, long fileLength) {
        super(file);
        this.fileLength = fileLength;
    }

    /**
     * We should override the query in downloadTrans,
     * because the file length should be gotten from the corresponding parameter,
     * rather than from the file.length()
     */
    @Override
    public void query() {
        // get basic info of this file (trans)
        // filename, status, progress, progress%
        String fileName = file.getName();
        String progress = finishedLength + "/" + fileLength;
        double progressPer = ((double) finishedLength / (double) fileLength) * 100;

        // printout the result
        System.out.printf("%-15s %-30s %-20.2f %s \n", this.getStatus(), progress, progressPer, fileName);
    }

    public void startDownloadTransmission(BufferedInputStream bis){
        // create a BufferedOutputStream (write file)
        // NOTICE: this stream is local, not in socket! so we need to close it here
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(this.file, true))) {

            // the buffer for each time reading
            byte[] buffer = new byte[1024];
            // the length has been read each time
            int readLen = 0;

            while(((readLen = bis.read(buffer)) != -1)){

                // for test
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                // write in the new file
                bos.write(buffer, 0, readLen);

                // update finished length
                this.finishedLength += readLen;
            }

            // important! check if the trans is finished or suspended
            // No matter who (sender, or receiver) use the command "suspend", the stream will always be broken by the sender
            // so the receiver stream will also finish as a result. But, the finishedLength will less than the total file length.
            if(finishedLength < fileLength){
                // this should be suspended
                this.setStatus(STATUS_SUSPENDED);
            }else{
                // transmission finished,  update status
                this.setStatus(STATUS_FINISHED);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
