package com.example.FarmStock.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String message, String path, LocalDateTime timestamp) {
}
