def is_binary_palindrome(n):
    """
    Проверяет, является ли 32-битное двоичное представление числа палиндромом.

    Аргументы:
        n (int): Целое число для проверки.

    Возвращает:
        int: 1, если двоичное представление является палиндромом, иначе 0.
    """
    binary_str = f"{n:032b}"  # Convert to 32-bit binary string
    res = binary_str == binary_str[::-1]
    return 1 if res else 0


assert is_binary_palindrome(5) == 0
assert is_binary_palindrome(15) == 0
assert is_binary_palindrome(4026531855) == 1
assert is_binary_palindrome(3221225474) == 0
