package quickshop.entity;

import java.nio.file.Path;

public record Item(
    String ID,
    String name,
    float price,
    int stock,
    Path imagePath
) {}
