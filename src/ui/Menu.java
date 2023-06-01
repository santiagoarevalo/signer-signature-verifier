import java.util.Scanner;

public class Menu {

    private final Verifier verifier;
    private final Scanner sc;

    public Menu() {
        this.verifier = new Verifier();
        //If the scanner doesn't work, initialize it out of the constructor
        sc = new Scanner(System.in);
    }

    public void showMenu() {
        System.out.println("================================================================================");
        System.out.println("Bienvenido al Firmador y Verificador de Firmas, por favor elija una opción");
        System.out.println("Seleccione (1) para generar el par de claves RSA");
        System.out.println("Seleccione (2) para firmar un archivo ");
        System.out.println("Seleccione (3) si desea verificar la firma de un archivo");
        System.out.println("Seleccione (4) para salir");
        System.out.println("================================================================================");
    }

    private String[] inputGenerateKeyPair() {
        String[] input = new String[4];
        System.out.println("Generando el par de claves RSA.\n");
        System.out.print("Nombre del archivo para grabar la clave pública: ");
        input[0] = sc.nextLine();
        System.out.print("Nombre del archivo para grabar la clave privada: ");
        input[1] = sc.nextLine();
        System.out.print("Indica la ruta del directorio en el que deseas guardar la clave privada (sin el nombre que indicaste anteriormente). Ejemplo: C:/Users/User/keys" +
                "\nRuta: ");
        input[2] = sc.nextLine();
        System.out.print("Contraseña para proteger la clave privada: ");
        input[3] = sc.nextLine();
        return input;
    }

    private String[] inputSignFile() {
        String[] input = new String[4];
        System.out.print("Ingrese la ruta del archivo que contiene la clave privada: ");
        input[0] = sc.nextLine();
        System.out.println("Ingrese la contraseña para desencriptar la clave privada: ");
        input[1] = sc.nextLine();
        System.out.print("Ingrese la ruta del archivo que desea firmar, con el nombre del archivo incluido: ");
        input[2] = sc.nextLine();
        System.out.print("Ingrese la ruta del directorio en el que desea guardar la firma, con el nombre del archivo incluido: ");
        input[3] = sc.nextLine();
        return input;
    }

    private String[] inputVerifySignature() {
        String[] input = new String[3];
        System.out.print("Ingrese la ruta del archivo original que desea verificar: ");
        input[0] = sc.nextLine();
        System.out.print("Ingrese la ruta del archivo que contiene la firma: ");
        input[1] = sc.nextLine();
        System.out.print("Ingrese la ruta del archivo que contiene la llave pública: ");
        input[2] = sc.nextLine();
        return input;
    }

    public void doOperation(int option) throws Exception {
        switch (option) {
            case 1 -> verifier.generateKeyPair(inputGenerateKeyPair());
            case 2 -> verifier.signFile(inputSignFile());
            case 3 -> verifier.verifySignature(inputVerifySignature());
            case 4 -> System.out.println("Gracias por usar el Firmador y Verificador de Firmas, ¡adiós!");
            case 5 -> thanks();
            default -> System.out.println("Error, opción no válida");
        }
    }

    public int readOption() {
        int option = sc.nextInt();
        sc.nextLine();
        return option;
    }

    public void startProgram() throws Exception {
        int option;
        showLogo();
        do {
            showMenu();
            option = readOption();
            doOperation(option);
        } while (option != 4);
    }

    private void showLogo() {
        System.out.println(" _____ _ _               _____                      _ _         ");
        System.out.println("/  __ (_) |             /  ___|                    (_) |        ");
        System.out.println("| /  \\/_| |__   ___ _ __\\ `--.  ___  ___ _   _ _ __ _| |_ _   _ ");
        System.out.println("| |   | | '_ \\ / _ \\ '__|`--. \\/ _ \\/ __| | | | '__| | __| | | |");
        System.out.println("| \\__/\\ | |_) |  __/ |  /\\__/ /  __/ (__| |_| | |  | | |_| |_| |");
        System.out.println(" \\____/_|_.__/ \\___|_|  \\____/ \\___|\\___|\\__,_|_|  |_|\\__|\\__, |");
        System.out.println("                                                           __/ |");
        System.out.println("                                                          |___/ ");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void thanks() {
        System.out.println(" _____                _                ______           __     ");
        System.out.println("|  __ \\              (_)              | ___ \\       |  / _|    ");
        System.out.println("| |  \\/_ __ __ _  ___ _  __ _ ___     | |_/ / __ ___ | |_ ___ ");
        System.out.println("| | __| '__/ _` |/ __| |/ _` / __|     |  __/ '__/ _ \\|  _/ _ \\");
        System.out.println("| |_\\ \\ | | (_| | (__| | (_| \\__ \\_ _ _| |  | | | (_) | ||  __/");
        System.out.println(" \\____/_|  \\__,_|\\___|_|\\__,_|___(_|_|_)_|  |_|  \\___/|_| \\___|");
    }
}
