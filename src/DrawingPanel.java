import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Панель для відображення точок та трикутників
 */
public class DrawingPanel extends JPanel {
    private List<Point> points;
    private List<Triangle> triangles;
    private double minX, maxX, minY, maxY;
    private static final int PADDING = 50;
    private static final int POINT_SIZE = 8;
    
    // Кольори для сучасного інтерфейсу
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 40);
    private static final Color POINT_COLOR = new Color(255, 107, 107);
    private static final Color[] TRIANGLE_COLORS = {
        new Color(78, 205, 196, 180),
        new Color(255, 107, 107, 180),
        new Color(252, 196, 25, 180),
        new Color(72, 219, 251, 180),
        new Color(162, 155, 254, 180),
        new Color(255, 159, 243, 180)
    };
    private static final Color TEXT_COLOR = new Color(220, 220, 230);
    
    public DrawingPanel() {
        setBackground(BACKGROUND_COLOR);
        points = new ArrayList<>();
        triangles = new ArrayList<>();
    }
    
    public void setPoints(List<Point> points) {
        this.points = points;
        calculateBounds();
        repaint();
    }
    
    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
        repaint();
    }
    
    private void calculateBounds() {
        if (points.isEmpty()) {
            minX = minY = 0;
            maxX = maxY = 100;
            return;
        }
        
        minX = maxX = points.get(0).getX();
        minY = maxY = points.get(0).getY();
        
        for (Point p : points) {
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minY = Math.min(minY, p.getY());
            maxY = Math.max(maxY, p.getY());
        }
        
        // Додаємо відступи
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        minX -= rangeX * 0.1;
        maxX += rangeX * 0.1;
        minY -= rangeY * 0.1;
        maxY += rangeY * 0.1;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Увімкнення антиаліасингу для красивої графіки
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        if (points.isEmpty()) {
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            String message = "Завантажте файл з точками";
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g2.drawString(message, x, y);
            return;
        }
        
        int width = getWidth() - 2 * PADDING;
        int height = getHeight() - 2 * PADDING;
        
        // Малюємо трикутники
        for (int i = 0; i < triangles.size(); i++) {
            Triangle t = triangles.get(i);
            
            int x1 = PADDING + (int)((t.getP1().getX() - minX) / (maxX - minX) * width);
            int y1 = PADDING + (int)((maxY - t.getP1().getY()) / (maxY - minY) * height);
            int x2 = PADDING + (int)((t.getP2().getX() - minX) / (maxX - minX) * width);
            int y2 = PADDING + (int)((maxY - t.getP2().getY()) / (maxY - minY) * height);
            int x3 = PADDING + (int)((t.getP3().getX() - minX) / (maxX - minX) * width);
            int y3 = PADDING + (int)((maxY - t.getP3().getY()) / (maxY - minY) * height);
            
            int[] xPoints = {x1, x2, x3};
            int[] yPoints = {y1, y2, y3};
            
            // Заповнюємо трикутник
            Color color = TRIANGLE_COLORS[i % TRIANGLE_COLORS.length];
            g2.setColor(color);
            g2.fillPolygon(xPoints, yPoints, 3);
            
            // Малюємо контур
            g2.setColor(color.darker());
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(xPoints, yPoints, 3);
        }
        
        // Малюємо точки
        for (Point p : points) {
            int x = PADDING + (int)((p.getX() - minX) / (maxX - minX) * width);
            int y = PADDING + (int)((maxY - p.getY()) / (maxY - minY) * height);
            
            // Тінь
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillOval(x - POINT_SIZE/2 + 2, y - POINT_SIZE/2 + 2, POINT_SIZE, POINT_SIZE);
            
            // Точка
            g2.setColor(POINT_COLOR);
            g2.fillOval(x - POINT_SIZE/2, y - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);
            
            // Контур
            g2.setColor(POINT_COLOR.brighter());
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(x - POINT_SIZE/2, y - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);
            
            // Номер точки
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2.drawString(String.valueOf(p.getId()), x + POINT_SIZE, y - POINT_SIZE);
        }
        
        // Інформація
        g2.setColor(TEXT_COLOR);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.drawString("Точок: " + points.size(), 10, 20);
        g2.drawString("Трикутників: " + triangles.size(), 10, 40);
    }
}
