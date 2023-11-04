package quickshop.dto;

import java.nio.file.Path;
import java.util.List;

import quickshop.entity.Item;
import quickshop.services.LocalizationService.Language;

public record SetupDto(
    String companyName,
    Path logoPath,
    Language language,
    String adminUsername,
    String adminPassword,
    String cashierUsername,
    String cashierPassword,
    List<Item> items
) {}
