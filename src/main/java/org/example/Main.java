package org.example;

import org.example.controllers.HibernateController;
import org.example.entities.Rental;
import org.example.entities.User;
import org.example.entities.Vehicles;
import org.example.enums.VehicleCategory;
import org.example.records.TotalPriceRental;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws ParseException {
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
                        Vehicles vehicle = registerVehicle(scanner, hibernateController);
                        vehicle.dataVehicle();
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
                        if (user.isCarRental()) {
                            System.out.println("Você já possui um carro alugado!");
                            continue;
                        }
                        while (true) {
                            System.out.println("Escolha uma categoria de veículos:");
                            System.out.println("1 - Cancelar");
                            System.out.println("2 - SEDAN");
                            System.out.println("3 - SUV");
                            System.out.println("4 - HATCH");
                            String category = scanner.nextLine();
                            if (Objects.equals(category, "1")) {
                                break;
                            } else if (Objects.equals(category, "2")) {
                                List<Vehicles> vehicles = hibernateController.getVehicleByCategory(VehicleCategory.valueOf("SEDAN"));
                                vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                                break;
                            } else if (Objects.equals(category, "3")) {
                                List<Vehicles> vehicles = hibernateController.getVehicleByCategory(VehicleCategory.valueOf("SUV"));
                                vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                                break;
                            } else if (Objects.equals(category, "4")) {
                                List<Vehicles> vehicles = hibernateController.getVehicleByCategory(VehicleCategory.valueOf("HATCH"));
                                vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                                break;
                            } else {
                                System.out.println("Opção inválida!");
                            }
                        }
                        System.out.println("Digite o código do carro que deseja alugar:");
                        String id = scanner.nextLine();
                        Vehicles vehicle = hibernateController.selectGeneric(Vehicles.class, Integer.parseInt(id), "id");
                        System.out.println("Carro escolhido:");
                        System.out.println(vehicle.dataVehicle());
                        if (vehicle.isRented()) {
                            System.out.println("Carro já alugado!");
                            Rental rental = hibernateController.selectGeneric(Rental.class, vehicle.getId(), "vehicle_id");
                            System.out.println("O veículo estará disponível a partir do dia " + rental.getEndDate());
                            System.out.println("Deseja agenda-lo para quando estiver disponível? (S/N)");
                        } else {
                            System.out.println("Deseja alugar esse carro? (S/N)");
                        }
                        String answer = scanner.nextLine();
                        if (Objects.equals(answer, "S")) {
                            TotalPriceRental total;
                            if (vehicle.isRented()) {
                                Rental rental = hibernateController.selectGeneric(Rental.class, vehicle.getId(), "vehicle_id");
                                String startDate;
                                while (true) {
                                    System.out.println("Data de aluguel ou S para sair:");
                                    startDate = scanner.nextLine();
                                    if (startDate.equals("S")) {
                                        break;
                                    }
                                    if (parseDate(startDate).before(rental.getEndDate())) {
                                        System.out.println("Data inválida!");
                                        continue;
                                    }
                                    break;
                                }
                                if (startDate.equals("S")) {
                                    continue;
                                }

                                total = totalPriceRental(scanner, vehicle, parseDate(startDate));
                            } else {
                                total = totalPriceRental(scanner, vehicle, null);
                            }
                            System.out.println("O valor total para " + total.days() + " dias é de: " + total.totalPrice() + " R$");
                            System.out.println("Deseja confirmar o aluguel? (S/N)");
                            String confirm = scanner.nextLine();
                            if (Objects.equals(confirm, "S")) {
                                registerRental(hibernateController, user, vehicle, total);
                            } else {
                                continue;
                            }
                            vehicle.setRented(true);
                            hibernateController.update(vehicle);
                            user.setCarRental(true);
                            hibernateController.update(user);
                            System.out.println("Carro alugado com sucesso!");
                        } else {
                            System.out.println("Ok, carro não alugado!");
                        }
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

    public static Vehicles registerVehicle(Scanner scanner, HibernateController hibernateController) {
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
        hibernateController.save(vehicle);
        System.out.println("Veículo cadastrado com sucesso!");
        return vehicle;
    }

    public static TotalPriceRental totalPriceRental(Scanner scanner, Vehicles vehicle, Date startDate) throws ParseException {
        if (startDate == null) {
            System.out.println("Data de início:");
            startDate = parseDate(scanner.nextLine());
        }
        System.out.println("Data de fim:");
        Date endDate = parseDate(scanner.nextLine());
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
        System.out.println("Valor total:");
        String totalPrice = days * vehicle.getPrice() + "";
        System.out.println("Valor total: " + totalPrice);
        return new TotalPriceRental(startDate, endDate, Double.parseDouble(totalPrice), days);
    }

    public static void registerRental(HibernateController hibernateController,
                                      User user,
                                      Vehicles vehicle,
                                      TotalPriceRental total
    ) {
        Rental rental = new Rental();
        rental.setStartDate(total.startDate());
        rental.setEndDate(total.endDate());
        rental.setTotalPrice(total.totalPrice());
        rental.setUser(user);
        rental.setVehicle(vehicle);
        hibernateController.save(rental);
        System.out.println("Aluguel cadastrado com sucesso!");
    }

    public static void updateDataBase() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("car_rental_unit");
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }

    public static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(dateString);
    }
}
