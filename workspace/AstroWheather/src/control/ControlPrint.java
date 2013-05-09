package control;

import entity.Forecast;
import entity.Scale;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

/**
 *
 * @author osfprieto
 */
public class ControlPrint {

    /**
     * The main method tests the printing options of this controller
     *
     * @param args unused
     */
    public static void main(String[] args) {
        //Calendar calendar = new GregorianCalendar();
        //System.out.println(PrintableObject.format(calendar));

        List<Forecast> forecasts = new ArrayList<Forecast>();

        for (int i = 0; i < 10; i++) {
            forecasts.add(new Forecast(new Date(), 25, Scale.CELSIUS));
        }

        Image image = new ImageIcon("test.png").getImage();

        print(20, forecasts, image);

    }

    /**
     * Gets a PrinterJob instance and sets the new printable object to be
     * printed
     *
     * @param dataDispersion The double indicating the data dispersion of the
     * forecasts
     * @param forecasts The list containing the weather forecasts
     * @param image The image to be printed with the grpahics of the weather
     * forecasts
     */
    public static void print(double dataDispersion, List<Forecast> forecasts, Image image) {
        PrintableObject po = new PrintableObject(dataDispersion, forecasts, image);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(po);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                System.out.println("Error while printing forecast.");
            }
        } else {
            System.out.println("Forecast printing has been cancelled.");
        }
    }

    /**
     * Class that implements the Printable interace
     */
    public static class PrintableObject implements Printable {

        private static final Font FUENTE_SUBTITULO = new Font("Times New Roman", Font.BOLD, 16);
        private static final Font FUENTE_CUERPO = new Font("Times New Roman", Font.PLAIN, 12);
        private double dataDispersion;
        private List<Forecast> forecasts;
        private Image image;

        /**
         * Constructor
         *
         * @param dataDispersion
         * @param forecasts
         * @param image
         */
        public PrintableObject(double dataDispersion, List<Forecast> forecasts,
                Image image) {
            this.dataDispersion = dataDispersion;
            this.forecasts = forecasts;
            this.image = image;
        }

        /**
         * Printing method
         *
         * @param g Graphics object to be used for painting on the sheet
         * @param pageFormat PageFormat object conaining the data of the sheet
         * to print on
         * @param pageNum The number of the sheet to print on
         */
        public int print(Graphics g, PageFormat pageFormat, int pageNum)
                throws PrinterException {

            /*The report prints just one sheet*/

            if (pageNum == 0) {

                int width = (int) pageFormat.getPaper().getWidth();
                int height = (int) pageFormat.getPaper().getHeight();

                pageFormat.getPaper().setImageableArea(0, 0,
                        width, height);

                g.setFont(FUENTE_SUBTITULO);

                /* 
                 * Draws the titles of the report and puts the image on the report
                 */

                g.drawString("Forecast trends", 100, 100);

                g.drawImage(image, 100, 120, width - 200 > 0 ? width - 200 : 100, 300, null);

                g.drawString("Data dispersion: ", 100, 430);

                g.drawString("Date (DD/MM/YYYY)", width / 4 + -25, 470);
                g.drawString("Forecast", 3 * width / 4 + -25, 470);

                g.setFont(FUENTE_CUERPO);

                g.drawString("" + dataDispersion, 250, 430);

                /* Puts each forecast to be reported on the sheet moving forward*/

                Iterator<Forecast> iterator = forecasts.iterator();
                int y = 490;
                while (iterator.hasNext()) {
                    Forecast forecast = iterator.next();

                    g.drawString(forecast.getDateWithFormat(),
                            150, y);
                    g.drawString(forecast.getForecast() + " "
                            + forecast.getScale().toString(),
                            3 * width / 4, y);

                    y += 15;
                }

                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }
    }
}

