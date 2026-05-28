import random

def generate_points_file(filename, count):
    """Генерує файл з випадковими точками"""
    with open(filename, 'w', encoding='utf-8') as f:
        for i in range(1, count + 1):
            x = random.randint(50, 1950)
            y = random.randint(50, 1950)
            f.write(f"Point {i}: x={x}; y={y};\n")
    print(f"Створено {filename} з {count} точками")

generate_points_file('points_500.txt', 500)
