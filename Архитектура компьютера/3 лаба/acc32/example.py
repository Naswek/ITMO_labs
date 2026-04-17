def gcd(a, b):
    """Найти наибольший общий делитель (НОД)"""
    while b != 0:
        a, b = b, a % b
    return [abs(a)]


assert gcd(48, 18) == [6]
assert gcd(56, 98) == [14]
