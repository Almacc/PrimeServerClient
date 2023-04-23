import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class PrimeClientGUI {
    private JFrame frame;
    private JTextField inputField;
    private JButton checkButton;
    private JTextArea logTextArea;


    public static void main(String[] args) {
        PrimeClientGUI client = new PrimeClientGUI();
        client.initGUI();
    }

    private void initGUI() {
        frame = new JFrame("Client");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel inputLabel = new JLabel("Enter a number to check prime: ");
        inputField = new JTextField();
        checkButton = new JButton("Check");
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputLabel, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(checkButton, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(logTextArea);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText();

                try {
                    int number = Integer.parseInt(input);

                    Socket socket = new Socket("localhost", 9090);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    out.println(number);

                    String response = in.readLine();

                    if (response.equals("Yes")) {
                        log(number + " This number is prime.");
                        System.out.println(number + " This number is prime.");
                    } else {
                        log(number + " This number is is not prime.");
                        System.out.println(number + " This number is not prime.");
                    }

                    socket.close();
                } catch (NumberFormatException ex) {
                    log("Invalid input: " + input);
                } catch (UnknownHostException ex) {
                    log("Unknown host: localhost");
                } catch (IOException ex) {
                    log("Error communicating with server: " + ex.getMessage());
                }
            }
        });

        frame.setVisible(true);
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logTextArea.append(message + "\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
        });
    }
}