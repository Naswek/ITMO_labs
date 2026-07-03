def determinant_2x2_stream(*xs):
    """Input: first word N, then N matrices: a, b, c, d.

    Output: N values of determinant where det = a*d - b*c.
    """
    n = xs[0]
    if n < 0:
        return [-1]

    result = []
    for i in range(n):
        base = 1 + 4 * i
        a, b, c, d = xs[base : base + 4]
        det = a * d - b * c
        if det < -0x80000000 or det > 0x7FFFFFFF:
            return [0xCCCCCCCC]
        result.append(det)

    return result


assert determinant_2x2_stream(0) == []
assert determinant_2x2_stream(1, 1, 2, 3, 4) == [-2]
assert determinant_2x2_stream(2, 1, 0, 0, 1, 2, 3, 5, 7) == [1, -1]
assert determinant_2x2_stream(3, 0, 0, 0, 0, -1, 2, 3, -4, 7, -5, 4, 8) == [0, -2, 76]