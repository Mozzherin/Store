package org.example.entityes;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "Категория не должена быть пустой")
    @Size(min=2, max=15, message = "Категория должена содержать от 2-х до 15-ти символов")
    @Column(name = "category")
    private String category;
    @NotEmpty(message = "Производитель не должен быть пустым")
    @Size(min=2, max=15, message = "Производитель должен содержать от 2-х до 15-ти символов")
    @Column(name = "manufacturer")
    private String manufacturer;
    @NotEmpty(message = "Модель не должена быть пустой")
    @Size(min=2, max=15, message = "Модель должена содержать от 2-х до 15-ти символов")
    @Column(name = "model")
    private String model;
    @NotNull(message = "Стоимость не должена быть пустой")
    @Min(100)
    @Column(name = "price")
    private double price;
    @Column(name = "filename")
    private String filename;
}
