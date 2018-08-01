package NXT;

import lejos.nxt.*;


public class NXTcode {

    private static int rotationA = -240;
    private static int rotationB = -240;
    private static int rotationC = -240;
    private static boolean skipA = true;
    private static boolean skipB = true;
    private static boolean skipC = true;
    private static int correction = 60;

    public static void main(String[] args) {

        printGeld(2,0,0);
    }

    public static void printGeld(int tientjes, int twintigjes, int vijftigjes) {

        for(int i =0;i<tientjes;i++){

            Motor.A.rotate(rotationA);
            Motor.A.rotate(correction);

            if (skipA) {
                rotationA--;
                skipA = false;
            } else {
                skipA = true;
            }
            lejos.util.Delay.msDelay(200);
        }
        for(int i =0;i<twintigjes;i++){

            Motor.B.rotate(rotationB);
            Motor.B.rotate(correction);

            if (skipB) {
                rotationB--;
                skipB = false;
            } else {
                skipB = true;
            }
            lejos.util.Delay.msDelay(200);
        }
        for(int i =0;i<vijftigjes;i++){

            Motor.C.rotate(rotationC);
            Motor.C.rotate(correction);

            if (skipC) {
                rotationC--;
                skipC = false;
            } else {
                skipC = true;
            }
            lejos.util.Delay.msDelay(200);
        }
    }
}