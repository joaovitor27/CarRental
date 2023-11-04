package org.carrental.com.entities;

import org.carrental.com.controllers.HibernateController;

import javax.persistence.*;
import java.util.Scanner;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "car_rental", nullable = false)
    private boolean carRental;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCarRental() {
        return carRental;
    }

    public void setCarRental(boolean carRental) {
        this.carRental = carRental;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getDataUser() {
        return "Nome: " + this.nome + "\nEmail: " + this.email + "\nAdmin: " + this.isAdmin;
    }

    public static void registerUser(Scanner scanner) {
        System.out.println("Cadastro:");
        System.out.println("Nome:");
        String nome = scanner.nextLine();
        System.out.println("Email:");
        String email = scanner.nextLine();
        System.out.println("Senha:");
        String password = scanner.nextLine();
        User user = new User();
        user.setNome(nome);
        user.setEmail(email);
        user.setPassword(password);
        new HibernateController().save(user);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    public static User getUserLogin(Scanner scanner) {
        System.out.println("Digite o email do usuário:");
        String email = scanner.nextLine();
        System.out.println("Digite a senha do usuário:");
        String password = scanner.nextLine();
        User user = getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Usuário logado com sucesso!");
            return user;
        } else {
            System.out.println("Usuário ou senha incorretos!");
            return null;
        }
    }

    public static User getUserByEmail(String email) {
        return new HibernateController().selectGeneric(User.class, email, "email");
    }

    public static void updateUser(User user) {
        new HibernateController().update(user);
    }

}
