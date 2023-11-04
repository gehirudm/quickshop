package quickshop.dto;

import java.util.List;

import quickshop.entity.Sale;

public record ReportMetadata(
    List<Sale> records
) {}
