import java.util.Scanner;

public class Menu {

    private Verificador verificador;
    Scanner sc = new Scanner(System.in);

    public Menu() {
        this.verificador = new Verificador();
    }

    public void showMenu() {
        System.out.println(
                "Bienvenido al menú, por favor elija una opción");
        System.out.println("Seleccione (1) para generar el par de claves RSA");
        System.out.println("Seleccione (2) para verificar archivo ");
        System.out.println("Seleccione (3) si desea verificar la firma");

        System.out.println("Seleccione (10) salir");
    }

    public void doOperation(int option) throws Exception {
        switch (option) {
            case 1:
                verificador.generarParDeClaves();
                break;

            case 2:
                ;
                break;

            case 3:

                System.out.println("Gracias por usar esta aplicacion, vuelva pronto");
                break;

            default:
                System.out.println("Error, opción no válida");
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
        } while (option != 10);
    }
}
