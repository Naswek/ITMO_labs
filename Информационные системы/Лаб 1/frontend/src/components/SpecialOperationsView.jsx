import { useState } from 'react'
import { api } from '../api'
import { readNumber } from '../validation'

const order = ['EASY', 'IMPOSSIBLE', 'INSANE', 'HOPELESS']

function Result({ result, kind }) {
  if (result === undefined) return <p className="result-caption">Результат появится здесь.</p>
  if (result?.error) return <p className="result-error" role="alert">{result.error}</p>
  if (result == null || (Array.isArray(result) && result.length === 0)) return <p>Ничего не найдено.</p>
  if (kind === 'counts') return <ul className="result-list">{order.map((difficulty) => <li className="result-row" key={difficulty}><strong>{difficulty}</strong><span>{result[difficulty] ?? 0} работ</span></li>)}</ul>
  if (kind === 'list') return <ul className="result-list">{result.map((work) => <li className="result-row" key={work.id}><strong>#{work.id} {work.name}</strong><span>{work.difficulty}</span></li>)}</ul>
  return <ul className="result-list"><li className="result-row"><strong>#{result.id} {result.name}</strong><span>{result.difficulty}</span></li>{result.description && <li className="result-row"><strong>Описание</strong><span>{result.description}</span></li>}</ul>
}

export default function SpecialOperationsView({ onChanged }) {
  const [result, setResult] = useState(undefined)
  const [kind, setKind] = useState('work')
  const [busy, setBusy] = useState(false)

  async function run(request, resultKind = 'work', changed = false) {
    setBusy(true); setKind(resultKind)
    try {
      const value = await request()
      setResult(value)
      if (changed) onChanged()
    } catch (problem) { setResult({ error: problem.message }) }
    finally { setBusy(false) }
  }

  function shiftDifficulty(event) {
    event.preventDefault()
    const data = new FormData(event.currentTarget)
    const direction = event.nativeEvent.submitter?.value || 'increase'
    try {
      const id = readNumber(data.get('id'), 'ID работы', { integer: true, minExclusive: 0, maxInclusive: 2147483647 })
      const steps = readNumber(data.get('steps'), 'Количество шагов', { integer: true, minExclusive: 0, maxInclusive: 2147483647 })
      run(() => api(`labworks/${id}/difficulty/${direction}?steps=${steps}`, { method: 'POST' }), 'work', true)
    } catch (problem) { setResult({ error: problem.message }) }
  }

  function searchByName(event) {
    event.preventDefault()
    const substring = String(new FormData(event.currentTarget).get('substring') ?? '')
    if (!substring.trim()) { setResult({ error: 'Укажите часть названия для поиска.' }); return }
    run(() => api(`labworks/special/name-contains?substring=${encodeURIComponent(substring)}`), 'list')
  }

  return <>
    <div className="section-head"><h1>Специальные операции</h1></div>
    <div className="special-grid">
      <form className="panel" onSubmit={(event) => { event.preventDefault(); run(() => api('labworks/special/maximum-difficulty')) }}><h2>Максимальная сложность</h2><div className="actions"><button disabled={busy}>Найти работу</button></div></form>
      <form className="panel" onSubmit={(event) => { event.preventDefault(); run(() => api('labworks/special/difficulty-counts'), 'counts') }}><h2>Группировка по сложности</h2><div className="actions"><button disabled={busy}>Показать количество</button></div></form>
      <form className="panel" onSubmit={searchByName} noValidate><h2>Поиск по названию</h2><label>Подстрока<input name="substring" required /></label><div className="actions"><button disabled={busy}>Искать</button></div></form>
      <form className="panel" onSubmit={shiftDifficulty} noValidate><h2>Изменить сложность</h2><label>ID работы<input name="id" type="text" inputMode="numeric" required /></label><label>Количество шагов<input name="steps" type="text" inputMode="numeric" required /></label><div className="actions"><button value="increase" disabled={busy}>Повысить</button><button value="decrease" disabled={busy} className="secondary">Понизить</button></div></form>
    </div>
    <section className="panel result"><div className="panel-head"><h2>Результат</h2></div><div className="special-output"><Result result={result} kind={kind} /></div></section>
  </>
}
