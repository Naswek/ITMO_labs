import assert from 'node:assert/strict'
import test from 'node:test'
import { readNumber } from './validation.js'

test('decimal comma and dot produce the same number', () => {
  assert.equal(readNumber(' 12,5 ', 'Балл'), 12.5)
  assert.equal(readNumber('12.5', 'Балл'), 12.5)
})

test('empty required numbers and malformed decimals show field names', () => {
  assert.throws(() => readNumber('', 'Координата X'), /Координата X: заполните поле/)
  assert.throws(() => readNumber('1,2,3', 'Балл'), /Балл: введите число/)
})

test('integer and boundary checks reject invalid values', () => {
  assert.throws(() => readNumber('2,5', 'Шаги', { integer: true }), /Шаги: введите целое число/)
  assert.throws(() => readNumber('0', 'Шаги', { integer: true, minExclusive: 0 }), /Шаги: значение должно быть больше 0/)
  assert.equal(readNumber('', 'Вес', { optional: true }), null)
})
