package com.restio.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private Set<String> roles;          // Изменено с String на Set<String>
    private boolean active = true;
}