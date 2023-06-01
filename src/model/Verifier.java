import java.security.*;
import javax.crypto.*;

import java.io.*;
import java.util.*;

public class Verifier {

    public KeyPair keys;
    private static final int RSA_KEY_SIZE = 2048;

    // First, the RSA algorithm must be used to create a key pair (public and private) to digitally sign the file.

    public void generateKeyPair(String[] parameters) throws Exception {
        // Create RSA key
        KeyPairGenerator generatorRSA = KeyPairGenerator.getInstance("RSA");
        generatorRSA.initialize(RSA_KEY_SIZE);
        KeyPair keys = generatorRSA.genKeyPair();

        System.out.println("Public key generated: " + keys.getPublic());

        // Take the encoded form of the public key for future use.
        byte[] bytesPublic = keys.getPublic().getEncoded();

        // Read the file name for the public key
        String publicKey = parameters[0];

        // Write the encoded public key to its file
        savePublicKey(bytesPublic, publicKey);

        // Repeat the same for the private key, encrypting it with a password
        String privateKey = parameters[1];

        // The path where the private key is to be stored must be saved for upcoming future uses
        String privatePath = parameters[2] + "/" + privateKey;

        System.out.println("Public key generated: " + keys.getPrivate());
        // Take the encoded form of the private key
        byte[] bytesPrivate = keys.getPrivate().getEncoded();

        // Get the password to protect the private key
        String password = parameters[3];

        // Here we encrypt making a call to the method that encrypts the private key
        byte[] encryptedBytesPrivate = encryptPrivateKey(password.toCharArray(), bytesPrivate);

        // Write the result to the file
        savePrivateKey(encryptedBytesPrivate, privateKey);

        System.out.println("El par de claves RSA se ha generado y guardado exitosamente.");
    }

    private void savePublicKey(byte[] publicKeyBytes, String privateKeyName) throws Exception {
        FileOutputStream output = new FileOutputStream(privateKeyName);
        output.write(publicKeyBytes);
        output.close();
    }

    private void savePrivateKey(byte[] privateKeyBytes, String privateKeyName) throws Exception {
        FileOutputStream output = new FileOutputStream(privateKeyName);
        output.write(privateKeyBytes);
        output.close();
    }

    private byte[] encryptPrivateKey(char[] password, byte[] privateBytes) throws Exception {

        // The first 8 bytes are created, a unique random value that is added to the data before it is encrypted
        byte[] start = new byte[8];
        Random random = new Random();
        random.nextBytes(start);

        // Create a RSA key
        KeyPairGenerator generatorRSA = KeyPairGenerator.getInstance("RSA");
        generatorRSA.initialize(RSA_KEY_SIZE);
        KeyPair claves = generatorRSA.genKeyPair();
        PublicKey publicKey = claves.getPublic();

        // Get RSA cipher in ECB mode with PKCS1 padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        System.out.println(Arrays.toString(privateBytes));
        System.out.println(privateBytes.length);
        byte[] cipheredText = cipher.doFinal(privateBytes);

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
