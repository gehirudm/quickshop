package quickshop.dto;

import java.util.Optional;

import quickshop.services.UsersService.UserType;

public record CreateOrUpdateUserDto(
    String ID,
    String username,
    Optional<String> password,
    UserType type
) {}
