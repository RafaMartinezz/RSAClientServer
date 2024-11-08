import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

/**
 * The Cliente class encapsulates the functionality of a client that establishes
 * a
 * socket connection to a server, exchanges public keys, and securely
 * communicates
 * using RSA encryption.
 */
public class Cliente {
    private GestorCifradoRSA gestor; // Manager for RSA encryption and decryption
    final static int PORT = 8080; // Port number for the socket connection
    private Socket socket; // Socket for communication with the server

    /**
     * Constructor for the Cliente class.
     * Initializes the RSA encryption manager and configures the socket connection.
     *
     * @throws NoSuchAlgorithmException if the RSA algorithm is not available.
     * @throws NoSuchPaddingException   if the specified padding mechanism is not
     *                                  available.
     */
    public Cliente() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.gestor = new GestorCifradoRSA(); // Initializes the RSA encryption manager
        InetAddress direccion;
        try {
            direccion = InetAddress.getByName("localhost"); // Sets server address to localhost
            this.socket = new Socket(direccion, PORT); // Opens a socket connection on specified port
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to connect and communicate with the server.
     * Sends the client's public key, receives the server's public key, exchanges
     * encrypted
     * messages, and displays responses.
     */
    public void conectar() {
        try {
            // Send client's public key to the server
            ObjectOutputStream outObj = new ObjectOutputStream(socket.getOutputStream());
            outObj.writeObject(gestor.getPublica()); // Send public key
            outObj.flush();

            // Receive server's public key
            ObjectInputStream inObj = new ObjectInputStream(socket.getInputStream());
            PublicKey clavePublicaServidor = (PublicKey) inObj.readObject(); // Receive public key

            // Receive and decrypt a message from the server
            DataInputStream inStr = new DataInputStream(socket.getInputStream());
            int lengthFala = inStr.readInt(); // Length of incoming encrypted message
            byte[] falaRecibido = new byte[lengthFala];
            inStr.readFully(falaRecibido, 0, lengthFala); // Read encrypted message
            String respostaFala = new String(gestor.descifrar(falaRecibido, gestor.getPrivada())); // Decrypt with
                                                                                                   // private key
            System.out.println(respostaFala); // Display server's decrypted message

            // Send an encrypted message to the server
            DataOutputStream outStr = new DataOutputStream(socket.getOutputStream());
            byte[] mallonCifrado = gestor.cifrar("Mallon".getBytes(), clavePublicaServidor); // Encrypt message with
                                                                                             // server's public key
            int lengthMallon = mallonCifrado.length;
            outObj.writeInt(lengthMallon); // Send length of encrypted message
            outStr.write(mallonCifrado); // Send encrypted message
            outStr.flush();

            // Receive server's final response
            String portas = inStr.readUTF(); // Read and display server's final response
            System.out.println(portas);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // Handle invalid key error
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // Handle illegal block size error
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // Handle incorrect padding error
            e.printStackTrace();
        }
    }

    /**
     * Main method to execute the client.
     * Creates an instance of the client and connects it to the server.
     *
     * @param args Command line arguments (not used in this example).
     * @throws NoSuchAlgorithmException if the RSA algorithm is not available.
     * @throws NoSuchPaddingException   if the specified padding mechanism is not
     *                                  available.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cliente cliente = new Cliente(); // Create a new client instance
        cliente.conectar(); // Connect the client to the server
    }
}
