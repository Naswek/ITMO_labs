import { useEffect, useState } from 'react'
import { api } from '../api'

export default function ImportsView({ revision, onChanged, admin }) {
  const [file, setFile] = useState(null)
  const [history, setHistory] = useState([])
  const [error, setError] = useState('')
  const [historyError, setHistoryError] = useState('')
  const [message, setMessage] = useState('')
  const [busy, setBusy] = useState(false)

  async function loadHistory() {
    try { setHistory(await api('imports')); setHistoryError('') }
    catch (problem) { setHistoryError(problem.message) }
  }

  useEffect(() => {
    let active = true
    api('imports').then((items) => { if (active) { setHistory(items); setHistoryError('') } })
      .catch((problem) => { if (active) setHistoryError(problem.message) })
    return () => { active = false }
  }, [revision])

  async function upload(event) {
    event.preventDefault()
    const form = event.currentTarget
    setError(''); setMessage('')
    if (!file) { setError('Выберите CSV-файл.'); return }
    if (!file.name.toLowerCase().endsWith('.csv')) { setError('Выберите файл с расширением .csv.'); return }
    if (file.size === 0 || file.size > 2 * 1024 * 1024) {
      setError('Файл должен быть непустым и не больше 2 МБ.'); return
    }
    setBusy(true)
    try {
      const result = await api('imports', { method: 'POST', rawBody: file, headers: { 'Content-Type': 'text/csv' } })
      setMessage(`Импорт #${result.id} завершён: добавлено ${result.addedCount} работ.`)
      setFile(null)
      form.reset()
      onChanged()
    } catch (problem) { setError(problem.message) }
    finally { setBusy(false); await loadHistory() }
  }

  return <>
    <div className="section-head"><div><h1>Импорт работ</h1><p>Загрузите CSV в UTF-8: одна работа в строке, поля координат, дисциплины и автора — в отдельных колонках. Названия работ, координаты и названия дисциплин не должны повторяться. <a href={new URL('import-labworks.csv', document.baseURI).href} download>Скачать пример</a>.</p></div></div>
    <div className="panel">
      <form onSubmit={upload}>
        <label>Файл CSV<input type="file" accept=".csv,text/csv" onChange={(event) => setFile(event.target.files?.[0] ?? null)} /></label>
        <div className="actions"><button disabled={busy}>{busy ? 'Загружаем…' : 'Импортировать'}</button></div>
      </form>
      {error && <p className="error" role="alert">{error}</p>}
      {message && <p role="status">{message}</p>}
    </div>
    <div className="section-head"><h2>История импорта</h2></div>
    <p>{admin ? 'Показаны операции всех пользователей.' : 'Показаны ваши операции.'}</p>
    {historyError && <p className="error" role="alert">{historyError}</p>}
    <div className="table-wrap"><table><thead><tr><th>ID</th><th>Время</th><th>Статус</th><th>Пользователь</th><th>Добавлено</th><th>Ошибка</th></tr></thead><tbody>
      {history.length ? history.map((item) => <tr key={item.id}><td>{item.id}</td><td>{new Date(item.startedAt).toLocaleString('ru-RU')}</td><td>{item.status === 'SUCCESS' ? 'Успешно' : 'Ошибка'}</td><td>{item.user}</td><td>{item.status === 'SUCCESS' ? item.addedCount : '—'}</td><td>{item.errorMessage || '—'}</td></tr>) : <tr><td colSpan="6">Операций пока нет.</td></tr>}
    </tbody></table></div>
  </>
}
