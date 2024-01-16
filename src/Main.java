import Connection.VehiculoDAO;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        VehiculoDAO vehiculoDAO = new VehiculoDAO();

        vehiculoDAO.generateReportVehiculosRegistrados("xd");

        int opcion = 1;
        String placa = "";
        while (opcion != 0){
            System.out.println(
                    "Elige la opción a realizar: \n" +
                            "[1] Registro de entrada \n" +
                            "[2] Registro de salida \n" +
                            "[3] Dar de alta vehiculo oficial \n" +
                            "[4] Dar de alta vehiculo residente \n" +
                            "[5] Comianza el mes \n" +
                            "[6] Pago de residentes \n" +
                            "[0] Salir"
            );

            Scanner scanner = new Scanner(System.in);
            opcion = scanner.nextInt();
            Scanner placaScanner = new Scanner(System.in);
            switch (opcion) {
                case 1:
                    System.out.println("Dijite la placa del auto");
                    placa = placaScanner.nextLine();

                    vehiculoDAO.registroEntrada(placa);
                    break;
                case 2:
                    System.out.println("Dijite la placa del auto");
                    placa = placaScanner.nextLine();
                    vehiculoDAO.registroSalida(placa);
                    break;
                case 3:
                    System.out.println("Dijite la placa del auto");
                    placa = placaScanner.nextLine();
                    vehiculoDAO.altaVehiculoOficial(placa);
                    break;
                case 4:
                    System.out.println("Dijite la placa del auto");
                    placa = placaScanner.nextLine();
                    vehiculoDAO.altaVehiculoResidente(placa);
                    break;
                case 5:
                    System.out.println("Comienza el mes!!!!");
                    vehiculoDAO.comienzaMes();
                    break;
                case 6:
                    System.out.println("Dijite el nombre del archivo");
                    String nombre = placaScanner.nextLine();
                    vehiculoDAO.generateReportVehiculosRegistrados(nombre);
                    break;
                default:
                    System.out.println("Bye");
                    break;
            }

        }
    }

}