package org.example.entities;

import org.example.enums.VehicleCategory;

import javax.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicles {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
    @Column(name = "rented")
    private boolean rented;
    @Column(name = "price")
    private double price;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String dataVehicle(){
        return "==================================================\n" +
                "código: " + this.getId() + "\n" +
                "Placa: " + this.getLicensePlate() + "\n" +
                "Modelo: " + this.getModel() + "\n" +
                "Cor: " + this.getColor() + "\n" +
                "Ano: " + this.getYear() + "\n" +
                "Categoria: " + this.getCategory() + "\n" +
                "Preço: " + this.getPrice() + "\n" +
                "==================================================\n";
    }
}
