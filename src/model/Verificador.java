
import java.security.*;
import javax.crypto.*;

import java.io.*;
import java.util.*;

public class Verificador {

    public String ruta_privada;

    public KeyPair claves;

    // Primero se debe usar el algoritmo RSA para crear un par de claves (publica y
    // privada) para firmar el archivo digitalmente

    public void generarParDeClaves() throws Exception {
        // Crear la clave RSA
        System.out.println("Generando el par de claves RSA.\n");
        KeyPairGenerator generadorRSA = KeyPairGenerator.getInstance("RSA");
        generadorRSA.initialize(2048);
        KeyPair claves = generadorRSA.genKeyPair();

        // Toma la forma codificada de la clave pública para usarla en el futuro.
        byte[] bytesPublica = claves.getPublic().getEncoded();

        // Leer el nombre del archivo para la clave pública
        Scanner sc = new Scanner(System.in);
        System.out.print("Nombre del archivo para grabar la clave pública: ");
        String clavePublica = sc.nextLine();

        // Escribir la clave pública codificada en su fichero
        FileOutputStream salida = new FileOutputStream(clavePublica);
        salida.write(bytesPublica);
        salida.close();

        // Repetir lo mismo para la clave privada, encriptándola con una contraseña.
        System.out.print("Nombre del archivo para grabar la clave privada: ");
        String clavePrivada = sc.nextLine();

        // Se debe guardar la ruta donde se va a alojar la clave privada para próximos
        // usos
        ruta_privada = "C:/Users/USUARIO/Desktop/Séptimo semestre/Seguridad/Proyecto final/signer-signature-verifier/claves/"
                + clavePrivada + "";

        // Tomar la forma codificada de la clave privada
        byte[] bytesPrivada = claves.getPrivate().getEncoded();

        // Solicitar la contraseña para encriptar la clave privada
        System.out.print("Contraseña para encriptar la clave privada: ");
        String password = sc.nextLine();

        // Aquí encriptamos hacemos un llamado al método que encripta la clave privada
        byte[] bytesPrivadaEncriptados = encriptarPrivateKey(password.toCharArray(), bytesPrivada);

        // Grabar el resultado en el fichero
        salida = new FileOutputStream(clavePrivada);
        salida.write(bytesPrivadaEncriptados);
        salida.close();
        sc.close();

        System.out.println("El par de claves RSA se ha generado y guardado exitosamente.");
    }

    private static byte[] encriptarPrivateKey(char[] password, byte[] texto) throws Exception {

        // Se crean los primeros 8 bytes , valor aleatorio único que se añade a los
        // datos antes de ser encriptados

        byte[] inicio = new byte[8];
        Random aleatorio = new Random();
        aleatorio.nextBytes(inicio);

        // Crear una clave RSA
        KeyPairGenerator generadorRSA = KeyPairGenerator.getInstance("RSA");
        generadorRSA.initialize(1024);
        KeyPair claves = generadorRSA.genKeyPair();
        PublicKey clavePublica = claves.getPublic();

        // Obtener el cifrador RSA en modo ECB
        Cipher cifrador = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cifrador.init(Cipher.ENCRYPT_MODE, clavePublica);

        byte[] textoCifrado = cifrador.doFinal(texto);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(inicio);
        baos.write(textoCifrado);
        return baos.toByteArray();
    }

    public void firmarArchivo() {

    }

    public void verificarFirma() {
    }
}
