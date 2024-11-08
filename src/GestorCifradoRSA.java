import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * The GestorCifradoRSA class handles RSA encryption and decryption operations.
 * This class provides methods to generate RSA key pairs, encrypt data with a
 * public key,
 * and decrypt data with a private key.
 */
public class GestorCifradoRSA {
    // Key pair (public and private) for RSA encryption and decryption.
    private KeyPair claves;
    // Key pair generator for creating RSA key pairs.
    private KeyPairGenerator generadorClaves;
    // Cipher object to handle encryption and decryption processes.
    private Cipher cifrador;

    /**
     * Constructor for GestorCifradoRSA.
     * Initializes the RSA key pair generator and the Cipher for encryption and
     * decryption.
     * 
     * @throws NoSuchAlgorithmException if the RSA algorithm is unavailable.
     * @throws NoSuchPaddingException   if the specified padding is unavailable.
     */
    public GestorCifradoRSA() throws NoSuchAlgorithmException, NoSuchPaddingException {
        // Initializes RSA key generator with 1024-bit key size.
        this.generadorClaves = KeyPairGenerator.getInstance("RSA");
        this.generadorClaves.initialize(1024); // Sets key size to 1024 bits.
        // Generates RSA key pair.
        this.claves = this.generadorClaves.generateKeyPair();
        // Initializes Cipher for RSA encryption and decryption.
        this.cifrador = Cipher.getInstance("RSA");
    }

    /**
     * Returns the public key from the RSA key pair.
     * 
     * @return RSA public key.
     */
    public PublicKey getPublica() {
        return this.claves.getPublic();
    }

    /**
     * Returns the private key from the RSA key pair.
     * 
     * @return RSA private key.
     */
    public PrivateKey getPrivada() {
        return this.claves.getPrivate();
    }

    /**
     * Encrypts data using the provided encryption key.
     * 
     * @param paraCifrar   Data to be encrypted.
     * @param claveCifrado Key used for encryption (usually a public key).
     * @return Encrypted data as a byte array.
     * @throws InvalidKeyException       if the key is invalid for RSA encryption.
     * @throws IllegalBlockSizeException if the data block size is not appropriate
     *                                   for RSA.
     * @throws BadPaddingException       if there's an issue with data padding.
     */
    public byte[] cifrar(byte[] paraCifrar, Key claveCifrado)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Initialize cipher in encryption mode with the specified key.
        cifrador.init(Cipher.ENCRYPT_MODE, claveCifrado);
        // Encrypts the data and returns it as a byte array.
        return cifrador.doFinal(paraCifrar);
    }

    /**
     * Decrypts data using the provided decryption key.
     * 
     * @param paraDescifrar   Data to be decrypted.
     * @param claveDescifrado Key used for decryption (usually a private key).
     * @return Decrypted data as a byte array.
     * @throws InvalidKeyException       if the key is invalid for RSA decryption.
     * @throws IllegalBlockSizeException if the data block size is not appropriate
     *                                   for RSA.
     * @throws BadPaddingException       if there's an issue with data padding.
     */
    public byte[] descifrar(byte[] paraDescifrar, Key claveDescifrado)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Initialize cipher in decryption mode with the specified key.
        cifrador.init(Cipher.DECRYPT_MODE, claveDescifrado);
        // Decrypts the data and returns it as a byte array.
        return cifrador.doFinal(paraDescifrar);
    }

    /**
     * Prints the byte array as text to the console.
     * 
     * @param buffer Byte array to print.
     * @throws IOException if an error occurs while converting bytes to text.
     */
    public void mostrarBytes(byte[] buffer) throws IOException {
        // Converts the byte array to a string and prints it to the console.
        String texto = new String(buffer);
        System.out.println(texto);
    }
}
