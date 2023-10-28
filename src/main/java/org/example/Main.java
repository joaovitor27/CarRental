package org.example;

import org.example.controllers.HibernateController;
import org.example.entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("car_rental_unit");
        EntityManager em = emf.createEntityManager();

        em.close();
        emf.close();


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

            if (Objects.equals(option, "3")) {
                System.out.println("Saindo...");
                break;
            } else if (Objects.equals(option, "2")) {
                registerUser(scanner, hibernateController);
            } else if (Objects.equals(option, "1")) {
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
            } else {
                System.out.println("Opção inválida!");
            }

        }
        if (userLogged) {
            System.out.println("Bem vindo " + hibernateController.selectGeneric(User.class, email, "email").getNome());

        }
        hibernateController.closeSessionFactory();
    }

    public static String optionsLogin(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Login");
        System.out.println("2 - Cadastro");
        System.out.println("3 - Sair");
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
}
