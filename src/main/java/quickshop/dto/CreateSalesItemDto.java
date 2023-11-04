package quickshop.dto;

public record CreateSalesItemDto(
    String ID,
    float unitPrice,
    int quantity
) {}
