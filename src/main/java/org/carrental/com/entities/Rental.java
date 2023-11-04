package org.carrental.com.entities;

import org.carrental.com.controllers.HibernateController;
import org.carrental.com.records.TotalPriceRental;
import org.carrental.com.utils.ParseDate;

import javax.persistence.*;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicles vehicle;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "total_price")
    private double totalPrice;
    @Column(name = "is_active")
    private boolean isActive;

    public int getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicles getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicles vehicle) {
        this.vehicle = vehicle;
    }

    public void getDataRental() {
        System.out.println("==================================================");
        System.out.println("Código: " + getId());
        System.out.println("Ativo: " + isActive());
        System.out.println("Data de início: " + startDate.toString());
        System.out.println("Data de fim: " + endDate.toString());
        System.out.println("Valor total: " + totalPrice);
        System.out.println("Cliente: \n" + getUser().getDataUser());
        System.out.println("Veículo: \n" + getVehicle().dataVehicle());
        System.out.println("==================================================\n");
    }

    public static void registerRental(User user, Vehicles vehicle, TotalPriceRental total) {
        Rental rental = new Rental();
        rental.setStartDate(total.startDate());
        rental.setEndDate(total.endDate());
        rental.setTotalPrice(total.totalPrice());
        rental.setUser(user);
        rental.setVehicle(vehicle);
        rental.setActive(true);
        new HibernateController().save(rental);
        System.out.println("Aluguel cadastrado com sucesso!");
    }

    public static TotalPriceRental totalPriceRental(Scanner scanner, Vehicles vehicle, Date startDate) throws ParseException {
        if (startDate == null) {
            System.out.println("Data de início (dd/mm/aaaa):");
            startDate = ParseDate.parseDate(scanner.nextLine());
        }
        System.out.println("Data de fim (dd/mm/aaaa):");
        Date endDate = ParseDate.parseDate(scanner.nextLine());
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        String totalPrice = days * vehicle.getPrice() + "";
        return new TotalPriceRental(startDate, endDate, Double.parseDouble(totalPrice), days);
    }

    public static Rental getRentalByVehicle(Vehicles vehicle) {
        return new HibernateController().selectGeneric(Rental.class, vehicle.getId(), "vehicle_id");
    }

    public static Rental getRentalByUser(User user) {
        return new HibernateController().selectRentalUserActive(Rental.class, user.getId(), "user_id");
    }

    public static void returnVehicle(Rental rental) {
        if (rental != null) {
            Vehicles vehicle = rental.getVehicle();
            vehicle.setRented(false);
            User user = rental.getUser();
            user.setCarRental(false);
            rental.setActive(false);
            new HibernateController().update(user);
            new HibernateController().update(vehicle);
            new HibernateController().update(rental);
            System.out.println("Veículo devolvido com sucesso!");
        } else {
            System.out.println("Aluguel não encontrado!");
        }
    }

    public static void returnCalculation(Rental rental) {
        Date startDate = rental.getStartDate();
        Date endDate = rental.getEndDate();
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        String totalPrice = days * rental.getVehicle().getPrice() + "";
        Date dateNow = new Date();
        if (startDate.after(dateNow)) {
            System.out.println("Ainda não é possível calcular o valor total do aluguel!");
            System.out.println("Data de início: " + startDate);
            return;
        }
        if (endDate.before(dateNow)) {
            long overdueDays = (dateNow.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24);
            double penalty = (days * rental.getVehicle().getPrice()) * 0.15 * overdueDays;
            totalPrice = (days * rental.getVehicle().getPrice()) + penalty + "";
            System.out.println("Atraso de " + (dateNow.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24) + " dias");
        }
        rental.setTotalPrice(Double.parseDouble(totalPrice));
        new HibernateController().update(rental);
        System.out.println("Valor total: " + rental.getTotalPrice());
    }

}
