import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Клас для читання точок з файлу
 */
public class PointFileReader {
    
    /**
     * Допоміжний метод для повторення символу (сумісність з Java 8)
     */
    private static String repeatChar(char ch, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
    
    /**
     * Зчитує точки з файлу
     * Формат: Point 1: x=22; y=130;
     */
    public static List<Point> readPoints(String filename) throws IOException {
        List<Point> points = new ArrayList<>();
        
        // Використовуємо BufferedReader для ефективного читання
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Pattern pattern = Pattern.compile("Point\\s+(\\d+):\\s*x\\s*=\\s*([\\d.]+)\\s*;\\s*y\\s*=\\s*([\\d.]+)\\s*;");
            
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                
                if (matcher.find()) {
                    int id = Integer.parseInt(matcher.group(1));
                    double x = Double.parseDouble(matcher.group(2));
                    double y = Double.parseDouble(matcher.group(3));
                    
                    points.add(new Point(id, x, y));
                }
            }
        }
        
        return points;
    }
    
    /**
     * Записує результати у файл
     */
    public static void writeResults(String filename, List<Triangle> triangles, 
                                    String algorithmName, long executionTime) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(repeatChar('=', 80));
            writer.println("РЕЗУЛЬТАТИ ПОШУКУ ТУПОКУТНИХ ТРИКУТНИКІВ");
            writer.println(repeatChar('=', 80));
            writer.println();
            writer.println("Алгоритм: " + algorithmName);
            writer.println("Час виконання: " + executionTime + " мс");
            writer.println("Знайдено трикутників: " + triangles.size());
            writer.println();
            writer.println(repeatChar('-', 80));
            writer.println();
            
            for (int i = 0; i < triangles.size(); i++) {
                writer.println("Triangle " + (i + 1) + ": " + triangles.get(i).toString());
            }
            
            writer.println();
            writer.println(repeatChar('-', 80));
            writer.println("Вершини трикутників:");
            writer.println();
            
            Set<Point> usedPoints = new HashSet<>();
            for (Triangle t : triangles) {
                usedPoints.add(t.getP1());
                usedPoints.add(t.getP2());
                usedPoints.add(t.getP3());
            }
            
            List<Point> sortedPoints = new ArrayList<>(usedPoints);
            sortedPoints.sort(Comparator.comparingInt(Point::getId));
            
            for (Point p : sortedPoints) {
                writer.println(p.toString());
            }
        }
    }
}
