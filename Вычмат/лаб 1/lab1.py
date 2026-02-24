import os, sys

def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False
    

def input_numbers(expected_length=None):

    while True:
        try:
            row = list(map(float, input().split()))
            
            if expected_length is not None and len(row) != expected_length:
                print(f"нужно ввести ровно {expected_length} чисел")
                continue
            return row
        except ValueError:
            print("введите только числа через пробел.")

def require_input():
    print("варианты ввода, напишите цифру: \n" + "1. клава \n" + "2. файл \n" + "3. выйти из программы")
    selection = input()
    if (is_number(selection)):
        return int(selection)
    return None


def from_file():
    file_name  = input("Введите название файла: ")
    if not (os.path.exists(file_name) and os.path.isfile(file_name)):
        print("Такого файла нет")
        return None
    
    
    with open(file_name, 'r') as f:
        dimension = int(f.readline())
        matrix = []
    
    for _ in range(dimension):
        row= list(map(float, f.readline().split()))
        matrix.append(row)
    return matrix



def from_keyboard():
    while (True):
        dimension = (input("Введите размерность матрицы до 20 включительно: "))
        if (is_number(dimension)):
            dimension = int(dimension) 
        else:
            print("введите число")
            continue
        if (dimension <= 20):
            break
        else:
            print("введите число меньше или равное 20")
            continue
    return read_matrix(dimension)

def read_matrix(dimension):
    matrix = []
    print("Введите строки матрицы, разделяя коэффициенты черех пробел")
    for i in range(dimension):
        row = input_numbers(expected_length=dimension)
        matrix.append(row)
    return matrix

def check_convergence(matrix):
    n = len(matrix)
    for i, row in enumerate(matrix):
        diag = abs(row[i])
        others = sum(abs(row[j] for j in range(n) if j != i))
        if (diag <= others):
            return False
    return True

def try_swap(matrix):
    n = len(matrix)

    for i in range(n):
        max_row = max(range(i, n), key=lambda r: abs(matrix[r][i]))
        matrix[i], matrix[max_row] = matrix[max_row], matrix[i]

    return matrix

def beginning():
    actions = {
        1: from_keyboard,
        2: from_file,
        3: lambda: None
    }
    while True:
        selection = None
        while selection not in actions:
            selection = require_input()
        matrix = actions[selection]()
        if(matrix is None):
            return None
        if (make_diagonally_dominant(matrix)):
            return matrix
        print("Матрица не прошла проверку, попробуйте ввести другую \n")
    


def make_diagonally_dominant(matrix):    
    max_attempts = len(matrix)
    for _ in range(max_attempts):
        if check_convergence(matrix):
            return True
        try_swap(matrix)
    print("не удалось добиться диагонального преобладания")
    return False

def request_initial_approximation(dimension):
    print(f"Напишите начальное приближение c размерностью {dimension}, формат ввода: x1 x2 x3 ... xn")
    row_x0 = input_numbers(expected_length=dimension)

def change_matrix(matrix, row_x0):
    n = len(matrix)
    for i, row in enumerate(matrix):
        x = sum(row[j]*row_x0[j] for j in range(len(row)) if j != i) / (row[i] if row[i] != 0 else 1)
        print(f'x' + str(i) + ' = ')

beginning()






