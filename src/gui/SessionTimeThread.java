package gui;

/**
 * thread to cancel session after 2 minutes
 */
public class SessionTimeThread extends Thread {
    private Frame frame;
    private long sessionStartTime;
    private long sessionTime;
    private long maxSessionTime;

    public SessionTimeThread(Frame _frame) {
        frame = _frame;
    }

    public void run() {
        while (true) {
            maxSessionTime = 120*1000; //2 minutes
            sessionStartTime = System.currentTimeMillis();
            sessionTime = 0;
            while (frame.isCardIsScannedOnce()) {
                sessionTime = System.currentTimeMillis() - sessionStartTime;
                if (sessionTime > maxSessionTime) { //if session takes longer than 2 minutes
                    frame.stopSession();
                    break;
                }
                if (!frame.isCardIsScannedOnce()) { //if session is canceled by the user
                    System.out.println("Session canceled");
                    break;
                }
            }
        }
    }
}
