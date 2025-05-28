import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o endereço IP do servidor (ex: localhost ou 127.0.0.1): ");
        String ipServidor = scanner.nextLine();

        System.out.print("Digite a porta do servidor (ex: 12345): ");
        int portaServidor;
        try {
            portaServidor = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Porta inválida. Usando porta padrão 12345.");
            portaServidor = 12345;
        }

        try (Socket socket = new Socket(ipServidor, portaServidor);
             PrintWriter saidaServidor = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader entradaUsuario = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor " + ipServidor + ":" + portaServidor);
            System.out.println("Digite suas mensagens. Use 'exit' para sair.");

            // Thread para ler mensagens do servidor de forma assíncrona
            Thread threadRespostaServidor = new Thread(() -> {
                try {
                    String respostaDoServidor;
                    while ((respostaDoServidor = entradaServidor.readLine()) != null) {
                        System.out.println(respostaDoServidor); // Exibe a mensagem do servidor
                        if (respostaDoServidor.contains("Encerrando sua conexão")) {
                            break; // Sai do loop se o servidor confirmar o encerramento
                        }
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) { // Só mostra erro se o socket não foi fechado intencionalmente
                        System.err.println("Conexão com o servidor perdida: " + e.getMessage());
                    }
                } finally {
                    System.out.println("Thread de escuta do servidor finalizada.");
                }
            });
            threadRespostaServidor.start();

            String mensagemUsuario;
            System.out.print("Você: ");
            while (socket.isConnected() && !socket.isOutputShutdown() && (mensagemUsuario = entradaUsuario.readLine()) != null) {
                saidaServidor.println(mensagemUsuario);

                if ("exit".equalsIgnoreCase(mensagemUsuario.trim())) {
                    System.out.println("Solicitando encerramento da conexão...");
                    break; // Sai do loop de envio, a thread de escuta também será interrompida.
                }
                System.out.print("Você: ");
            }

            // Espera a thread de escuta terminar (ela terminará quando o socket fechar ou "exit")
            threadRespostaServidor.join(2000); // Espera no máximo 2 segundos

        } catch (java.net.ConnectException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            System.err.println("Verifique se o servidor está rodando no IP e porta corretos.");
        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            System.out.println("Cliente desconectado.");
        }
    }
}