package org.example.entities;

import org.example.enums.VehicleCategory;

import javax.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicles {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "license_plate")
    private String licensePlate;
    @Column(name = "model")
    private String model;
    @Column(name = "color")
    private String color;
    @Column(name = "year")
    private String year;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private VehicleCategory category;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
