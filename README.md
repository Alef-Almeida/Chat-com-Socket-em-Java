# Aplicação de Chat Cliente-Servidor em Java

Esta aplicação implementa um sistema de chat simples utilizando sockets Java. O servidor é capaz de lidar com múltiplos clientes simultaneamente, e todas as conversas são registradas em um arquivo de log no servidor.

## Funcionalidades Principais

* **Comunicação Cliente-Servidor:** Os clientes podem se conectar ao servidor para enviar e receber mensagens.
* **Múltiplos Clientes Concorrentes:** O servidor utiliza threads para gerenciar várias conexões de clientes ao mesmo tempo, permitindo que diferentes usuários interajam independentemente.
* **Log de Mensagens:** Todas as mensagens trocadas entre clientes e o servidor, bem como eventos importantes (conexões, desconexões, erros), são salvos em um arquivo de texto (`chat_log.txt`) no diretório do servidor, com data e hora.
* **Configuração de Conexão pelo Cliente:** O programa cliente solicita ao usuário o endereço IP e o número da porta do servidor ao qual deseja se conectar.
* **Encerramento de Conexão:** Os clientes podem encerrar sua conexão de forma limpa digitando o comando `exit`.

## Tecnologias Utilizadas

* **Java (JDK 8 ou superior):** Linguagem de programação principal utilizada para desenvolver tanto o servidor quanto o cliente.
* **Sockets Java (`java.net.Socket`, `java.net.ServerSocket`):** Para estabelecer a comunicação baseada em TCP/IP entre o cliente e o servidor.
* **Threads (`java.lang.Thread`, `java.lang.Runnable`):** Utilizadas no lado do servidor para permitir o tratamento concorrente de múltiplos clientes.
* **Java I/O Streams (`java.io.*`):** Para o envio e recebimento de dados (mensagens) através das conexões de socket e para a escrita no arquivo de log.
* **Java Collections (implícito):** Embora não proeminente, estruturas de dados podem ser usadas internamente (por exemplo, se o servidor precisasse manter uma lista de clientes ativos, o que não foi explicitamente implementado no código fornecido mas é uma extensão comum).

## Estrutura dos Arquivos do Projeto

* `Servidor.java`: Contém o código fonte do programa servidor.
* `ClientHandler.java`: Classe auxiliar utilizada pelo servidor para gerenciar a comunicação com cada cliente conectado em uma thread separada.
* `Cliente.java`: Contém o código fonte do programa cliente.
* `chat_log.txt`: Arquivo de texto gerado automaticamente pelo servidor para armazenar o histórico de mensagens e eventos. (Este arquivo aparecerá no mesmo diretório do `Servidor.class` após a primeira execução do servidor).

## Como Compilar o Projeto

Para compilar os arquivos Java, você precisará ter o JDK (Java Development Kit) instalado e configurado no seu sistema.

1.  Abra um terminal ou prompt de comando.
2.  Navegue até o diretório onde você salvou os arquivos `.java` (`Servidor.java`, `ClientHandler.java`, `Cliente.java`).
3.  Execute o seguinte comando de compilação:

    ```bash
    javac Servidor.java ClientHandler.java Cliente.java
    ```
    Ou, se todos os arquivos `.java` estiverem sozinhos no diretório:
    ```bash
    javac *.java
    ```
    Isso gerará os arquivos `.class` correspondentes (`Servidor.class`, `ClientHandler.class`, `Cliente.class`) no mesmo diretório.

## Como Executar a Aplicação

Após a compilação bem-sucedida, siga os passos abaixo:

### 1. Iniciar o Servidor
   * No terminal (ou em um novo, se preferir), certifique-se de estar no diretório onde os arquivos `.class` foram gerados.
   * Execute o comando:
     ```bash
     java Servidor
     ```
   * O servidor exibirá mensagens indicando que foi iniciado, em qual porta está escutando (por padrão, 12345), e o nome do arquivo de log. Ele ficará aguardando conexões de clientes.

### 2. Iniciar o(s) Cliente(s)
   * Abra uma **nova janela** do terminal para cada cliente que você deseja conectar.
   * Navegue até o diretório dos arquivos `.class`.
   * Execute o comando:
     ```bash
     java Cliente
     ```
   * O programa cliente solicitará:
     1.  **O endereço IP do servidor:**
         * Se o servidor estiver rodando na mesma máquina que o cliente, você pode digitar `localhost` ou `127.0.0.1`.
         * Se o servidor estiver em outra máquina na mesma rede, digite o endereço IP local dessa máquina.
     2.  **A porta do servidor:** Por padrão, o servidor usa a porta `12345` (conforme definido no código).
   * Após fornecer as informações e conectar-se com sucesso, você verá uma mensagem de boas-vindas do servidor.
   * Digite suas mensagens e pressione Enter para enviá-las.
   * Para desconectar, digite `exit` e pressione Enter.

Você pode iniciar vários clientes, cada um em sua própria janela de terminal, para testar a capacidade do servidor de lidar com múltiplas conexões. Todas as interações serão visíveis no console do servidor e registradas no arquivo `chat_log.txt`.
