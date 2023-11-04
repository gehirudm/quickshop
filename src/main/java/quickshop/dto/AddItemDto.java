package quickshop.dto;

import java.nio.file.Path;

public record AddItemDto(
    String name,
    Float unitPrice,
    int stock,
    Path imagePath
) {}
