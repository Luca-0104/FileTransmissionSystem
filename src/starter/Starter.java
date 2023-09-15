package starter;

import interfaces.INode;

public class Starter {

    /**
     * This method is for start running a node (sender or receiver)
     * @param node The sender or receiver instance
     */
    void startNode(INode node){
        // create and run a sender obj
        node.start();
    }

}
