import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

/**
 * The Servidor class represents a server that continuously accepts client
 * connections
 * and handles encrypted message exchanges using RSA protocol. This class
 * extends Thread,
 * allowing the server to run continuously in a separate thread.
 */
public class Servidor extends Thread {
    private GestorCifradoRSA gestor; // RSA manager for encryption and decryption.
    final static int PORT = 8080; // Server port number.
    private ServerSocket serverSocket; // Server socket for accepting connections.

    /**
     * Constructor for the Servidor class.
     * Initializes the RSA encryption manager and creates a new ServerSocket
     * to listen on the specified port.
     * 
     * @throws NoSuchAlgorithmException if the RSA encryption algorithm is not
     *                                  available.
     * @throws NoSuchPaddingException   if the specified padding scheme is not
     *                                  available.
     */
    public Servidor() throws NoSuchAlgorithmException, NoSuchPaddingException {
        // Initialize the RSA manager for encryption and decryption.
        this.gestor = new GestorCifradoRSA();
        try {
            // Create a ServerSocket to listen on the specified port.
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The run method is executed when the thread starts.
     * It continuously accepts client connections and handles message exchanges
     * between the server and clients.
     */
    public void run() {
        // Infinite loop to accept and handle client connections continuously.
        while (true) {
            try {
                // Accepts an incoming connection from a client.
                Socket clientSocket = serverSocket.accept();
                // Log message indicating that a client has connected.
                System.out.println("Servidor: Cliente conectado");

                // Creates an input stream to read the client's public key.
                ObjectInputStream inObj = new ObjectInputStream(clientSocket.getInputStream());
                PublicKey clavePublicaCliente = (PublicKey) inObj.readObject();
                System.out.println("Clave publica cliente recibida");

                // Creates an output stream to send the server's public key to the client.
                ObjectOutputStream outObj = new ObjectOutputStream(clientSocket.getOutputStream());
                outObj.writeObject(gestor.getPublica()); // Send server's public key.
                outObj.flush();
                System.out.println("Clave publica servidor enviada");

                // Sends an encrypted message to the client using the client's public key.
                DataOutputStream outStr = new DataOutputStream(clientSocket.getOutputStream());
                byte[] falaCifrado = gestor.cifrar("Fala amigo e entra".getBytes(), clavePublicaCliente);
                // Send the length of the encrypted message first, followed by the encrypted
                // message.
                outStr.writeInt(falaCifrado.length);
                outStr.write(falaCifrado);
                outStr.flush();

                // Receives and decrypts the response message from the client.
                DataInputStream inStr = new DataInputStream(clientSocket.getInputStream());
                int length = inStr.readInt(); // Reads the length of the encrypted message.
                byte[] mallonRecibido = new byte[length];
                inStr.readFully(mallonRecibido, 0, length); // Reads the encrypted message content.
                // Decrypt the client's response using the server's private key.
                String respostaMellon = new String(gestor.descifrar(mallonRecibido, gestor.getPrivada()));
                System.out.println("Respuesta Mallon recibida");

                // Validate the decrypted response and send confirmation to the client.
                if (respostaMellon.equals("Mallon")) {
                    outStr.writeUTF("As portas de Moria están abiertas para ti, amigo"); // Success message.
                } else {
                    System.out.println("Clave incorrecta"); // Log incorrect password message.
                }

            } catch (IOException | ClassNotFoundException | InvalidKeyException | IllegalBlockSizeException
                    | BadPaddingException e) {
                // Print stack trace for any errors during connection handling or data
                // processing.
                e.printStackTrace();
            }
        }
    }

    /**
     * Main method to start the server as a thread.
     * 
     * @throws NoSuchAlgorithmException if the RSA encryption algorithm is
     *                                  unavailable.
     * @throws NoSuchPaddingException   if the padding scheme is unavailable.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Servidor servidor = new Servidor(); // Create a new server instance.
        servidor.start(); // Start the server thread.
    }
}
