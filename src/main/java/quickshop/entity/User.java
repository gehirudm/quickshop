package quickshop.entity;

import quickshop.services.UsersService.UserType;

public record User(
    String ID,
    String username,
    UserType type
) {}
