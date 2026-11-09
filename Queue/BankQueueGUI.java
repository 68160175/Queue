package Queue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BankQueueGUI extends JFrame {

    private QueueManager manager;

    private JTextField nameField;
    private JComboBox<String> serviceCombo;

    private JTable queueTable;
    private DefaultTableModel queueModel;

    private JLabel totalLabel;
    private JLabel waitingLabel;
    private JLabel completedLabel;

    private JLabel currentQueueLabel;
    private JLabel currentNameLabel;
    private JLabel currentServiceLabel;

    public BankQueueGUI() {

        manager = new QueueManager();

        setTitle("Bank Queue Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // สีของโปรแกรม
        Color darkPurple = new Color(37, 35, 59);
        Color purple = new Color(101, 82, 163);
        Color lightBackground = new Color(245, 243, 248);
        Color green = new Color(49, 155, 118);

        getContentPane().setBackground(lightBackground);

        // ===== HEADER =====

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(darkPurple);
        header.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel title = new JLabel("BANK QUEUE SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitle = new JLabel("Customer Service Management");
        subtitle.setForeground(new Color(220, 215, 235));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ===== MAIN PANEL =====

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(lightBackground);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20));

        // ===== STATISTICS =====

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);

        totalLabel = new JLabel("0", JLabel.CENTER);
        waitingLabel = new JLabel("0", JLabel.CENTER);
        completedLabel = new JLabel("0", JLabel.CENTER);

        statsPanel.add(createStatCard("Total Customers", totalLabel, purple));
        statsPanel.add(createStatCard("Waiting Queue", waitingLabel,
                new Color(211, 140, 57)));
        statsPanel.add(createStatCard("Completed", completedLabel, green));

        // ===== CURRENT CUSTOMER =====

        JPanel currentPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        currentPanel.setBackground(Color.WHITE);
        currentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 220, 235)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel currentTitle = new JLabel("NOW SERVING");
        currentTitle.setFont(new Font("Arial", Font.BOLD, 14));

        currentQueueLabel = new JLabel("No Queue");
        currentQueueLabel.setForeground(purple);
        currentQueueLabel.setFont(new Font("Arial", Font.BOLD, 27));

        currentNameLabel = new JLabel("Customer: -");
        currentNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        currentServiceLabel = new JLabel("Service: -");
        currentServiceLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel customerInfo = new JPanel(new GridLayout(2, 1));
        customerInfo.setOpaque(false);
        customerInfo.add(currentNameLabel);
        customerInfo.add(currentServiceLabel);

        currentPanel.add(currentTitle);
        currentPanel.add(currentQueueLabel);
        currentPanel.add(customerInfo);

        // ===== LEFT PANEL =====

        JPanel leftPanel = new JPanel(new BorderLayout(10, 15));
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setOpaque(false);

        JPanel registerPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Register Customer"),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel nameLabel = new JLabel("Customer Name");

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel serviceLabel = new JLabel("Select Service");

        String[] services = {
                "Deposit",
                "Withdraw",
                "Open Saving Account",
                "Request information"
        };

        serviceCombo = new JComboBox<>(services);

        serviceCombo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        nameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        serviceCombo.setFont(new Font("Tahoma", Font.PLAIN, 14));

        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        serviceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JButton registerButton = createButton(
                "Register Customer", purple);

        registerButton.addActionListener(e -> registerCustomer());

        registerPanel.add(nameLabel);
        registerPanel.add(nameField);
        registerPanel.add(serviceLabel);
        registerPanel.add(serviceCombo);
        registerPanel.add(registerButton);

        // Action buttons

        JPanel actionPanel = new JPanel(new GridLayout(0, 1, 8, 10));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Queue Actions"),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JButton callButton = createButton("Call Next Queue", green);
        JButton completeButton = createButton(
                "Complete Service", purple);
        JButton cancelButton = createButton(
                "Cancel Selected Queue", new Color(190, 70, 70));
        JButton historyButton = createButton(
                "View History", darkPurple);

        callButton.addActionListener(e -> callNextQueue());
        completeButton.addActionListener(e -> completeService());
        cancelButton.addActionListener(e -> cancelSelectedQueue());
        historyButton.addActionListener(e -> showHistory());

        actionPanel.add(callButton);
        actionPanel.add(completeButton);
        actionPanel.add(cancelButton);
        actionPanel.add(historyButton);

        leftPanel.add(registerPanel, BorderLayout.NORTH);
        leftPanel.add(actionPanel, BorderLayout.CENTER);

        // ===== RIGHT PANEL =====

        JPanel rightPanel = new JPanel(new BorderLayout(10, 15));
        rightPanel.setOpaque(false);

        String[] columns = {
                "Queue No.",
                "Customer Name",
                "Service",
                "Status"
        };

        queueModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        queueTable = new JTable(queueModel);
        queueTable.setRowHeight(30);
        queueTable.setFont(new Font("Arial", Font.PLAIN, 13));
        queueTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(queueTable);

        JPanel queuePanel = new JPanel(new BorderLayout(5, 10));
        queuePanel.setBackground(Color.WHITE);
        queuePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Waiting Queue"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        queuePanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.add(currentPanel, BorderLayout.NORTH);
        rightPanel.add(queuePanel, BorderLayout.CENTER);

        // ===== LAYOUT =====

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setOpaque(false);
        topPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        refreshData();

        setVisible(true);
    }

    // ===== CREATE STAT CARD =====

    private JPanel createStatCard(
            String title, JLabel value, Color color) {

        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 220, 235)),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        titleLabel.setForeground(Color.GRAY);

        value.setFont(new Font("Arial", Font.BOLD, 25));
        value.setForeground(color);

        panel.add(titleLabel);
        panel.add(value);

        return panel;
    }

    // ===== CREATE BUTTON =====

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // ===== REGISTER CUSTOMER =====

    private void registerCustomer() {

        String name = nameField.getText().trim();
        String service = (String) serviceCombo.getSelectedItem();

        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter customer name!"
            );

            return;
        }

        Customer customer = manager.register(name, service);

        JOptionPane.showMessageDialog(
                this,
                "Registration Successful!\n\n" +
                        "Queue Number: " + customer.getQueueNumber() +
                        "\nName: " + customer.getName() +
                        "\nService: " + customer.getServiceType()
        );

        nameField.setText("");

        refreshData();
    }

    // ===== CALL NEXT QUEUE =====

    private void callNextQueue() {

        Customer customer = manager.callNext();

        if (customer == null) {

            if (manager.getCurrentCustomer() != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please complete current service first!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No customers in queue!"
                );
            }

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Now Serving: " + customer.getQueueNumber() +
                        "\nCustomer: " + customer.getName()
        );

        refreshData();
    }

    // ===== COMPLETE SERVICE =====

    private void completeService() {

        Customer customer = manager.getCurrentCustomer();

        if (customer == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No customer is currently being served!"
            );

            return;
        }

        manager.completeService();

        JOptionPane.showMessageDialog(
                this,
                "Service Completed!\n" +
                        "Queue Number: " + customer.getQueueNumber()
        );

        refreshData();
    }

    // ===== CANCEL QUEUE =====

    private void cancelSelectedQueue() {

        int row = queueTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a queue first!"
            );

            return;
        }

        String queueNumber = queueModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Cancel queue " + queueNumber + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            boolean success = manager.cancelQueue(queueNumber);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Queue cancelled successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to cancel queue!"
                );
            }

            refreshData();
        }
    }

    // ===== SHOW HISTORY =====

    private void showHistory() {

        String[] columns = {
                "Queue No.",
                "Name",
                "Service",
                "Status"
        };

        DefaultTableModel model = new DefaultTableModel(
                columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Customer customer : manager.getHistoryList()) {

            model.addRow(new Object[]{
                    customer.getQueueNumber(),
                    customer.getName(),
                    customer.getServiceType(),
                    customer.getStatus()
            });
        }

        JTable historyTable = new JTable(model);
        historyTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(historyTable);

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Customer History",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    // ===== REFRESH DATA =====

    private void refreshData() {

        queueModel.setRowCount(0);

        for (Customer customer : manager.getWaitingQueue()) {

            queueModel.addRow(new Object[]{
                    customer.getQueueNumber(),
                    customer.getName(),
                    customer.getServiceType(),
                    customer.getStatus()
            });
        }

        totalLabel.setText(
                String.valueOf(manager.getTotalCount()));

        waitingLabel.setText(
                String.valueOf(manager.getWaitingCount()));

        completedLabel.setText(
                String.valueOf(manager.getCompletedCount()));

        Customer current = manager.getCurrentCustomer();

        if (current != null) {

            currentQueueLabel.setText(current.getQueueNumber());

            currentNameLabel.setText(
                    "Customer: " + current.getName());

            currentServiceLabel.setText(
                    "Service: " + current.getServiceType());

        } else {

            currentQueueLabel.setText("No Queue");
            currentNameLabel.setText("Customer: -");
            currentServiceLabel.setText("Service: -");
        }
    }
}