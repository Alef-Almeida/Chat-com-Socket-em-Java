import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Servidor {
    private static final String LOG_FILE = "chat_log.txt";
    private static PrintWriter logger;

    public static void main(String[] args) {
        final int PORTA = 12345; // Porta padrão

        try {
            // Inicializa o FileWriter para o log, em modo append (true)
            logger = new PrintWriter(new FileWriter(LOG_FILE, true), true);
            logInitialServerMessage("Servidor iniciado na porta " + PORTA);
            System.out.println("Servidor iniciado. Logando em: " + LOG_FILE);
            System.out.println("Aguardando conexões na porta " + PORTA + "...");

            try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
                while (true) { // Loop para aceitar múltiplos clientes
                    Socket clientSocket = serverSocket.accept();
                    // Cria uma nova thread para lidar com o cliente
                    ClientHandler clientHandler = new ClientHandler(clientSocket, logger);
                    new Thread(clientHandler).start();
                }
            }
        } catch (IOException e) {
            String errorMsg = "Erro crítico no servidor: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            if (logger != null) {
                logInitialServerMessage(errorMsg + " - StackTrace: " + e);
            }
        } finally {
            if (logger != null) {
                logInitialServerMessage("Servidor encerrado.");
                logger.close();
            }
        }
    }

    private static synchronized void logInitialServerMessage(String message) {
        if (logger == null) return; // Se o logger não pôde ser inicializado
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logger.println(timestamp + " [SERVIDOR] - " + message);
        logger.flush();
    }
}