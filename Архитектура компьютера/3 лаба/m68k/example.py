def rle_compress_bytes(*input_words):
    """сжатие методом rle (run-length encoding) для байтов, упакованных в 32-битные слова.

    формат входных данных:
    - первое слово: длина данных в байтах
    - последующие слова: байты данных, упакованные в слова (по 4 байта в каждом)
    - если количество байт не кратно 4, дополняется нулями

    формат выходных данных:
    - первое слово: длина сжатых данных в байтах
    - последующие слова: сжатые данные в виде пар (количество + байт)

    пример:
    [4, 0x0a0a0a0a] -> [2, 0x040a0000]
    (4 байта со значением 0x0a → количество = 4, байт = 0x0a)
    """
    if not input_words:
        return [-1]

    length = input_words[0]
    if length < 0:
        return [-1]

    if length == 0:
        return [0]

    try:
        # extract bytes from words
        bytes_data = []
        word_count = (length + 3) // 4  # round up to nearest word

        for i in range(1, min(len(input_words), word_count + 1)):
            word = input_words[i]
            for j in range(4):
                if len(bytes_data) < length:
                    byte_val = (word >> (24 - j * 8)) & 0xFF
                    bytes_data.append(byte_val)

        if len(bytes_data) < length:
            return [-1]  # not enough input data

        # compress bytes
        compressed = []
        i = 0
        while i < len(bytes_data):
            current_byte = bytes_data[i]
            count = 1

            # count consecutive identical bytes
            while (
                i + count < len(bytes_data)
                and bytes_data[i + count] == current_byte
                and count < 255
            ):
                count += 1

            compressed.append(count)
            compressed.append(current_byte)
            i += count

        # pack compressed data into words
        result = [len(compressed)]  # length in bytes

        for i in range(0, len(compressed), 4):
            word = 0
            for j in range(4):
                if i + j < len(compressed):
                    word |= (compressed[i + j] & 0xFF) << (24 - j * 8)
            result.append(word)

        return result

    except exception:
        return [-1]


assert rle_compress_bytes([4, 168430090]) == [2, 67764224]
assert rle_compress_bytes([12, 2863315899, 3435973836, 3722304989]) == [
    8,
    44696251,
    80479453,
]
assert rle_compress_bytes([1, 4278190080]) == [2, 33488896]
