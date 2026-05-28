import java.util.*;

/**
 * Алгоритм 1: Жадібний підхід з пріоритетом за площею
 * Складність часу: O(n³) для генерації всіх трикутників + O(m log m) для сортування, де m - кількість тупокутних трикутників
 * Складність пам'яті: O(n³) в гіршому випадку для зберігання всіх трикутників
 */
public class GreedyTriangleAlgorithm implements TriangleAlgorithm {
    
    @Override
    public List<Triangle> findTriangles(List<Point> points, ProgressCallback callback) {
        List<Triangle> result = new ArrayList<>();
        Set<Point> usedPoints = new HashSet<>();
        
        // Генеруємо всі можливі тупокутні трикутники
        List<Triangle> allObtuseTriangles = new ArrayList<>();
        int totalCombinations = points.size() * (points.size() - 1) * (points.size() - 2) / 6;
        int processed = 0;
        
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                for (int k = j + 1; k < points.size(); k++) {
                    Triangle triangle = new Triangle(points.get(i), points.get(j), points.get(k));
                    
                    if (triangle.isValid() && triangle.isObtuse()) {
                        allObtuseTriangles.add(triangle);
                    }
                    
                    processed++;
                    if (callback != null && processed % 100 == 0) {
                        callback.updateProgress((int)(processed * 100.0 / totalCombinations));
                    }
                }
            }
        }
        
        // Сортуємо за площею (від більшої до меншої) для жадібного вибору
        allObtuseTriangles.sort((t1, t2) -> Double.compare(t2.getArea(), t1.getArea()));
        
        // Жадібно вибираємо трикутники без спільних вершин
        for (Triangle triangle : allObtuseTriangles) {
            if (!usedPoints.contains(triangle.getP1()) &&
                !usedPoints.contains(triangle.getP2()) &&
                !usedPoints.contains(triangle.getP3())) {
                
                result.add(triangle);
                usedPoints.add(triangle.getP1());
                usedPoints.add(triangle.getP2());
                usedPoints.add(triangle.getP3());
            }
        }
        
        if (callback != null) {
            callback.updateProgress(100);
        }
        
        return result;
    }
    
    @Override
    public String getName() {
        return "Жадібний алгоритм (за площею)";
    }
    
    @Override
    public String getComplexity() {
        return "Час: O(n³ + m log m), Пам'ять: O(n³)";
    }
}
