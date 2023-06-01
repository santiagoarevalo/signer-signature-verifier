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
        System.out.println("Bienvenido al Firmador y Verificafor de Firmas, por favor elija una opción");
        System.out.println("Seleccione (1) para generar el par de claves RSA");
        System.out.println("Seleccione (2) para firmar un archivo ");
        System.out.println("Seleccione (3) si desea verificar la firma de un archivo");
        System.out.println("Seleccione (4) para salir");
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

    public void doOperation(int option) throws Exception {
        switch (option) {
            case 1 -> verifier.generateKeyPair(inputGenerateKeyPair());
            case 2 -> verifier.signFile();
            case 3 -> verifier.verifySignature();
            case 4 -> System.out.println("Gracias por usar el Firmador y Verificador de Firmas, ¡adiós!");
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
        do {
            showMenu();
            option = readOption();
            doOperation(option);
        } while (option != 4);
    }
}
