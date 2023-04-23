import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrimeServerGUI {
    private JFrame frame;
    private JTextArea logTextArea;

    public static void main(String[] args) {
        PrimeServerGUI server = new PrimeServerGUI();
        server.initGUI();
        server.startServer();
    }

    private void initGUI() {
        frame = new JFrame("Server");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void startServer() {

        new Thread(() -> {
            try {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                String formattedDate = dateFormat.format(currentDate);
                ServerSocket serverSocket = new ServerSocket(9090);
                log("Server Started at " + formattedDate);

                while (true) {
                    Socket socket = serverSocket.accept();
                    log("Client connected.");

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String inputLine = in.readLine();
                    int number = Integer.parseInt(inputLine);

                    log("Received request to check if " + number + " is prime.");

                    boolean isPrime = isPrime(number);

                    if (isPrime) {
                        out.println("Yes");
                        log("Number Entered is "+ number + ", This number is prime.");
                        System.out.println("Number Entered is "+ number + ", This number is prime.");
                    } else {
                        out.println("No");
                        log("Number Entered is "+ number + ", This number is not prime.");
                        System.out.println("Number Entered is "+ number + ", This number is not prime.");
                    }

                    socket.close();
                    log("Client disconnected.");
                }
            } catch (IOException e) {
                log("Error starting server: " + e.getMessage());
            }
        }).start();
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logTextArea.append(message + "\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        });
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }
}