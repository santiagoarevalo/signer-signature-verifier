# Informe Firmador y Verificador de Firma

*Integrantes:* 
Santiago Arévalo Valencia | Luis Alfonso Murcia Hernandez | Paula Andrea Trujillo Mejía

En este informe, se presenta en detalle el desarrollo de un programa que tiene la capacidad de generar un par de claves RSA con las cuales se permitirá un firmado seguro de un archivo digitalmente para garantizar la integridad, autenticidad y no repudio de un documento electrónico y además, permite la verificación de firmas para validar la autenticidad e integridad de un documento firmado digitalmente, todo esto implementado el uso de las bibliotecas java.security y java.crypto. 

El objetivo principal de este programa es garantizar la seguridad de los datos mediante el uso de algoritmos criptográficos seguros y técnicas de encriptación asimétrica. Además, ofrece la capacidad de generar un par de claves RSA (pública y privada) para su uso en el proceso de firma y verificación.

A lo largo de este informe, se describirá la estructura y funcionalidad del programa, incluyendo las etapas de generación de claves, firma digital y verificación. También se abordarán las dificultades encontradas durante el desarrollo y se propondrán soluciones correspondientes. Por último, se discutirán las conclusiones y consideraciones relevantes sobre el uso de estas bibliotecas para implementar funcionalidades criptográficas y de firma digital en aplicaciones Java.

# ¿Cómo se hizo el programa?

En este programa se utilizaron las bibliotecas java.security y java.crypto. Estas bibliotecas proporcionan una amplia gama de funciones y herramientas para llevar a cabo operaciones criptográficas como encriptación, desencriptación, generación de claves públicas y privadas, así como la creación y verificación de firmas digitales.

El programa ha sido desarrollado utilizando el lenguaje de programación Java y se ha estructurado de la siguiente manera:

Importación de las bibliotecas necesarias:

```java
import java.security.*;
import javax.crypto.*;
```
Utilización de las bibliotecas para generar claves públicas y privadas, utilizando el algoritmo RSA:

```java
public void generateKeyPair() throws Exception {
     // Create RSA key
     System.out.println("Generando el par de claves RSA.\n");
     KeyPairGenerator generatorROSA = KeyPairGenerator.getInstance("RSA");
     generatorROSA.initialize(2048);
     KeyPair claves = generatorROSA.genKeyPair();
```
Encriptación de datos utilizando una clave pública:

```java
// Get RSA cipher in ECB mode with PKCS1 padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        System.out.println(Arrays.toString(privateBytes));
        System.out.println(privateBytes.length);
        byte[] cipheredText = cipher.doFinal(privateBytes);
  ```
  
  
En pocas palabras, estos métodos reciben un arreglo de bytes que representa una clave pública o privada y lo guardan en un archivo en el sistema de archivos, asegurándose de cerrar correctamente los recursos utilizados. Cabe resaltar que estos métodos son llamados por el método principal encargado de generar el par de claves RSA.

```java
private void savePublicKey(byte[] publicKeyBytes, String privateKeyName) throws Exception {
        FileOutputStream output = new FileOutputStream(privateKeyName);
        output.write(publicKeyBytes);
        output.close();
    }
```
Luego, es necesario realizar el proceso de encriptación de una clave privada utilizando una contraseña proporcionada por el usuario. Comienza generando una cadena aleatoria para aumentar la seguridad de la encriptación (hacerlo más resistente a ataques criptográficos, como los ataques de fuerza bruta y los ataques de diccionario). Luego, se deriva una clave secreta a partir de la contraseña y la cadena utilizando el algoritmo PBE. A continuación, se configura el cifrado utilizando el algoritmo PBEWithMD5AndDES. El objeto Cipher se inicializa en el modo de encriptación y se utiliza para encriptar la clave privada, luego se obtienen los parámetros del algoritmo de encriptación, incluyendo la cadena aleatoria utilizada. Finalmente, se crea un objeto EncryptedPrivateKeyInfo que contiene los parámetros y la clave privada encriptada, que se devuelve como un arreglo de bytes.

```java
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
   }
```

# Dificultades encontradas

Durante el desarrollo del proyecto nos encontramos con el error "Data must not be longer than 117 bytes", el cual generalmente está asociado con el cifrado RSA, que es un algoritmo de cifrado asimétrico utilizado para proteger la información. El mensaje indica que el tamaño de los datos que estás tratando de cifrar excede el tamaño máximo permitido para el algoritmo RSA.

Es importante tener en cuenta que RSA tiene un límite en la cantidad de datos que puede cifrar en una sola operación. Este límite está determinado por el tamaño de la clave utilizada en el cifrado RSA. Si estás utilizando una clave de tamaño razonable, el límite suele ser alrededor de 117 bytes para cifrado RSA de 1024 bits

Por otro lado, al intentar usar la clase Certificate de la librería de seguridad de Java está marcada como obsoleta (deprecated) en el API de Java a partir de Java 9. En su lugar, se utilizaron las clases de la API de seguridad de Java, como X509Certificate, para trabajar con certificados.

Durante el desarrollo del proyecto, nos encontramos con el desafío de utilizar el algoritmo "PBEWithMD5AndDES" para la encriptación basada en contraseña. Sin embargo, descubrimos que este algoritmo se considera obsoleto y tiene limitaciones de seguridad. El uso de MD5 y DES en la encriptación puede dejar la aplicación vulnerable a ataques de fuerza bruta y de diccionario.

# Conclusiones

En conclusión, se ha llevado a cabo el desarrollo exitoso del programa de firmado y verificación de firmas utilizando las bibliotecas java.security y java.crypto. El programa ha sido diseñado con la capacidad de generar un par de claves RSA (pública y privada) para llevar a cabo el firmado seguro de archivos digitales, asegurando la integridad, autenticidad y no repudio de los documentos electrónicos.

Durante el desarrollo, se encontraron desafíos relacionados con el uso de algoritmos obsoletos y débiles en el cifrado de contraseñas. Para superar estas limitaciones, se optó por algoritmos más seguros y actualizados, como PBKDF2, que ofrecen una mayor resistencia a ataques criptográficos. Además, se implementó la funcionalidad de guardar las claves públicas y privadas generadas en archivos en el sistema de archivos. Esto permite conservar las claves de forma segura y utilizarlas posteriormente para la verificación de firmas.

Finalmente, el programa desarrollado proporciona una solución completa para el firmado y verificación de firmas de archivos digitales. Utilizando claves RSA y algoritmos de encriptación seguros, se garantiza la integridad y autenticidad de los documentos electrónicos, brindando un nivel adicional de seguridad en el intercambio de información sensible. Durante el proceso de desarrollo, se tuvieron en cuenta las mejores prácticas de seguridad y se implementaron medidas para proteger los datos confidenciales y prevenir posibles vulnerabilidades. El programa resultante es una herramienta confiable y efectiva para garantizar la seguridad en el manejo de documentos digitales y asegurar la confidencialidad y autenticidad de la información.
