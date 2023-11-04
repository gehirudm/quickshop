package quickshop.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import quickshop.dto.CreateSaleDto;
import quickshop.dto.CreateSalesItemDto;
import quickshop.dto.RecieptItem;
import quickshop.dto.ReceiptMetadata;

public class Util {
    public enum RecieptItemProperty {
        ID("ID"),
        NAME("Name"),
        PRICE("Price"),
        AMOUNT("Quantity"),
        TOTAL("Amount");

        public final String headerName;

        private RecieptItemProperty(String headerName) {
            this.headerName = headerName;
        }
    }

    public static String getRecieptItemProperty(RecieptItem item, RecieptItemProperty property) {
        switch (property) {
            case ID:
                return item.id();
            case AMOUNT:
                return String.valueOf(item.amount());
            case NAME:
                return item.name();
            case PRICE:
                return formatToRupees(item.price());
            case TOTAL:
                return formatToRupees(item.price() * (float) item.amount());
            default:
                return "";
        }
    }

    public static Cell getCell(String text, TextAlignment alignment) throws IOException {
        Cell cell = new Cell().add(
                new Paragraph(text)
                        .setFontSize(10)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                        .setFontColor(ColorConstants.BLACK));
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public static Cell getHeaderCell(String text) throws IOException {
        Cell cell = new Cell().add(
                new Paragraph(text)
                        .setFontSize(10)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontColor(ColorConstants.BLACK)
                        .setTextAlignment(TextAlignment.LEFT));

        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
        cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 2));

        return cell;
    }

    public static Cell getBodyCell(String text) throws IOException {
        Cell cell = new Cell().add(
                new Paragraph(text)
                        .setFontSize(10)
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                        .setFontColor(ColorConstants.BLACK)
                        .setTextAlignment(TextAlignment.LEFT));

        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

        return cell;
    }

    public static float calculateRecieptTotal(ReceiptMetadata metadata) {
        float total = 0.0f;
        for (RecieptItem item : metadata.items()) {
            total += item.price() * (float) item.amount();
        }

        return total;
    }

    public static float calculateSaleTotal(CreateSaleDto data) {
        float total = 0.0f;
        for (CreateSalesItemDto item : data.items()) {
            total += item.unitPrice() * (float) item.quantity();
        }

        return total;
    }

    public static float calculateRecieptBalance(ReceiptMetadata metadata) {
        return metadata.cash() - calculateRecieptTotal(metadata);
    }

    public static String formatToRupees(float f) {
        return "Rs. " + new DecimalFormat("###,###.##").format(f);
    }

    public static Optional<?> validateAndConvert(String input, Class<?> targetClass) {
        try {
            if (targetClass == Integer.class) {
                int intValue = Integer.parseInt(input);
                return Optional.of(intValue);
            } else if (targetClass == Float.class) {
                float floatValue = Float.parseFloat(input);
                return Optional.of(floatValue);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String getFileExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
