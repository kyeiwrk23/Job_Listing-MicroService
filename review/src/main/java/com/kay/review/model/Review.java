package com.kay.review.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 5, message = "Title must be more than 5 characters")
    private String title;

    @NotBlank(message = "Description can't be blank")
    @Size(min = 5, message = "Description must be more than 5 characters")
    private String description;

    private double rating;

    @NotNull(message = "Company Id can't be Null")
    private Long companyId;
}
