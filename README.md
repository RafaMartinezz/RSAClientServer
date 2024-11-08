# Client-Server communication project with RSA encryption

## Description

This project implements a secure communication system between a client and a server using RSA asymmetric encryption. The client and server exchange public keys to establish a secure connection, enabling the encrypted transmission of messages. Both parties utilize encryption and decryption keys generated via the `GestorCifradoRSA` class.

## Features

- **RSA Asymmetric Encryption**: Ensures communication security through the use of generated public and private RSA keys.
- **Multithreaded Server**: Allows the server to accept multiple client connections and handle communication in parallel.
- **Public Key Exchange**: Client and server exchange public keys at the start of communication to establish encryption protocols.
- **Message Validation**: The server verifies the client's response to confirm authenticity.

## Project Structure

- **`GestorCifradoRSA`**: Handles RSA key generation and data encryption/decryption.
- **`Servidor`**: Represents the server, listens for incoming connections, and manages the encrypted message exchange with clients.
- **`Cliente`**: Represents the client, establishes a secure connection to the server, and enables encrypted message transmission.

## Prerequisites

- Java 11 or higher.
- Internet connection for local client-server testing on `localhost`.

## Project Workflow

### Communication Flow

1. The server starts in listening mode to receive client connections.
2. The client connects to the server on `localhost` at port `8080`.
3. Once the connection is established:
   - **Public Key Exchange**: The client and server exchange public keys to allow encrypted messaging.
   - **Encrypted Message Transmission**: The server sends an initial encrypted message using the client's public key.
   - **Response Validation**: The client replies with an encrypted message, which the server validates before confirming the connection.

### Example Messages

- Initial message from the server to the client: "Fala amigo e entra."
- Expected response from the client: "Mallon."
- Final message from the server if the client responds correctly: "The gates of Moria are open to you, friend."

## Key Classes and Methods

### 1. `GestorCifradoRSA`
   - **Key Generation**: Creates public and private key pairs.
   - **Encryption and Decryption**: Provides methods to encrypt and decrypt data using the generated keys.

### 2. `Servidor`
   - **run()**: Accepts incoming connections and manages encrypted communication with the client.
   - **Message Exchange**: Sends and receives encrypted messages, validating the client’s response.

### 3. `Cliente`
   - **connect()**: Connects to the server, exchanges keys, and enables secure communication through encryption and decryption.

## Exception Handling

- **NoSuchAlgorithmException**: Thrown if the RSA encryption algorithm is unavailable.
- **NoSuchPaddingException**: Thrown if the specified padding scheme is unavailable.
- **IOException**: For input/output errors during communication.
- **InvalidKeyException**: Thrown if the encryption or decryption key is invalid.
- **IllegalBlockSizeException**: Thrown if the data block size is inappropriate for encryption.
- **BadPaddingException**: Thrown in case of padding issues with the encrypted data.

## Notes

- This project is a simulation of secure communication using RSA encryption. It was developed for learning purposes and may not meet all security requirements for production environments.
- Exposing the private key in insecure environments is not recommended.
