package org.example;

import org.example.controllers.HibernateController;
import org.example.entities.User;
import org.example.entities.Vehicles;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        updateDataBase(); // This method is used to update the database

        HibernateController hibernateController = new HibernateController();
        String email = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seja Bem Vindo a melhor locadora de carro do mundo!");
        System.out.println("Aqui você encontra os melhores carros para alugar.");
        System.out.println("Aqui você encontra os melhores preços.");
        System.out.println("Aqui você encontra os melhores serviços.");
        System.out.println("===============================================");
        System.out.println("|              Locadora de Carros             |");
        System.out.println("===============================================");

        System.out.println("Faça seu login para continuar:");
        boolean userLogged = false;
        while (!userLogged) {

            String option = optionsLogin(scanner);

            if (Objects.equals(option, "1")) {
                System.out.println("Saindo...");
                break;

            } else if (Objects.equals(option, "2")) {
                System.out.println("Email:");
                email = scanner.nextLine();
                System.out.println("Senha:");
                String password = scanner.nextLine();
                if (hibernateController.selectGeneric(User.class, email, "email") != null) {
                    if (hibernateController.selectGeneric(User.class, email, "email").getPassword().equals(password)) {
                        System.out.println("Login realizado com sucesso!");
                        userLogged = true;
                    } else {
                        System.out.println("Senha incorreta!");
                    }
                } else {
                    System.out.println("Email não cadastrado!");
                }

            } else if (Objects.equals(option, "3")) {
                registerUser(scanner, hibernateController);

            } else {
                System.out.println("Opção inválida!");
            }

        }
        if (userLogged) {
            User user = hibernateController.selectGeneric(User.class, email, "email");
            System.out.println("Bem vindo " + user.getNome());
            if (user.isAdmin()) {
                while (true) {
                    String option = optionsUserAdmin(scanner);
                    if (Objects.equals(option, "1")) {
                        System.out.println("Saindo...");
                        break;
                    } else if (Objects.equals(option, "2")) {
                        List<Vehicles> vehicles = hibernateController.getVehicleRented();
                        if (vehicles.isEmpty()) {
                            System.out.println("===============================");
                            System.out.println("Nenhum carro foi alugado ainda!");
                            System.out.println("===============================");
                        } else {
                            vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                        }
                    } else if (Objects.equals(option, "3")) {

                        List<Vehicles> vehicles = hibernateController.getVehicleNotRented();
                        if (vehicles.isEmpty()) {
                            System.out.println("===================================");
                            System.out.println(" Todos os carros já foram alugados!");
                            System.out.println("===================================");
                        } else {
                            vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                        }
                        vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                    } else if (Objects.equals(option, "4")) {
                        System.out.println("Cadastrar um carro");
                    } else {
                        System.out.println("Opção inválida!");
                    }
                }
            } else {
                while (true) {
                    String option = optionsUser(scanner);
                    if (Objects.equals(option, "1")) {
                        System.out.println("Saindo...");
                        break;
                    } else if (Objects.equals(option, "2")) {
                        System.out.println("Alugar um carro");
                    } else if (Objects.equals(option, "3")) {
                        System.out.println("Devolver um carro");
                    } else if (Objects.equals(option, "4")) {
                        List<Vehicles> vehicles = hibernateController.getVehicleRented();
                        vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                    } else if (Objects.equals(option, "5")) {
                        List<Vehicles> vehicles = hibernateController.getVehicleNotRented();
                        vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                    } else {
                        System.out.println("Opção inválida!");
                    }
                }
            }
        }
        hibernateController.closeSessionFactory();
    }

    public static String optionsLogin(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Sair");
        System.out.println("2 - Login");
        System.out.println("3 - Cadastro");
        return scanner.nextLine();
    }

    public static String optionsUser(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Sair");
        System.out.println("2 - Alugar um carro");
        System.out.println("3 - Devolver um carro");
        System.out.println("4 - Lista de carros alugados");
        System.out.println("5 - Lista de carros não alugados");
        return scanner.nextLine();
    }

    public static String optionsUserAdmin(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Sair");
        System.out.println("2 - Lista de carros alugados");
        System.out.println("3 - Lista de carros não alugados");
        System.out.println("4 - Cadastrar um carro");
        return scanner.nextLine();
    }

    public static void registerUser(Scanner scanner, HibernateController hibernateController) {
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
        hibernateController.save(user);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    public static void updateDataBase() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("car_rental_unit");
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }
}
