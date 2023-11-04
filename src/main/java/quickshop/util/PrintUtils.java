package quickshop.util;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class PrintUtils {
    public static void printPdf(String pdfPath) throws PrinterException, IOException {
        PDDocument document = PDDocument.load(new File(pdfPath));
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = pageFormat.getPaper();

        // Set the imageable area of this Paper
        paper.setImageableArea(0, 0, pageFormat.getWidth(), pageFormat.getHeight());

        // Set the Paper object for this PageFormat
        pageFormat.setPaper(paper);

        // Set the copies
        printerJob.setCopies(1);

        // Call painter to render the pages in the specified format
        printerJob.setPageable(new PDFPageable(document));

        // Display the print dialog
        if (printerJob.printDialog()) {
            printerJob.print();
        }
    }
}
