/**
 * Клас для представлення трикутника
 */
public class Triangle {
    private final Point p1;
    private final Point p2;
    private final Point p3;
    private final double side1;
    private final double side2;
    private final double side3;
    
    public Triangle(Point p1, Point p2, Point p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        
        // Обчислення довжин сторін
        this.side1 = p1.distanceTo(p2);
        this.side2 = p2.distanceTo(p3);
        this.side3 = p3.distanceTo(p1);
    }
    
    public Point getP1() {
        return p1;
    }
    
    public Point getP2() {
        return p2;
    }
    
    public Point getP3() {
        return p3;
    }
    
    /**
     * Перевіряє чи є трикутник тупокутним
     * Використовує теорему косинусів: якщо a² > b² + c², то кут протилежний до a - тупий
     */
    public boolean isObtuse() {
        double a2 = side1 * side1;
        double b2 = side2 * side2;
        double c2 = side3 * side3;
        
        // Перевіряємо всі три кути
        return (a2 > b2 + c2) || (b2 > a2 + c2) || (c2 > a2 + b2);
    }
    
    /**
     * Перевіряє чи є трикутник валідним (не вироджений)
     */
    public boolean isValid() {
        // Перевірка на вироджений трикутник
        double epsilon = 1e-10;
        return side1 > epsilon && side2 > epsilon && side3 > epsilon &&
               side1 + side2 > side3 && side2 + side3 > side1 && side3 + side1 > side2;
    }
    
    /**
     * Перевіряє чи має трикутник спільні вершини з іншим трикутником
     */
    public boolean hasCommonVertex(Triangle other) {
        return this.p1.equals(other.p1) || this.p1.equals(other.p2) || this.p1.equals(other.p3) ||
               this.p2.equals(other.p1) || this.p2.equals(other.p2) || this.p2.equals(other.p3) ||
               this.p3.equals(other.p1) || this.p3.equals(other.p2) || this.p3.equals(other.p3);
    }
    
    /**
     * Обчислює площу трикутника за формулою Герона
     */
    public double getArea() {
        double s = (side1 + side2 + side3) / 2;
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }
    
    @Override
    public String toString() {
        return String.format("x1=%.1f; y1=%.1f; x2=%.1f; y2=%.1f; x3=%.1f; y3=%.1f;",
                p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
    }
}
