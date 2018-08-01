package gui;


import lejos.nxt.Motor;

public class Dispenser {

    private int invTen;
    private int invTwenty;
    private int invFifty;

    public int getInvTen() {

        return invTen;
    }


    public int getInvTwenty() {
        return invTwenty;
    }

    public int getInvFifty() {
        return invFifty;
    }
    public static void main(String [] args){
    	Dispenser x = new Dispenser();
    	x.dispenseMoney(10, 10, 10);
    }
    public void dispenseMoney(int _invTen, int _invTwenty, int _invFifty) {

        for (int i = 0; i < _invTen; i++) {
            Motor.A.setSpeed(900);
            Motor.A.rotate(-480);
        	if(i == 9){
               Motor.A.setSpeed(900);
               Motor.A.rotate(1000);
        	}
        	else{
               Motor.A.setSpeed(900);
               Motor.A.rotate(700);
        	}
            invTen--;
        }
        for (int i = 0; i < _invTwenty; i++) {
        	 Motor.B.setSpeed(900);
             Motor.B.rotate(-480);
         	if(i == 9){
                Motor.B.setSpeed(900);
                Motor.B.rotate(1000);
         	}
         	else{
                Motor.B.setSpeed(900);
                Motor.B.rotate(700);
         	}
            invTwenty--;
        }
        for (int i = 0; i < _invFifty; i++) {
       	 	Motor.C.setSpeed(900);
       	 	Motor.C.rotate(-480);
       	 	if(i == 9){
       	 		Motor.C.setSpeed(900);
       	 		Motor.C.rotate(1000);
       	 	}
       	 	else{
       	 		Motor.C.setSpeed(900);
            	Motor.C.rotate(700);
       	 	}
            invFifty--;
        }
    }
    void restockInv(){
        invTen = 10;
        invTwenty = 10;
        invFifty = 10;
    }
    void invTenminus(){
    	invTen--;
    }
    void invTwentyminus(){
    	invTwenty--;
    }
    void invFiftyminus(){
    	invFifty--;
    }
    void reset(int ten, int twenty, int fifty){
    	if(ten != 0){
    		invTen = invTen + ten;
    	}
    	if(twenty != 0){
    		invTwenty = invTwenty + twenty;
    	}
    	if(fifty != 0){
    		invFifty = invFifty + fifty;
    	}
    }
}