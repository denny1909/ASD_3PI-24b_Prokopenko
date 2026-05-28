import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Головний клас GUI з сучасним інтерфейсом
 */
public class TriangleFinderGUI extends JFrame {
    
    // Компоненти інтерфейсу
    private DrawingPanel drawingPanel;
    private JTextArea infoArea;
    private JProgressBar progressBar;
    private JButton loadButton, runButton, saveButton;
    private JComboBox<String> algorithmComboBox;
    private JLabel statusLabel;
    private JLabel timeLabel;
    
    // Дані
    private List<Point> points;
    private List<Triangle> triangles;
    private String currentFilePath;
    
    // Алгоритми
    private final TriangleAlgorithm[] algorithms = {
        new GreedyTriangleAlgorithm(),
        new BacktrackingTriangleAlgorithm()
    };
    
    // Кольори для сучасного інтерфейсу
    private static final Color DARK_BG = new Color(30, 30, 40);
    private static final Color PANEL_BG = new Color(40, 40, 50);
    private static final Color ACCENT_COLOR = new Color(78, 205, 196);
    private static final Color TEXT_COLOR = new Color(220, 220, 230);
    private static final Color BUTTON_BG = new Color(60, 60, 70);
    
    public TriangleFinderGUI() {
        setTitle("Пошук тупокутних трикутників - Завдання №19");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Встановлюємо сучасний Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        setupLayout();
        
        points = new ArrayList<>();
        triangles = new ArrayList<>();
    }
    
    private void initializeComponents() {
        // Панель малювання
        drawingPanel = new DrawingPanel();
        drawingPanel.setPreferredSize(new Dimension(900, 800));
        
        // Текстова область для інформації
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setBackground(PANEL_BG);
        infoArea.setForeground(TEXT_COLOR);
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Прогрес-бар (як у іграх)
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBackground(PANEL_BG);
        progressBar.setBorderPainted(false);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Кнопки
        loadButton = createStyledButton(" Завантажити файл", ACCENT_COLOR);
        runButton = createStyledButton(" Запустити алгоритм", new Color(255, 107, 107));
        saveButton = createStyledButton(" Зберегти результат", new Color(252, 196, 25));
        
        loadButton.addActionListener(e -> loadFile());
        runButton.addActionListener(e -> runAlgorithm());
        saveButton.addActionListener(e -> saveResults());
        
        runButton.setEnabled(false);
        saveButton.setEnabled(false);
        
        // Вибір алгоритму
        String[] algorithmNames = new String[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            algorithmNames[i] = algorithms[i].getName();
        }
        algorithmComboBox = new JComboBox<>(algorithmNames);
        algorithmComboBox.setBackground(BUTTON_BG);
        algorithmComboBox.setForeground(TEXT_COLOR);
        algorithmComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Статус і час
        statusLabel = new JLabel("Готовий до роботи");
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        timeLabel = new JLabel("");
        timeLabel.setForeground(ACCENT_COLOR);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));
        
        // Ефект наведення
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(DARK_BG);
        
        // Ліва панель (малювання)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(DARK_BG);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        leftPanel.add(drawingPanel, BorderLayout.CENTER);
        
        // Права панель (контрольна панель)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(PANEL_BG);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setPreferredSize(new Dimension(450, 800));
        
        // Заголовок
        JLabel titleLabel = new JLabel("Панель керування");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(titleLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Секція завантаження
        addSection(rightPanel, "1. Завантаження даних", loadButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Секція вибору алгоритму
        JPanel algoPanel = new JPanel(new BorderLayout(10, 5));
        algoPanel.setBackground(PANEL_BG);
        algoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        algoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel algoLabel = new JLabel("Виберіть алгоритм:");
        algoLabel.setForeground(TEXT_COLOR);
        algoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        algoPanel.add(algoLabel, BorderLayout.NORTH);
        algoPanel.add(algorithmComboBox, BorderLayout.CENTER);
        
        addSection(rightPanel, "2. Налаштування", algoPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Секція запуску
        addSection(rightPanel, "3. Виконання", runButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Прогрес-бар
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(PANEL_BG);
        progressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        progressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        rightPanel.add(progressPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Секція збереження
        addSection(rightPanel, "4. Збереження результатів", saveButton);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Інформаційна область
        JLabel infoLabel = new JLabel("Інформація:");
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoLabel.setForeground(TEXT_COLOR);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(infoLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BG, 2));
        rightPanel.add(scrollPane);
        
        rightPanel.add(Box.createVerticalGlue());
        
        // Нижня панель зі статусом
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(PANEL_BG);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(timeLabel, BorderLayout.EAST);
        rightPanel.add(bottomPanel);
        
        // Додаємо панелі до вікна
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    private void addSection(JPanel parent, String title, JComponent component) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionLabel.setForeground(ACCENT_COLOR);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(sectionLabel);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(component);
    }
    
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fileChooser.setCurrentDirectory(new File("."));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            
            try {
                points = PointFileReader.readPoints(currentFilePath);
                drawingPanel.setPoints(points);
                drawingPanel.setTriangles(new ArrayList<>());
                triangles.clear();
                
                infoArea.setText("Завантажено точок: " + points.size() + "\n");
                infoArea.append("Файл: " + fileChooser.getSelectedFile().getName() + "\n");
                infoArea.append("\nТочки:\n");
                for (int i = 0; i < Math.min(10, points.size()); i++) {
                    infoArea.append(points.get(i).toString() + "\n");
                }
                if (points.size() > 10) {
                    infoArea.append("... та ще " + (points.size() - 10) + " точок\n");
                }
                
                statusLabel.setText("Файл завантажено успішно");
                runButton.setEnabled(true);
                saveButton.setEnabled(false);
                timeLabel.setText("");
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Помилка завантаження файлу: " + ex.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void runAlgorithm() {
        if (points.size() < 3) {
            JOptionPane.showMessageDialog(this,
                "Для побудови трикутників потрібно щонайменше 3 точки!",
                "Помилка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Вимикаємо кнопки під час виконання
        loadButton.setEnabled(false);
        runButton.setEnabled(false);
        saveButton.setEnabled(false);
        
        int selectedIndex = algorithmComboBox.getSelectedIndex();
        TriangleAlgorithm algorithm = algorithms[selectedIndex];
        
        statusLabel.setText("Виконується: " + algorithm.getName());
        progressBar.setValue(0);
        
        // Запускаємо в окремому потоці
        new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                
                // Callback для оновлення прогресу
                ProgressCallback callback = percentage -> {
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(percentage);
                        progressBar.setString("Завантаження: " + percentage + "%");
                    });
                };
                
                triangles = algorithm.findTriangles(points, callback);
                
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                
                // Оновлюємо UI в головному потоці
                SwingUtilities.invokeLater(() -> {
                    drawingPanel.setTriangles(triangles);
                    
                    infoArea.setText("=== РЕЗУЛЬТАТИ ===\n");
                    infoArea.append("Алгоритм: " + algorithm.getName() + "\n");
                    infoArea.append("Складність: " + algorithm.getComplexity() + "\n");
                    infoArea.append("Час виконання: " + executionTime + " мс\n");
                    infoArea.append("Знайдено трикутників: " + triangles.size() + "\n\n");
                    
                    infoArea.append("Трикутники:\n");
                    for (int i = 0; i < Math.min(5, triangles.size()); i++) {
                        infoArea.append((i + 1) + ". " + triangles.get(i).toString() + "\n");
                    }
                    if (triangles.size() > 5) {
                        infoArea.append("... та ще " + (triangles.size() - 5) + " трикутників\n");
                    }
                    
                    statusLabel.setText("Виконано успішно");
                    timeLabel.setText("⏱ " + executionTime + " мс");
                    progressBar.setValue(100);
                    progressBar.setString("Завершено");
                    
                    loadButton.setEnabled(true);
                    runButton.setEnabled(true);
                    saveButton.setEnabled(true);
                });
                
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Помилка виконання алгоритму: " + ex.getMessage(),
                        "Помилка", JOptionPane.ERROR_MESSAGE);
                    
                    loadButton.setEnabled(true);
                    runButton.setEnabled(true);
                    statusLabel.setText("Помилка виконання");
                    progressBar.setValue(0);
                });
                ex.printStackTrace();
            }
        }).start();
    }
    
    private void saveResults() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fileChooser.setSelectedFile(new File("results.txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                int selectedIndex = algorithmComboBox.getSelectedIndex();
                TriangleAlgorithm algorithm = algorithms[selectedIndex];
                long executionTime = Long.parseLong(timeLabel.getText().replaceAll("[^0-9]", ""));
                
                PointFileReader.writeResults(
                    fileChooser.getSelectedFile().getAbsolutePath(),
                    triangles,
                    algorithm.getName(),
                    executionTime
                );
                
                JOptionPane.showMessageDialog(this,
                    "Результати збережено успішно!",
                    "Успіх", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Помилка збереження файлу: " + ex.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TriangleFinderGUI gui = new TriangleFinderGUI();
            gui.setVisible(true);
        });
    }
}
