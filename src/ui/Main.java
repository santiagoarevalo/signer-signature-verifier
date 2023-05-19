import java.util.Scanner;

public class Main {

    private static Verificador verificador;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Firmador y verificador de firmas");
        System.out.println("Opciones disponibles:");
        System.out.println("1) Generar par de claves RSA");
        System.out.println("2) Firmar archivo");
        System.out.println("3) Verificar firma");
        System.out.print("Seleccione una opción: ");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                verificador.generarParDeClaves();
                break;
            case 2:
                verificador.firmarArchivo();
                break;
            case 3:
                verificador.verificarFirma();
                break;
            default:
                System.out.println("Opcion invalida.");
                break;
        }

        scanner.close();
    }

}
