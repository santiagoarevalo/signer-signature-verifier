import java.security.*;
import javax.crypto.*;

import java.io.*;
import java.util.*;

public class Verifier {

    public String privatePath;

    public KeyPair keys;

    // First, the RSA algorithm must be used to create a key pair (public and private) to digitally sign the file.

    public void generateKeyPair() throws Exception {
        // Create RSA key
        System.out.println("Generando el par de claves RSA.\n");
        KeyPairGenerator generatorROSA = KeyPairGenerator.getInstance("RSA");
        generatorROSA.initialize(2048);
        KeyPair claves = generatorROSA.genKeyPair();

        // Take the encoded form of the public key for future use.
        byte[] bytesPublic = claves.getPublic().getEncoded();

        // Read the file name for the public key
        //TODO: Move this user input to the Menu class
        Scanner sc = new Scanner(System.in);
        System.out.print("Nombre del archivo para grabar la clave pública: ");
        String publicKey = sc.nextLine();

        // Write the encoded public key to its file
        FileOutputStream output = new FileOutputStream(publicKey);
        output.write(bytesPublic);
        output.close();

        // Repeat the same for the private key, encrypting it with a password
        System.out.print("Nombre del archivo para grabar la clave privada: ");
        String privateKey = sc.nextLine();

        // The path where the private key is to be stored must be saved for upcoming future uses
        privatePath = "C:/Users/USUARIO/Desktop/Séptimo semestre/Seguridad/Proyecto final/signer-signature-verifier/claves/"
                + privateKey + "";

        // Take the encoded form of the private key
        byte[] bytesPrivate = claves.getPrivate().getEncoded();

        // Get the password to protect the private key
        System.out.print("Contraseña para proteger la clave privada: ");
        String password = sc.nextLine();

        // Here we encrypt making a call to the method that encrypts the private key
        byte[] encryptedBytesPrivate = encryptPrivateKey(password.toCharArray(), bytesPrivate);

        // Write the result to the file
        output = new FileOutputStream(privateKey);
        output.write(encryptedBytesPrivate);
        output.close();
        sc.close();

        System.out.println("El par de claves RSA se ha generado y guardado exitosamente.");
    }

    private static byte[] encryptPrivateKey(char[] password, byte[] text) throws Exception {

        // The first 8 bytes are created, a unique random value that is added to the data before it is encrypted
        byte[] start = new byte[8];
        Random random = new Random();
        random.nextBytes(start);

        // Create a RSA key
        KeyPairGenerator generatorRSA = KeyPairGenerator.getInstance("RSA");
        generatorRSA.initialize(1024);
        KeyPair claves = generatorRSA.genKeyPair();
        PublicKey publicKey = claves.getPublic();

        // Get RSA cipher in ECB mode with PKCS1 padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipheredText = cipher.doFinal(text);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(start);
        baos.write(cipheredText);
        return baos.toByteArray();
    }

    public void signFile() {

    }

    public void verifySignature() {
    }
}
