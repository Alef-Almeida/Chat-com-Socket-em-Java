import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter logger; // Para escrever no arquivo de log
    private String clientAddress;

    public ClientHandler(Socket socket, PrintWriter logger) {
        this.clientSocket = socket;
        this.logger = logger;
        this.clientAddress = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter saida = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            System.out.println("Cliente conectado: " + clientAddress);
            logMessage("Cliente conectado: " + clientAddress);
            saida.println("Bem-vindo ao servidor! Você está conectado como " + clientAddress);

            String mensagemCliente;
            while ((mensagemCliente = entrada.readLine()) != null) {
                String logEntry = "[" + clientAddress + "]: " + mensagemCliente;
                System.out.println(logEntry); // Exibe no console do servidor
                logMessage(logEntry);      // Salva no arquivo de log

                if ("exit".equalsIgnoreCase(mensagemCliente.trim())) {
                    saida.println("Servidor: Entendido. Encerrando sua conexão.");
                    System.out.println("Cliente " + clientAddress + " solicitou encerramento.");
                    logMessage("Cliente " + clientAddress + " desconectado (solicitado pelo cliente).");
                    break;
                }
                // Resposta simples para o cliente
                saida.println("Servidor recebeu: " + mensagemCliente);
            }

        } catch (IOException e) {
            String errorMsg = "Erro na comunicação com o cliente " + clientAddress + ": " + e.getMessage();
            System.err.println(errorMsg);
            logMessage(errorMsg);
            if (e.getMessage().toLowerCase().contains("connection reset") || e.getMessage().toLowerCase().contains("socket closed")) {
                logMessage("Cliente " + clientAddress + " desconectado abruptamente.");
            }
        } finally {
            try {
                clientSocket.close();
                System.out.println("Conexão com " + clientAddress + " fechada.");
                logMessage("Conexão com " + clientAddress + " fechada.");
            } catch (IOException e) {
                String errorMsg = "Erro ao fechar socket do cliente " + clientAddress + ": " + e.getMessage();
                System.err.println(errorMsg);
                logMessage(errorMsg);
            }
        }
    }

    private synchronized void logMessage(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logger.println(timestamp + " - " + message);
        logger.flush(); // Garante que a mensagem seja escrita imediatamente no arquivo
    }
}