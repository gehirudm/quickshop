package quickshop.dto;

import java.util.Date;
import java.util.List;

public record CreateSaleDto(
    Date date,
    float total,
    List<CreateSalesItemDto> items
) {}
