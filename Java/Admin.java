import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Admin {

    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable rentalTable;
    private JTextField bookingIdTextField;

    public Admin() {
        initialize();
        SwingUtilities.invokeLater(this::fetchData); // Initial fetch of data when the application starts
    }

    private void initialize() {
        frame = new JFrame("Bike Rental Admin Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.getContentPane().add(mainPanel);

        JLabel headerLabel = new JLabel("Bike Rentals Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        String[] columnNames = {"Rental ID", "Bike ID", "Rental Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        rentalTable = new JTable(tableModel);
        rentalTable.setFont(new Font("Arial", Font.PLAIN, 16));
        rentalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        rentalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(rentalTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JLabel statusChangeLabel = new JLabel("Enter Rental ID to Change Status:");
        statusChangeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(statusChangeLabel);

        bookingIdTextField = new JTextField(20);
        bookingIdTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(bookingIdTextField);

        JButton changeStatusButton = new JButton("Change Status");
        changeStatusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> changeStatus());
            }
        });
        changeStatusButton.setFont(new Font("Arial", Font.BOLD, 16));
        changeStatusButton.setBackground(Color.GREEN);
        changeStatusButton.setForeground(Color.WHITE);
        bottomPanel.add(changeStatusButton);

        frame.setVisible(true);
    }

    private void changeStatus() {
        String bookingId = bookingIdTextField.getText().trim();
        if (!bookingId.isEmpty()) {
            try {
                URL url = new URL("http://localhost/RESTFULCategory/SaveToDB.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                String urlParameters = "bookingId=" + bookingId;

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = urlParameters.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        JOptionPane.showMessageDialog(frame, "Status Changed Successfully!");
                        fetchData(); // Refresh data after status change
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: Received HTTP response code " + responseCode);
                }

                conn.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a Booking ID.");
        }
    }

    private void fetchData() {
        try {
            URL url = new URL("http://localhost/RESTFULCategory/FetchFromDB.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            ArrayList<RentalEntry> rentalEntries = parseJsonResponse(response.toString());

            SwingUtilities.invokeLater(() -> {
                tableModel.setRowCount(0);
                for (RentalEntry entry : rentalEntries) {
                    Object[] rowData = {entry.getRentalID(), entry.getBikeID(), entry.getRentalStatusAsString()};
                    tableModel.addRow(rowData);
                }
            });

            conn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + ex.getMessage());
        }
    }

    private ArrayList<RentalEntry> parseJsonResponse(String jsonResponse) {
        ArrayList<RentalEntry> rentalEntries = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int rentalID = obj.getInt("RentalID");
                int bikeID = obj.getInt("BikeID");
                int rentalStatus = obj.getInt("RentalStatus");
                RentalEntry entry = new RentalEntry(rentalID, bikeID, rentalStatus);
                rentalEntries.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error parsing data: " + e.getMessage());
        }

        return rentalEntries;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Admin::new);
    }
}

class RentalEntry {
    private int rentalID;
    private int bikeID;
    private int rentalStatus;

    public RentalEntry(int rentalID, int bikeID, int rentalStatus) {
        this.rentalID = rentalID;
        this.bikeID = bikeID;
        this.rentalStatus = rentalStatus;
    }

    public int getRentalID() {
        return rentalID;
    }

    public int getBikeID() {
        return bikeID;
    }

    public String getRentalStatusAsString() {
        return rentalStatus == 1 ? "Rented" : "Returned";
    }
}