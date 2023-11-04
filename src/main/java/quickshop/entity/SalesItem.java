package quickshop.entity;

public record SalesItem(
    String ID,
    String name,
    float unitPrice,
    int amount
) {}
