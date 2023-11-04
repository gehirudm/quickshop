package quickshop.dto;

import quickshop.entity.Item;

public record CartItemDto(
    Item item,
    int quantity
) {}
