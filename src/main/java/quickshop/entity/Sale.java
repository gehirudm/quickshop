package quickshop.entity;

import java.util.Date;
import java.util.List;

public record Sale(
    String ID,
    Date date,
    float total,
    List<SalesItem> items
) {}
