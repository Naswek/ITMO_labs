import os, sys

RED = "\033[31m"
GREEN = "\033[32m"
RESET = "\033[0m"
YELLOW = "\033[33m"

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
                print(f"{RED}нужно ввести ровно {expected_length} чисел{RESET}")
                continue
            return row
        except ValueError:
            print(f"{RED}введите только числа через пробел.{RESET}")

def require_input(to_float):
    selection = input()
    if (is_number(selection)):
        if to_float:
            return float(selection)
        return int(selection)
    return None


def from_file():
    file_name  = input("Введите название файла: ")
    if not (os.path.exists(file_name) and os.path.isfile(file_name)):
        print(f"{RED}Такого файла нет{RESET}")
        return None
    
    
    with open(file_name, 'r') as f:
        dimension = int(f.readline())
        matrix = []
    
        for _ in range(dimension):
            row= list(map(float, f.readline().split()))
            if len(row) != dimension + 1:
                print(f"{RED}Неверное количество элементов в строке{RESET}")
                return None
            matrix.append(row)
    print(f"{GREEN}Полученная матрица из файла: ")
    for i in matrix:
        print(*i)
    print(f"{RESET}")
    return matrix



def from_keyboard():
    while (True):
        dimension = (input("Введите размерность матрицы до 20 включительно: "))
        if (is_number(dimension)):
            dimension = int(dimension) 
        else:
            print(f"{RED}введите число{RESET}")
            continue
        if (dimension <= 20):
            break
        else:
            print(f"{RED}введите число меньше или равное 20{RESET}")
            continue
    return read_matrix(dimension)

def read_matrix(dimension):
    matrix = []
    print("Введите строки матрицы, разделяя коэффициенты черех пробел")
    for i in range(dimension):
        row = input_numbers(expected_length=dimension+1)
        matrix.append(row)
    return matrix

def check_convergence(matrix):
    n = len(matrix) - 1
    for i, row in enumerate(matrix):
        diag = abs(row[i])
        others = sum(abs(row[j]) for j in range(n) if j != i)
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
            print("варианты ввода, напишите цифру: \n" + "1. клава \n" + "2. файл \n" + "3. выйти из программы")
            selection = require_input(False)
        matrix = actions[selection]()
        if(matrix is None):
            return None
        if (make_diagonally_dominant(matrix)):
            return matrix
        print(f"{YELLOW}Матрица не прошла проверку, попробуйте ввести другую{RESET} \n")
    


def make_diagonally_dominant(matrix):    
    max_attempts = len(matrix)
    for _ in range(max_attempts):
        if check_convergence(matrix):
            return True
        try_swap(matrix)
    print(f"{RED}не удалось добиться диагонального преобладания{RESET}")
    return False

def build_jacobi_form(matrix):
    n = len(matrix)
    C = [[0.0]*n for _ in range(n)]
    d = [0.0]*n

    for i in range(n):
        a_ii = matrix[i][i]

        if a_ii == 0:
            raise ZeroDivisionError(f"{RED}Нулевой диагональный элемент в строке {i}{RESET}")

        for j in range(n):
            if i != j:
                C[i][j] = -matrix[i][j] / a_ii

        d[i] = matrix[i][-1] / a_ii
    
    print(f"{GREEN}получившаяся матрица C:")
    for i in C:
        print(*i)
    print(f"вектор d:\n{' '.join(map(str, d))}{RESET}")
    return C, d

def request_initial_approximation(dimension):
    print(f"Напишите начальное приближение c размерностью {dimension}, формат ввода: x1 x2 x3 ... xn")
    row_x0 = input_numbers( expected_length=dimension)
    return row_x0

def get_approximation_vector(matrix_c):
    print("Напишите эпсилон: ")
    epsilon = require_input(True)
    row_x0 = request_initial_approximation(len(matrix_c[0]))
    return epsilon, row_x0

def get_iterrations_count(matrix_c, d_vector):
    epsilon, row_0 = get_approximation_vector(matrix_c)
    count = 0
    diff = epsilon + 1
    new_row = []
    while True:
        new_row = iteration(matrix_c, row_0, d_vector)
        count+=1
        diff = [x - y for x, y in zip(row_0, new_row)]
        if(max(abs(x) for x in diff) < epsilon):
            break
        row_0 = new_row
    print(f"{GREEN}Введенный эпсилон: {epsilon} \n" 
          + f"Полученная строка: {new_row} \n" 
          + f"Количество итераций: {count} \n" 
          + f"Полученный max: {max(new_row)}{RESET}")
    
def get_iterations_rows(matrix_c, d_vector):
    epsilon, row_0 = get_approximation_vector(matrix_c)
    diff = epsilon + 1
    new_row = []
    while True:
        new_row = iteration(matrix_c, row_0, d_vector)
        diff = [x - y for x, y in zip(row_0, new_row)]
        diff_vector = [abs(new_row[i] - row_0[i]) for i in range(len(row_0))]
        print(f"{GREEN}Погрешности: {diff_vector}{RESET}")
        if(max(abs(x) for x in diff) < epsilon):
            break
        row_0 = new_row
    
def iteration(matrix_c, row_x0, d_vector):
    return [
        sum(matrix_c[i][j] * row_x0[j] for j in range(len(row_x0))) + d_vector[i]
        for i in range(len(row_x0))
    ]


def print_x(matrix_c, d_vector):
    for i in range(len(matrix_c)):
        print_jacobi_formula_from_C(i, matrix_c, d_vector)

def matrix_norm(matrix_c, d_vector):
    norm = max(sum(abs(koef) for koef in row) for row in matrix_c)
    if (norm < 1):
        print(f"{GREEN}Условие сходиомсти выполняется, норма: {norm}{RESET}")
    else:
        print(f"{RED}Условие не сходиомсти выполняется, норма: {norm}{RESET}") 

def print_jacobi_formula_from_C(i, C, d):
    sub = "₀₁₂₃₄₅₆₇₈₉"

    def idx(n):
        return ''.join(sub[int(d)] for d in str(n))

    parts = []
    n = len(C)

    for j in range(n):
        if C[i][j] == 0:
            continue

        coef = C[i][j]
        sign = '+' if coef >= 0 else '-'
        parts.append(f" {sign} {abs(coef):.3g}·x{idx(j+1)}^k")

    c_i = d[i]
    sign_c = '+' if c_i >= 0 else '-'

    formula = f"x{idx(i+1)}^(k+1) =" + ''.join(parts)
    formula += f" {sign_c} {abs(c_i):.3g}"

    print(f"{GREEN}{formula}{RESET}")

    
while True:
    actions = {
        1: matrix_norm,
        2: print_x,
        3: get_iterrations_count,
        4: get_iterations_rows
    }

    matrix = beginning()
    if matrix == None:
        continue
    matrix_c, d_vector = build_jacobi_form(matrix)
    outer_running = True
    while outer_running:


        while True:

            print("1. вывести норму матрицы \n" 
                + "2. вывести вектора неизвестных \n" 
                + "3. вывод количества итераций \n" 
                + "4. вывод вектора погрешностей \n" 
                + "5. вернуться к выбору ввода матрицы")

            selection = require_input(False)
            if selection is None:
                print(f"{RED}Введите число{RESET}")
                continue
            if selection == 5:
                outer_running = False
                break 
            elif selection in actions:
                actions[selection](matrix_c, d_vector)
                        

        










