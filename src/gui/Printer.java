package gui;

import java.awt.*;
import java.awt.print.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
 * Takes care of the printer
 */
public class Printer
{
    private int balance;
    private int moneyPinned;
    private String name;
    private String date;
    private boolean languageSelected;
    /**
     * This method creates a preview.
     */
    public void preview()
    {
        JFrame frame = new JFrame();

        frame.setTitle("Preview for label");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.add(new PrintPreview());
    }
    /**
     * This method prints the receipt.
     * @param noDialog
     */
    public void print(boolean noDialog)
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat page = new PageFormat();
        Paper paper = new Paper();

        paper.setImageableArea(0, 0, 160, 290);
        paper.setSize(160, 290);

        page.setPaper(paper);

        job.setPrintable(new OutPrintable(), page);

        try
        {
            if (noDialog)
                job.print();
            else
            {
                if (job.printDialog())
                    job.print();
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while printing: " + e.getMessage());
        }
    }
    /**
     * This method functions as a constructor for the receipt.
     * @param money
     * @param name
     * @param date
     * @param languageSelected
     */
    public  void parseInfoToPrint(int money,int balance ,String name, String date, boolean languageSelected){
        moneyPinned = money;
        this.balance = balance;
        this.name = name;
        this.date = date;
        this.languageSelected = languageSelected;

    }
    /**
     * This method creates the layout for the receipt.
     * @param graphics
     */
    public void createLabel(Graphics2D graphics)
    {
        if(languageSelected) {
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, 160, 280);

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Image img = new ImageIcon("/Users/Kirty/IdeaProjects/PrinterDemo/src/ff.png").getImage();
            graphics.drawImage(img, 10, 30, 90, 90, null);

            graphics.setColor(Color.black);

            graphics.setFont(new Font("Roboto", Font.BOLD, 18));
            graphics.drawString("The Pink Banker", 0, 34);

            graphics.setFont(new Font("Roboto", Font.PLAIN, 14));

            graphics.drawLine(5, 60, 150, 60);

            graphics.setFont(new Font("Roboto", Font.BOLD, 10));
            graphics.drawString(name , 5, 80);
            graphics.setFont(new Font("Roboto", Font.BOLD, 8));
            graphics.drawString("Bedankt voor het gebruiken van de ATM", 5, 100);
            graphics.drawString("Uw gepinde bedrag: " + moneyPinned, 5, 120);
            graphics.drawString("Balans: " + (balance - moneyPinned), 5, 140);
            graphics.drawString("Datum:" + date, 5, 230);

            graphics.drawLine(5, 240, 150, 240);

            graphics.setFont(new Font("Monospace", Font.PLAIN, 10));
            graphics.drawString("Wij hopen u snel terug te zien", 5, 255);
            graphics.setFont(new Font("Monospace", Font.PLAIN, 5));
            graphics.drawString("The Pink Banker 2017", 10, 270);
        }
        if(!languageSelected){
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, 160, 280);

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Image img = new ImageIcon("/Users/Kirty/IdeaProjects/PrinterDemo/src/ff.png").getImage();
            graphics.drawImage(img, 10, 30, 90, 90, null);

            graphics.setColor(Color.black);

            graphics.setFont(new Font("Roboto", Font.BOLD, 18));
            graphics.drawString("The Pink Banker", 0, 34);

            graphics.setFont(new Font("Roboto", Font.PLAIN, 14));

            graphics.drawLine(5, 60, 150, 60);

            graphics.setFont(new Font("Roboto", Font.BOLD, 10));
            graphics.drawString(name, 5, 80);
            graphics.setFont(new Font("Roboto", Font.BOLD, 8));
            graphics.drawString("Thank you for using our ATM", 5, 100);
            graphics.drawString("Pinned amount: " + moneyPinned, 5, 120);
            graphics.drawString("Balance: " + (balance - moneyPinned), 5, 140);
            graphics.drawString("Date:" + date, 5, 230);

            graphics.drawLine(5, 240, 150, 240);

            graphics.setFont(new Font("Monospace", Font.PLAIN, 10));
            graphics.drawString("We hope to see you again!", 5, 255);
            graphics.setFont(new Font("Monospace", Font.PLAIN, 5));
            graphics.drawString("The Pink Banker 2017", 10, 270);
        }
    }
    class OutPrintable implements Printable
    {
        public int print(Graphics g, PageFormat pf, int pageIndex)
        {
            if (pageIndex != 0)
                return NO_SUCH_PAGE;

            Graphics2D graphics = (Graphics2D)g;

            createLabel(graphics);

            return PAGE_EXISTS;
        }
    }

    class PrintPreview extends JPanel
    {
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            Graphics2D graphics = (Graphics2D)g;

            createLabel(graphics);
        }
    }
}
