import java.util.List;

/**
 * Інтерфейс для алгоритмів пошуку тупокутних трикутників
 */
public interface TriangleAlgorithm {
    /**
     * Знаходить максимальну кількість тупокутних трикутників без спільних вершин
     * @param points список точок
     * @param callback callback для оновлення прогресу (може бути null)
     * @return список знайдених трикутників
     */
    List<Triangle> findTriangles(List<Point> points, ProgressCallback callback);
    
    /**
     * Повертає назву алгоритму
     */
    String getName();
    
    /**
     * Повертає оцінку складності алгоритму
     */
    String getComplexity();
}
