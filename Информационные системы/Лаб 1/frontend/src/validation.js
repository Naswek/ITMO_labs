export function readNumber(value, label, { integer = false, optional = false, minExclusive, maxInclusive } = {}) {
  const raw = String(value ?? '').trim()
  if (!raw) {
    if (optional) return null
    throw new Error(`${label}: заполните поле.`)
  }

  const normalized = raw.replace(',', '.')
  if (!/^[+-]?(?:\d+(?:\.\d*)?|\.\d+)$/.test(normalized)) {
    throw new Error(`${label}: введите число; дробную часть можно отделять запятой или точкой.`)
  }

  const number = Number(normalized)
  if (!Number.isFinite(number)) throw new Error(`${label}: число слишком велико.`)
  if (integer && (!Number.isInteger(number) || !Number.isSafeInteger(number))) {
    throw new Error(`${label}: введите целое число в допустимом диапазоне.`)
  }
  if (minExclusive !== undefined && number <= minExclusive) {
    throw new Error(`${label}: значение должно быть больше ${minExclusive}.`)
  }
  if (maxInclusive !== undefined && number > maxInclusive) {
    throw new Error(`${label}: значение должно быть не больше ${maxInclusive}.`)
  }
  return number
}
