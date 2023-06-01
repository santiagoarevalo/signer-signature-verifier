import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class Verifier {

    public KeyPair keys;
    private static final int RSA_KEY_SIZE = 1024;
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String ENCRYPTION_ALGORITHM = "PBEWithMD5AndDES";

    // First, the RSA algorithm must be used to create a key pair (public and private) to digitally sign the file.

    public void generateKeyPair(String[] parameters) throws Exception {
        // Create RSA key
        KeyPairGenerator generatorRSA = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generatorRSA.initialize(RSA_KEY_SIZE);
        keys = generatorRSA.genKeyPair();

        // Take the encoded form of the public key for future use.
        byte[] bytesPublic = keys.getPublic().getEncoded();

        // Read the file name for the public key
        String publicKey = parameters[0];

        // Write the encoded public key to its file
        savePublicKey(bytesPublic, publicKey);

        // Repeat the same for the private key, encrypting it with a password
        String privateKey = parameters[1];

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
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);

        // Encrypt the private key
        byte[] encryptedPrivateKeyBytes = cipher.doFinal(privateKeyBytes);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(salt);
        baos.write(encryptedPrivateKeyBytes);
        return baos.toByteArray();
    }

    //Decrypt the private key
    private byte[] decryptPrivateKey(String password, byte[] encryptedPrivateKeyBytes) throws Exception {
        // Recover the salt and the encrypted private key
        byte[] salt = Arrays.copyOfRange(encryptedPrivateKeyBytes, 0, 8);
        byte[] encryptedPrivateKey = Arrays.copyOfRange(encryptedPrivateKeyBytes, 8, encryptedPrivateKeyBytes.length);

        // Generate the key from the password and salt
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);

        // Decrypt the private key
        return cipher.doFinal(encryptedPrivateKey);
    }

    public void signFile(String[] parameters) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String privateKeyPath = parameters[0];
        String password = parameters[1];
        String originalFilePath = parameters[2];
        String signatureFilePath = parameters[3];

        try {
            InputStream fis = new BufferedInputStream(new FileInputStream(originalFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            File file = new File(privateKeyPath);
            FileInputStream keyFis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = 0;
            while ((i = keyFis.read()) != -1) {
                baos.write(i);
            }
            byte[] keyBytes = baos.toByteArray();
            keyBytes = decryptPrivateKey(password, keyBytes);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(keys.getPrivate());
            signature.update(buffer);

            byte[] digitalSignature = signature.sign();
            FileOutputStream fileOutputStream = new FileOutputStream(signatureFilePath);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(digitalSignature);
            bufferedOutputStream.close();

            System.out.println("Firma digital generada y guardada en " + signatureFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifySignature(String[] parameters) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException {
        String originalFile = parameters[0];
        String signatureFile = parameters[1];
        String pbk = parameters[2];

        PublicKey pk = convertToPbKey(pbk);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pk);
        FileInputStream fis = new FileInputStream(signatureFile);
        byte[] sigFileBytes = new byte[fis.available()];
        fis.read(sigFileBytes);
        fis.close();
        FileInputStream fisTwo = new FileInputStream(originalFile);
        BufferedInputStream bis = new BufferedInputStream(fisTwo);
        byte[] buffer = new byte[1024];
        int len;
        while (bis.available() != 0) {
            len = bis.read(buffer);
            signature.update(buffer, 0, len);
        }
        bis.close();
        boolean isVerified = signature.verify(sigFileBytes);
        System.out.println("La firma es " + (isVerified ? "válida" : "inválida"));
    }

    private PublicKey convertToPbKey(String file) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] output = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            output = fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec keySpec = null;
        if (output != null) {
            keySpec = new X509EncodedKeySpec(output);
        }
        return keyFactory.generatePublic(keySpec);
    }
}
