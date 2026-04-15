package com.sprint.Book_Partner_Application.sales.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesId implements java.io.Serializable {
    private String storId;
    private String ordNum;
    private String titleId;
}
