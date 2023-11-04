package quickshop.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import quickshop.dto.ReceiptMetadata;
import quickshop.util.Util.RecieptItemProperty;

public class ReceiptGenerator {
    private String outPath;
    private String headerImageDir;
    private float top = 0f;

    private ReceiptMetadata metadata;

    private static float HEADER_IMAGE_WIDTH = (PageSize.A4.getWidth() / 100) * 30;
    private static float HEADER_IMAGE_MAX_HEIGHT = (PageSize.A4.getHeight() / 100) * 10;

    public ReceiptGenerator(String outPath, ReceiptMetadata metadata) {
        this.outPath = outPath + "/receipt_"
                + metadata.reciptNumber() + ".pdf";
        this.metadata = metadata;
    }

    public ReceiptGenerator setHeaderImage(String imgDir) {
        this.headerImageDir = imgDir;
        return this;
    }

    public String generate() throws IOException {
        File file = new File(outPath);
        file.getParentFile().mkdirs();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outPath));
        Document doc = new Document(pdfDoc, PageSize.A4);
        doc.setMargins(10, 4, 4, 10);

        addHeaderImage(doc);
        addHeaderText(doc);
        addRecieptDetails(doc);
        addRecieptBodyTable(doc);
        addRecieptFooter(doc);

        doc.close();

        return outPath;
    }

    private void addHeaderImage(Document doc) throws MalformedURLException {
        if (headerImageDir == null)
            return;

        // Creating an ImageData object
        ImageData data = ImageDataFactory.create(headerImageDir);

        // Creating an Image object
        Image image = new Image(data);
        image.scaleToFit(HEADER_IMAGE_WIDTH, HEADER_IMAGE_MAX_HEIGHT);
        float x = (PageSize.A4.getWidth() - image.getImageScaledWidth()) / 2;
        float y = PageSize.A4.getHeight() - (image.getImageScaledHeight() + 10);
        image.setFixedPosition(x, y);

        doc.add(image);

        top = image.getImageScaledHeight();
    }

    private void addHeaderText(Document doc) throws IOException {
        Paragraph preface = new Paragraph("Reciept")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontColor(ColorConstants.RED)
                .setMarginTop(top + 5);

        doc.add(preface);
    }

    private void addRecieptDetails(Document doc) throws IOException {
        Table table = new Table(3).useAllAvailableWidth();
        table.addCell(Util.getCell("No: " + metadata.reciptNumber(), TextAlignment.CENTER));
        table.addCell(Util.getCell("Cashier:  " + metadata.cashierName(), TextAlignment.CENTER));
        table.addCell(Util.getCell(
                "Date: " + new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Calendar.getInstance().getTime()),
                TextAlignment.CENTER));

        doc.add(table);
    }

    private void addRecieptBodyTable(Document doc) throws IOException {
        float[] columnWidths = { 1, 1, 4, 2, 2, 2 };
        Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

        // Add Header Cells
        table.addHeaderCell(Util.getHeaderCell("No"));

        for (RecieptItemProperty property : RecieptItemProperty.values()) {
            table.addHeaderCell(Util.getHeaderCell(property.headerName));
        }

        // Add Reciept Item cells
        for (int i = 0; i < metadata.items().size(); i++) {
            table.addCell(Util.getBodyCell(String.valueOf(i + 1)));

            for (RecieptItemProperty property : RecieptItemProperty.values()) {
                table.addCell(Util.getBodyCell(Util.getRecieptItemProperty(metadata.items().get(i), property)));
            }
        }

        // Footer Cells

        // SUB TOTAL
        table.addCell(new Cell(1, 5).add(new Paragraph("SUB TOTAL")
                .setFontSize(10)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph(Util.formatToRupees(Util.calculateRecieptTotal(metadata)))
                .setFontSize(10)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));

        // CASH
        table.addCell(new Cell(1, 5).add(new Paragraph("CASH")
                .setFontSize(10)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph(Util.formatToRupees(metadata.cash()))
                .setFontSize(10)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));

        // BALANCE
        table.addCell(new Cell(1, 5).add(new Paragraph("BALANCE")
                .setFontSize(10)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph(Util.formatToRupees(Util.calculateRecieptBalance(metadata)))
                .setFontSize(10)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.LEFT)).setBorder(Border.NO_BORDER));

        doc.add(table);
    }

    private void addRecieptFooter(Document doc) throws IOException {
        Paragraph preface = new Paragraph("Thank you! Come back again.")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(14)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontColor(ColorConstants.BLACK)
                .setMargin(5);

        doc.add(preface);
    }

}
