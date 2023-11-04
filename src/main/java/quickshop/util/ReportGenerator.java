package quickshop.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import quickshop.dto.ReportMetadata;
import quickshop.entity.Sale;

public class ReportGenerator {
    private String outPath;
    private String headerImageDir;
    private float top = 0f;

    private ReportMetadata metadata;

    private static float HEADER_IMAGE_WIDTH = (PageSize.A4.getWidth() / 100) * 30;
    private static float HEADER_IMAGE_MAX_HEIGHT = (PageSize.A4.getHeight() / 100) * 10;

    public ReportGenerator(String outPath, ReportMetadata metadata) {
        this.outPath = outPath + "/report_"
                + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".pdf";
        this.metadata = metadata;
    }

    public ReportGenerator setHeaderImage(String imgDir) {
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
        addRecordBodyTables(doc);
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
        Paragraph preface = new Paragraph("Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontColor(ColorConstants.RED)
                .setMarginTop(top + 5);

        doc.add(preface);
    }

    private void addRecieptDetails(Document doc) throws IOException {
        Table table = new Table(2).useAllAvailableWidth();

        table.addCell(Util.getCell(
                "Start Date: " + new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Calendar.getInstance().getTime()),
                TextAlignment.CENTER));
        table.addCell(Util.getCell(
                "End Date: " + new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Calendar.getInstance().getTime()),
                TextAlignment.CENTER));

        doc.add(table);
    }

    private void addRecordBodyTables(Document doc) throws IOException {
        for (Map.Entry<String, List<Sale>> entry : groupSalesByDate(metadata.records()).entrySet()) {
            addRecordDayBodyTable(doc, entry.getKey(), entry.getValue());
        }
    }

    private void addRecordDayBodyTable(Document doc, String date, List<Sale> records) throws IOException {
        // Add table header
        Table headerTable = new Table(1).useAllAvailableWidth();

        headerTable.addCell(Util.getCell(
                date + "(Count : " + records.size() + ")",
                TextAlignment.LEFT));

        headerTable.setMarginTop(10f);

        doc.add(headerTable);

        // Add record items table
        float[] columnWidths = { 1, 4, 4, 2, 4 };
        Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

        // Add Header Cells
        table.addHeaderCell(Util.getHeaderCell("No"));
        table.addHeaderCell(Util.getHeaderCell("Time"));
        table.addHeaderCell(Util.getHeaderCell("Order ID"));
        table.addHeaderCell(Util.getHeaderCell("Item Count"));
        table.addHeaderCell(Util.getHeaderCell("Total"));

        // Add Body rows
        for (int i = 0; i < records.size(); i++) {
            var item = records.get(i);

            Cell numCell = new Cell().add(
                    new Paragraph(String.valueOf(i + 1))
                            .setFontSize(10)
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                            .setFontColor(ColorConstants.BLACK)
                            .setTextAlignment(TextAlignment.LEFT));

            numCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            Cell timeCell = new Cell().add(
                    new Paragraph(formatDateTimeToAnalog(item.date()))
                            .setFontSize(10)
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                            .setFontColor(ColorConstants.BLACK)
                            .setTextAlignment(TextAlignment.LEFT));

            timeCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            Cell orderIDCell = new Cell().add(
                    new Paragraph(item.ID())
                            .setFontSize(10)
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                            .setFontColor(ColorConstants.BLACK)
                            .setTextAlignment(TextAlignment.LEFT));

            orderIDCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            Cell itemCountCell = new Cell().add(
                    new Paragraph(String.valueOf(item.items().size()))
                            .setFontSize(10)
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                            .setFontColor(ColorConstants.BLACK)
                            .setTextAlignment(TextAlignment.LEFT));

            itemCountCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            Cell totalCell = new Cell().add(
                    new Paragraph(Util.formatToRupees(item.total()))
                            .setFontSize(10)
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                            .setFontColor(ColorConstants.BLACK)
                            .setTextAlignment(TextAlignment.LEFT));

            totalCell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            table.addCell(numCell);
            table.addCell(timeCell);
            table.addCell(orderIDCell);
            table.addCell(itemCountCell);
            table.addCell(totalCell);
        }

        // Footer elements
        // SUB TOTAL
        table.addCell(new Cell(1, 5).add(new Paragraph("TOTAL")
                .setFontSize(10)
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell()
                .add(new Paragraph(Util.formatToRupees((float) records.stream().mapToDouble(x -> x.total()).sum()))
                        .setFontSize(10)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontColor(ColorConstants.BLACK)
                        .setTextAlignment(TextAlignment.LEFT))
                .setBorder(Border.NO_BORDER));

        doc.add(table);
    }

    private void addRecieptFooter(Document doc) throws IOException {
        Table table = new Table(2).useAllAvailableWidth();

        table.addCell(Util.getCell(
                "Sale count: " + metadata.records().size(),
                TextAlignment.CENTER));
        table.addCell(Util.getCell(
                "Total Sale: " + Util
                        .formatToRupees((float) metadata.records().stream().mapToDouble(item -> item.total()).sum()),
                TextAlignment.CENTER));

        doc.add(table);
    }

    private Map<String, List<Sale>> groupSalesByDate(List<Sale> sales) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return sales.stream().collect(Collectors.groupingBy(sale -> formatter.format(sale.date())));
    }

    private String formatDateTimeToAnalog(Date date) {
        return new SimpleDateFormat("h:mm a").format(date);
    }
}
