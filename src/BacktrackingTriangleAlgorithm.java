import java.util.*;

/**
 * Алгоритм 2: Backtracking з оптимізацією
 * Складність часу: O(n³ × 2^(n/3)) в гіршому випадку (експоненційна)
 * Складність пам'яті: O(n) для стеку рекурсії + O(n³) для зберігання трикутників
 */
public class BacktrackingTriangleAlgorithm implements TriangleAlgorithm {
    
    private List<Triangle> bestSolution;
    private Set<Point> usedPoints;
    private List<Triangle> allObtuseTriangles;
    private int totalSteps;
    private int currentStep;
    private ProgressCallback callback;
    
    @Override
    public List<Triangle> findTriangles(List<Point> points, ProgressCallback callback) {
        this.callback = callback;
        this.bestSolution = new ArrayList<>();
        this.usedPoints = new HashSet<>();
        this.currentStep = 0;
        
        // Генеруємо всі можливі тупокутні трикутники
        allObtuseTriangles = new ArrayList<>();
        
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
                        callback.updateProgress((int)(processed * 50.0 / totalCombinations));
                    }
                }
            }
        }
        
        // Оцінюємо кількість кроків для backtracking
        totalSteps = Math.min(10000, allObtuseTriangles.size() * 10);
        
        // Запускаємо backtracking
        backtrack(new ArrayList<>(), 0);
        
        if (callback != null) {
            callback.updateProgress(100);
        }
        
        return bestSolution;
    }
    
    private void backtrack(List<Triangle> currentSolution, int startIndex) {
        // Оновлюємо прогрес
        currentStep++;
        if (callback != null && currentStep % 100 == 0) {
            int progress = 50 + (int)(currentStep * 50.0 / totalSteps);
            callback.updateProgress(Math.min(progress, 99));
        }
        
        // Якщо поточне рішення краще за найкраще - зберігаємо
        if (currentSolution.size() > bestSolution.size()) {
            bestSolution = new ArrayList<>(currentSolution);
        }
        
        // Pruning: якщо навіть використавши всі залишкові точки, не зможемо побити рекорд
        int remainingPoints = 0;
        for (Point p : usedPoints) {
            remainingPoints++;
        }
        int maxPossibleTriangles = (allObtuseTriangles.size() - remainingPoints) / 3;
        if (currentSolution.size() + maxPossibleTriangles <= bestSolution.size()) {
            return;
        }
        
        // Обмежуємо кількість кроків для великих наборів даних
        if (currentStep > totalSteps) {
            return;
        }
        
        // Пробуємо додати наступний трикутник
        for (int i = startIndex; i < allObtuseTriangles.size(); i++) {
            Triangle triangle = allObtuseTriangles.get(i);
            
            // Перевіряємо чи можна додати цей трикутник
            if (!usedPoints.contains(triangle.getP1()) &&
                !usedPoints.contains(triangle.getP2()) &&
                !usedPoints.contains(triangle.getP3())) {
                
                // Додаємо трикутник
                currentSolution.add(triangle);
                usedPoints.add(triangle.getP1());
                usedPoints.add(triangle.getP2());
                usedPoints.add(triangle.getP3());
                
                // Рекурсивний виклик
                backtrack(currentSolution, i + 1);
                
                // Відкат
                currentSolution.remove(currentSolution.size() - 1);
                usedPoints.remove(triangle.getP1());
                usedPoints.remove(triangle.getP2());
                usedPoints.remove(triangle.getP3());
            }
        }
    }
    
    @Override
    public String getName() {
        return "Алгоритм з поверненням (Backtracking)";
    }
    
    @Override
    public String getComplexity() {
        return "Час: O(n³ × 2^(n/3)), Пам'ять: O(n³)";
    }
}
