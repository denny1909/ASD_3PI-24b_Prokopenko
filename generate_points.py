import random

def generate_points_file(filename, count):
    """Генерує файл з випадковими точками"""
    with open(filename, 'w', encoding='utf-8') as f:
        for i in range(1, count + 1):
            x = random.randint(50, 1950)
            y = random.randint(50, 1950)
            f.write(f"Point {i}: x={x}; y={y};\n")
    print(f"Створено {filename} з {count} точками")

# Генеруємо файли
generate_points_file('points_1000.txt', 1000)
generate_points_file('points_3000.txt', 3000)
generate_points_file('points_5000.txt', 5000)

print("\nГотово! Створено 3 файли:")
print("- points_1000.txt (1000 точок)")
print("- points_3000.txt (3000 точок)")
print("- points_5000.txt (5000 точок)")
