package com.restio.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private List<OrderDTO> items;
    private String status;
    private String comment;
}

