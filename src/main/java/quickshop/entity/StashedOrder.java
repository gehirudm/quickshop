package quickshop.entity;

import java.util.List;

import quickshop.dto.CartItemDto;

public record StashedOrder(
    List<CartItemDto> items,
    java.util.Date createdAt
) {}
