package org.carrental.com;

import org.carrental.com.entities.Rental;
import org.carrental.com.entities.User;
import org.carrental.com.controllers.HibernateController;
import org.carrental.com.entities.Vehicles;
import org.carrental.com.enums.VehicleCategory;
import org.carrental.com.records.TotalPriceRental;
import org.carrental.com.utils.ParseDate;

import java.text.ParseException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws ParseException {

        HibernateController hibernateController = new HibernateController(true);
        String email = "";
        Scanner scanner = new Scanner(System.in);

        greeting();
        System.out.println("Faça seu login para continuar:");
        boolean userLogged = false;
        while (!userLogged) {

            String option = optionsLogin(scanner);

            if (Objects.equals(option, "1")) {
                System.out.println("Saindo...");
                break;

            } else if (Objects.equals(option, "2")) {
                User user = User.getUserLogin(scanner);
                if (user == null) {
                    System.out.println("Usuário não encontrado!");
                    continue;
                }
                email = user.getEmail();
                userLogged = true;

            } else if (Objects.equals(option, "3")) {
                User.registerUser(scanner);

            } else {
                System.out.println("Opção inválida!");
            }

        }
        if (userLogged) {
            User user = User.getUserByEmail(email);
            System.out.println("Bem vindo " + user.getNome());
            if (user.isAdmin()) {
                while (true) {
                    String option = optionsUserAdmin(scanner);
                    if (Objects.equals(option, "1")) {
                        System.out.println("Saindo...");
                        break;
                    } else if (Objects.equals(option, "2")) {
                        List<Vehicles> vehicles = Vehicles.getVehicleRented();
                        if (vehicles.isEmpty()) {
                            System.out.println("===============================");
                            System.out.println("Nenhum carro foi alugado ainda!");
                            System.out.println("===============================");
                        } else {
                            vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                        }
                    } else if (Objects.equals(option, "3")) {
                        List<Vehicles> vehicles = Vehicles.getVehicleNotRented();
                        if (vehicles.isEmpty()) {
                            System.out.println("===================================");
                            System.out.println(" Todos os carros já foram alugados!");
                            System.out.println("===================================");
                        } else {
                            vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                        }
                        vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                    } else if (Objects.equals(option, "4")) {
                        Vehicles vehicle = Vehicles.registerVehicle(scanner);
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
                        user = User.getUserByEmail(email);
                        if (user.isCarRental()) {
                            System.out.println("Você já possui um carro alugado!");
                            continue;
                        }
                        Vehicles vehicleSelect = new Vehicles();
                        while (true) {
                            String category = optionsCategory(scanner);
                            if (Objects.equals(category, "1")) {
                                break;
                            } else if (Objects.equals(category, "2") || Objects.equals(category, "3") || Objects.equals(category, "4")) {
                                String categorySelected = getCategorySelected(category);
                                List<Vehicles> vehicles = Vehicles.getVehicleByCategory(VehicleCategory.valueOf(categorySelected));
                                if (vehicles.isEmpty()) {
                                    categoryEmpty(categorySelected);
                                    continue;
                                }else {
                                    while (true) {
                                        vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                                        System.out.println("Digite o código do carro que deseja alugar:");
                                        String id = scanner.nextLine();
                                        vehicleSelect = vehicles.stream().filter(v -> v.getId() == Integer.parseInt(id)).findFirst().orElse(null);
                                        if (vehicleSelect == null) {
                                            System.out.println("Carro não encontrado!");
                                            continue;
                                        }
                                        System.out.println("Carro selecionado:");
                                        System.out.println(vehicleSelect.dataVehicle());
                                        break;
                                    }
                                }
                                break;
                            } else {
                                System.out.println("Opção inválida!");
                            }
                        }
                        if (vehicleSelect.getId() == 0) {
                            continue;
                        }
                        if (vehicleSelect.isRented()) {
                            System.out.println("Carro já alugado!");
                            Rental rental = Rental.getRentalByVehicle(vehicleSelect);
                            System.out.println("O veículo estará disponível a partir do dia " + rental.getEndDate());
                            System.out.println("Deseja agenda-lo para quando estiver disponível? (S/N)");
                        } else {
                            System.out.println("Deseja alugar esse carro? (S/N)");
                        }
                        String answer = scanner.nextLine();
                        if (Objects.equals(answer, "S")) {
                            TotalPriceRental total;
                            if (vehicleSelect.isRented()) {
                                Rental rental = Rental.getRentalByVehicle(vehicleSelect);
                                String startDate;
                                while (true) {
                                    System.out.println("Data de aluguel ou S para sair:");
                                    startDate = scanner.nextLine();
                                    if (startDate.equals("S")) {
                                        break;
                                    }
                                    if (ParseDate.parseDate(startDate).before(rental.getEndDate())) {
                                        System.out.println("Data inválida!");
                                        continue;
                                    }
                                    break;
                                }
                                if (startDate.equals("S")) {
                                    continue;
                                }

                                total = Rental.totalPriceRental(scanner, vehicleSelect, ParseDate.parseDate(startDate));
                            } else {
                                total = Rental.totalPriceRental(scanner, vehicleSelect, null);
                            }
                            System.out.println("O valor total para " + total.days() + " dias é de: " + total.totalPrice() + " R$");
                            System.out.println("Deseja confirmar o aluguel? (S/N)");
                            String confirm = scanner.nextLine();
                            if (Objects.equals(confirm, "S")) {
                                Rental.registerRental(user, vehicleSelect, total);
                            } else {
                                continue;
                            }
                            vehicleSelect.setRented(true);
                            user.setCarRental(true);
                            Vehicles.updateVehicle(vehicleSelect);
                            User.updateUser(user);
                            System.out.println("Carro alugado com sucesso!");
                        } else {
                            System.out.println("Ok, carro não alugado!");
                        }
                    } else if (Objects.equals(option, "3")) {
                        if (!user.isCarRental()) {
                            System.out.println("Você não possui nenhum carro alugado!");
                            continue;
                        }
                        Rental rental = Rental.getRentalByUser(user);
                        Rental.returnCalculation(rental);
                        System.out.println("Deseja confirmar a devolução? (S/N)");
                        String confirm = scanner.nextLine();
                        if (Objects.equals(confirm, "S")) {
                            Rental.returnVehicle(rental);
                        } else {
                            System.out.println("Ok, carro não devolvido!");
                        }
                    } else if (Objects.equals(option, "4")) {
                        List<Vehicles> vehicles = Vehicles.getVehicleRented();
                        if (vehicles.isEmpty()) {
                            System.out.println("Nenhum carro foi alugado!");
                            continue;
                        }
                        vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                    } else if (Objects.equals(option, "5")) {
                        List<Vehicles> vehicles = Vehicles.getVehicleNotRented();
                        if (vehicles.isEmpty()) {
                            System.out.println("Todos os carros já foram alugados!");
                            continue;
                        }
                        vehicles.forEach(vehicle -> System.out.println(vehicle.dataVehicle()));
                    } else if (Objects.equals(option, "6")) {
                        Rental rental = Rental.getRentalByUser(user);
                        if (rental == null) {
                            System.out.println("Você não possui nenhum aluguel!");
                            continue;
                        }
                        rental.getDataRental();
                    } else {
                        System.out.println("Opção inválida!");
                    }
                }
            }
        }
        hibernateController.closeSessionFactory();
    }

    private static void greeting() {
        System.out.println("Seja Bem Vindo a melhor locadora de carro do mundo!");
        System.out.println("Aqui você encontra os melhores carros para alugar.");
        System.out.println("Aqui você encontra os melhores preços.");
        System.out.println("Aqui você encontra os melhores serviços.");
        System.out.println("===============================================");
        System.out.println("|              Locadora de Carros             |");
        System.out.println("===============================================");
    }

    private static String getCategorySelected(String category) {
        String categorySelected = "";
        switch (category) {
            case "2" -> categorySelected = "SEDAN";
            case "3" -> categorySelected = "SUV";
            case "4" -> categorySelected = "HATCH";
        }
        return categorySelected;
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
        System.out.println("6 - Verificar aluguel");
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

    public static String optionsCategory(Scanner scanner) {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Sair");
        System.out.println("2 - SEDAN");
        System.out.println("3 - SUV");
        System.out.println("4 - HATCH");
        return scanner.nextLine();
    }

    public static void categoryEmpty(String category) {
        System.out.println("===========================================================");
        System.out.println("  Não temos carros da categoria " + category + " no momento!  ");
        System.out.println("===========================================================");
    }
}
