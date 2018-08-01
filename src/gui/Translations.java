package gui;

/**
 * dialogue for the pages
 */
public class Translations {
    private int balance;
    public void setBalance(int _balance){
        balance = _balance;
    }
    public String translate(int page, boolean languageSelected) {
        String linesOfText [] = new String[14];
        if (languageSelected) {
            linesOfText[0] = "<html><p style='font-size: 20px;'>Scan uw pas aub<br></html>";
            linesOfText[1] = "<html><p style='font-size: 20px;'>Voer uw pin code in</p><p style='font-size: 10px;'>D: vul pin opnieuw in</p></html>";
            linesOfText[2] = "<html><p style='font-size: 20px;'>Wat wilt u doen? <Br> 1: Bekijk uw balans<br>2: Pin geld</p></html>";
            linesOfText[3] = "<html><p style='font-size: 20px;'>Wilt u een bon?<br>1: Ja </p><p style='font-size: 20px;'>2: Nee</p></html>";
            linesOfText[4] = "<html><p style='font-size: 20px;'>Kies het bedrag dat u wilt pinnen<br></p><p style='font-size: 10px;'>Instructies:<br>1: naar boven<br>4: naar beneden<br>3: selecteer biljet<br>D: wis bedrag</p>";
            linesOfText[5] = "<html><p style='font-size: 20px;'>Uw balans :" + balance + "</p></html>";
            linesOfText[6] = "<html><p style='font-size: 15px; color: #e6005c'>A: English<BR> B: Nederlands<BR> C: Afbreken</p></html>";
            linesOfText[7] = "";
            linesOfText[8] = "<html><p style='font-size: 20px;'>Bedankt voor het gebruikmaken van onze pinautomaat!</p></html>";
            linesOfText[9] = "<html><p style='font-size: 20px;'>Wilt u een bon?</p><p style='font-size: 20px; color: #e6005c;'>1: Ja </p><p style='font-size: 20px;'> 2: Nee</p></html>";
            linesOfText[10] = "<html><p style='font-size: 20px;'>Wilt u een bon?<br>1: Ja </p><p style='font-size: 20px; color: #e6005c;'> 2: Nee</p></html>";
            linesOfText[11] = "<html><p style='font-size: 20px;'>TE LAAG SALDO</p></html>";
            linesOfText[12] = "<html><p style='font-size: 20px;'>Pin code ongeldig, probeer opnieuw</p><p style='font-size: 10px;'>D: vul pin opnieuw in</p></html>";
            linesOfText[13] = "<html><p style='font-size: 20px;'>Pas geblokkeerd<br></p>";
        }
        if (!languageSelected) { //translate to english.
            linesOfText[0] = "<html><p style='font-size: 20px;'>Scan your card please</html>";
            linesOfText[1] = "<html><p style='font-size: 20px;'>Enter your pin code please</p><p style='font-size: 10px;'>D: Clear entered pin</p></html>";
            linesOfText[2] = "<html><p style='font-size: 20px;'>What would you like to do?<br>1: Check your balance<br> 2: Pin money</p></html>";
            linesOfText[3] = "<html><p style='font-size: 20px;'>Would you like a receipt?<br>1: Yes </p><p style='font-size: 20px;'>2: No</p></html>";
            linesOfText[4] = "<html><p style='font-size: 20px;'>Choose the amount that you would like to pin<br></p><p style='font-size: 10px;'>Instructies:<br>1: up<br>4: down<br>3: add ammount<br>D: clear amount</p></html>";
            linesOfText[5] = "<html><p style='font-size: 20px;'>Your balance :" + balance + "</p></html>";
            linesOfText[6] = "<html><p style='font-size: 15px; color: #e6005c'>A: English<BR> B: Nederlands<BR> C: Cancel</p></html>";
            linesOfText[7] = "";
            linesOfText[8] = "<html><p style='font-size: 20px;'>Thanks for using our ATM!</p></html>";
            linesOfText[9] = "<html><p style='font-size: 20px;'>Would you like a receipt?</p><p style='font-size: 20px; color: #e6005c;'>1: Yes </p><p style='font-size: 20px;'> 2: No</p></html>";
            linesOfText[10] = "<html><p style='font-size: 20px;'>Would you like a receipt?<br>1: Yes </p><p style='font-size: 20px; color: #e6005c;'> 2: No</p></html>";
            linesOfText[11] = "<html><p style='font-size: 20px;'>NOT ENOUGH BALANCE</p></html>";
            linesOfText[12] = "<html><p style='font-size: 20px;'>Pin code invalid, try again</p><p style='font-size: 10px;'>D: Clear entered pin</p></html>";
            linesOfText[13] = "<html><p style='font-size: 20px;'>Account blocked</p>";
        }
        return linesOfText[page];
    }

    public String welcomeScreen(String name, boolean languageSelected) {
        String welcome = "";
        if (languageSelected) {
            welcome = "<html><p style='font-size: 20px;'>Welkom " + name + ", wat wilt u doen?<br>1: Bekijk balans<br> 2: Pin geld</p></html>";
        }
        if (!languageSelected) {
            welcome = "<html><p style='font-size: 20px;'>Welcome " + name + ", what would you like to do?<br>1: Check your balance<br> 2: Pin money</p></html>";
        }
        return welcome;
    }
    public String moneyScreen(String money, boolean languageSelected){
        String moneylist = "";
        if (languageSelected) {
            moneylist = "<html><p style='font-size: 20px;'>Totaalbedrag: " + money + "</p></html>";
        }
        if (!languageSelected) {
            moneylist = "<html><p style='font-size: 20px;'>Total amount: " + money + "</p></html>";
        }
        return moneylist;
    }
}
