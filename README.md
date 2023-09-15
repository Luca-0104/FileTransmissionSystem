# File Transmission System

## How to Use This System

### How to run this system

To run this system, there are two executable classes in the "starter" package, 
which are "SenderStarter" class and "ReceiverStarter" class.

You should run the "SenderStarter" class first, then do not shut it down, run the
"ReceiverStarter" class.

After that you will see the socket server and client is connected, which means the
sender and receiver is connected.

### Provided Commands

In this system, some commands are provided for sender and receiver.

#### For Sender
1. `send file1, file2, file3, ...`

    Only the sender can send files. You can use command `send` + space + filenames.
    For each of the filename, they should be seperated by ",". Moreover, the name should
    begin from "src/". You can just put the file into the folder "src/testFiles/testSendDir/"
    then copy the "path from the content root" of this file. An example has been given below.

    `send src/testFiles/testSendDir/video1.mp4, src/testFiles/testSendDir/video2.mp4`

    In this example, a task would be created, and in this task, those 4 files would be sent concurrently.  


#### For both Sender and Receiver

1. `tasks`
    
    Both of the sender and receiver can query the task list by using the `tasks` command.
    Then they will be shown with a list of their task, including the task id, task status, 
    file number and finished file number.
    
    There are 3 types of status in total:
   1. "ongoing"
   2. "suspended"
   3. "finished"

2. `query` + `space` + `task id`

   Both of the sender and receiver can query the detailed information of a specific task
   by using this command. For example, `query 1` command will show you the details about task 1,
   includes the status, finished bytes, progress and filename of each file transmission in this task.

3. `suspend` + `space` + `task id`

   Both of the sender and receiver can suspend a task in "onging" status.
    For example, `suspend 1` would suspend the task 1, which means all the file transmissions
    in task 1 would be suspended.

4. `resume` + `space` + `task id`

   Both of the sender and receiver can resume a task in "suspended" status.
   For example, `resume 1` would resume the task 1, which means all the file transmissions
   in task 1 would be resumed.

#### Where to Find the Downloaded files
The downloaded files can be found in the directory of "src/testFiles/testReceiveDir/"

## Archetecture Analysis Based on SOLID Principles
![filetransmissiondoc](https://github.com/Luca-0104/FileTransmissionSystem/assets/61484990/ba2774d7-7c8f-41c3-a1be-8e77ba6c42b7)





