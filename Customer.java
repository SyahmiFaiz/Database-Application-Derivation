import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Customer {

    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable bikeTable;
    private JTextArea bikeDetailsArea;

    public Customer() {
        initialize();
        fetchData(); // Fetch data from the server
    }

    private void initialize() {
        frame = new JFrame("Bike Rental - Customer Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.getContentPane().add(mainPanel);

        JLabel titleLabel = new JLabel("Available Bikes", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"Bike ID", "Bike Model"}, 0); // Removed "Plate Number"
        bikeTable = new JTable(tableModel);
        bikeTable.setFont(new Font("Arial", Font.PLAIN, 16));
        bikeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bikeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16)); // Bold font for table header
        bikeTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Adjust column widths
        bikeTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        bikeTable.getSelectionModel().addListSelectionListener(e -> displayBikeDetails());
        JScrollPane tableScrollPane = new JScrollPane(bikeTable);
        centerPanel.add(tableScrollPane, BorderLayout.WEST);

        bikeDetailsArea = new JTextArea();
        bikeDetailsArea.setEditable(false);
        bikeDetailsArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane detailsScrollPane = new JScrollPane(bikeDetailsArea);
        centerPanel.add(detailsScrollPane, BorderLayout.CENTER);

        JButton rentButton = new JButton("Rent Selected Bike");
        rentButton.setFont(new Font("Arial", Font.BOLD, 18));
        rentButton.setBackground(Color.GREEN);
        rentButton.setForeground(Color.WHITE);
        rentButton.addActionListener(e -> {
			try {
				rentSelectedBike();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        mainPanel.add(rentButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void fetchData() {
        try {
            URL url = new URL("http://localhost/RESTFULCategory/FetchAllBikes.php"); // Changed to FetchAllBikes.php
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Parse JSON response into an ArrayList of bike entries
            ArrayList<BikeEntry> bikeEntries = parseJsonResponse(response.toString());

            // Clear current table data
            tableModel.setRowCount(0);

            // Populate table with fetched data
            for (BikeEntry entry : bikeEntries) {
                Object[] rowData = {entry.getBikeID(), entry.getModel()};
                tableModel.addRow(rowData);
            }

            conn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching data: " + ex.getMessage());
        }
    }

    private ArrayList<BikeEntry> parseJsonResponse(String jsonResponse) {
        ArrayList<BikeEntry> bikeEntries = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int bikeID = obj.getInt("BikeID");
                String model = obj.getString("BikeModel");
                BikeEntry entry = new BikeEntry(bikeID, model);
                bikeEntries.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle JSON parsing exception if necessary
        }

        return bikeEntries;
    }

    private void displayBikeDetails() {
        int selectedRow = bikeTable.getSelectedRow();
        if (selectedRow != -1) {
            String bikeDetails = "Bike ID: " + tableModel.getValueAt(selectedRow, 0) + "\n"
                    + "Model: " + tableModel.getValueAt(selectedRow, 1); // Removed "Plate Number"
            bikeDetailsArea.setText(bikeDetails);
        }
    }

    private void rentSelectedBike() throws JSONException {
        int selectedRow = bikeTable.getSelectedRow();
        if (selectedRow != -1) {
            int bikeID = (int) tableModel.getValueAt(selectedRow, 0); // Get selected bike ID
            String bikeModel = (String) tableModel.getValueAt(selectedRow, 1); // Get selected bike model

            // Perform the rental action (e.g., show confirmation dialog)
            int option = JOptionPane.showConfirmDialog(frame,
                    "Do you want to rent:\nBike ID: " + bikeID + "\nModel: " + bikeModel,
                    "Rent Bike Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // Send a request to save the rental record in the database
                saveRentalRecord(bikeID, bikeModel);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a bike to rent.", "Rent Bike", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveRentalRecord(int bikeID, String bikeModel) throws JSONException {
        try {
            URL url = new URL("http://localhost/RESTFULCategory/SaveBooking.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject postData = new JSONObject();
            postData.put("BikeID", bikeID);
            postData.put("BikeModel", bikeModel);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = postData.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response data
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    int rentalID = jsonResponse.getInt("RentalID"); // Retrieve RentalID from response
                    JOptionPane.showMessageDialog(frame, "Bike rented successfully! RentalID: " + rentalID);
                    fetchData(); // Refresh the available bikes list
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to rent bike. Please try again.");
            }

            conn.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving rental record: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Customer());
    }

    // Inner class to represent bike entries
    private static class BikeEntry {
        private final int bikeID;
        private final String model;

        public BikeEntry(int bikeID, String model) {
            this.bikeID = bikeID;
            this.model = model;
        }

        public int getBikeID() {
            return bikeID;
        }

        public String getModel() {
            return model;
        }
    }
}