import os, sys


def require_input():
    print("варианты ввода, напишите цифру: \n" + "1. клава \n" + "2. файл")
    return int(input())


def from_file():
    file_name  = input("Введите название файла: ")
    if (os.path.exists(file_name) and os.path.isfile(file_name)):
        with open(file_name, 'r') as f:
            sys.stdin = f
            dimension = int(input())
            return read_matrix(dimension)
    print("Такого файла нет")
    return None


def from_keyboard():
    while (True):
        dimension = int(input("Введите размерность матрицы до 20 включительно: "))
        if (dimension <= 20):
            break
        else:
            print("")
            continue
    return read_matrix(dimension)

def read_matrix(dimension):
    matrix = []
    print("Введите строки матрицы, разделяя коэффициенты черех пробел")
    for i in range(dimension):
        line = list(input().split(" "))
        matrix.append(line)
    return matrix

def check_convergence(maxtrix):
    for i in matrix:
        ...



selection = None


while selection not in (1, 2):
    selection = require_input()
matrix = from_keyboard if selection == 1 else from_file



