/**
 * Клас для представлення точки на площині
 */
public class Point {
    private final double x;
    private final double y;
    private final int id;
    
    public Point(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public int getId() {
        return id;
    }
    
    /**
     * Обчислює відстань до іншої точки
     */
    public double distanceTo(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    @Override
    public String toString() {
        return String.format("Point %d: x=%.1f; y=%.1f;", id, x, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return id == point.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
