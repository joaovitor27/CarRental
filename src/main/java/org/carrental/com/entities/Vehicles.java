package org.carrental.com.entities;

import org.carrental.com.controllers.HibernateController;
import org.carrental.com.enums.VehicleCategory;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

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

    public String dataVehicle() {
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

    public static List<Vehicles> getVehicleRented() {
        return new HibernateController().selectAllGeneric(Vehicles.class, true, "rented");
    }

    public static List<Vehicles> getVehicleNotRented() {
        return new HibernateController().selectAllGeneric(Vehicles.class, false, "rented");
    }

    public static List<Vehicles> getVehicleByCategory(VehicleCategory category) {
        return new HibernateController().selectAllGeneric(Vehicles.class, category, "category");
    }

    public static void updateVehicle(Vehicles vehicle) {
        new HibernateController().update(vehicle);
    }

    public static Vehicles registerVehicle(Scanner scanner) {
        System.out.println("Cadastro de veículos:");
        System.out.println("Modelo:");
        String model = scanner.nextLine();
        System.out.println("Ano:");
        String year = scanner.nextLine();
        System.out.println("Placa:");
        String licensePlate = scanner.nextLine();
        System.out.println("Cor:");
        String color = scanner.nextLine();
        System.out.println("Valor da diária:");
        String price = scanner.nextLine();
        System.out.println("Categoria:");
        String category = scanner.nextLine();
        Vehicles vehicle = new Vehicles();
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setPrice(Double.parseDouble(price));
        vehicle.setColor(color);
        vehicle.setCategory(VehicleCategory.valueOf(category));
        new HibernateController().save(vehicle);
        System.out.println("Veículo cadastrado com sucesso!");
        return vehicle;
    }

}
