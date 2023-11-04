package quickshop.dto;

import java.util.List;

public record ReceiptMetadata(String cashierName, String reciptNumber, List<RecieptItem> items, float cash) {
    
}
