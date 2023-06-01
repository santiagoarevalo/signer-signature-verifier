import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

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

        // Take the encoded form of the private key
        byte[] bytesPrivate = keys.getPrivate().getEncoded();

        // Get the password to protect the private key
        String password = parameters[3];

        // Here we encrypt making a call to the method that encrypts the private key
        byte[] encryptedPrivateKeyBytes = encryptPrivateKey(password, bytesPrivate);

        // Write the result to the file
        savePrivateKey(encryptedPrivateKeyBytes, privateKey);

        System.out.println("El par de claves RSA se ha generado y guardado exitosamente.");
    }

    private void savePublicKey(byte[] publicKeyBytes, String privateKeyName) throws Exception {
        FileOutputStream output = new FileOutputStream(privateKeyName);
        output.write(publicKeyBytes);
        output.close();
        System.out.println("La clave pública se guardó correctamente");
    }

    private void savePrivateKey(byte[] privateKeyBytes, String privateKeyName) throws Exception {
        FileOutputStream output = new FileOutputStream(privateKeyName);
        output.write(privateKeyBytes);
        output.close();
        System.out.println("La clave privada se guardó correctamente");
    }

    private byte[] encryptPrivateKey(String password, byte[] privateKeyBytes) throws Exception {
        // Generate a random salt
        byte[] salt = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Generate a key from the password and salt
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBE");
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);

        // Encrypt the private key
        byte[] encryptedPrivateKeyBytes = cipher.doFinal(privateKeyBytes);

        // Get the parameters of the encryption algorithm
        AlgorithmParameters algorithmParameters = cipher.getParameters();

        // Save the salt and the encrypted private key to a EncryptedPrivateKeyInfo object
        EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(algorithmParameters, encryptedPrivateKeyBytes);

        return encryptedPrivateKeyInfo.getEncoded();
    }

    public void signFile() {

    }

    public void verifySignature() {
    }
}
