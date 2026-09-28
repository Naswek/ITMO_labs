import { useEffect, useState } from 'react'
import { api } from '../api'
import { readNumber } from '../validation'
import Modal from './Modal'

const textFields = [
  ['name', 'Название'], ['description', 'Описание'], ['difficulty', 'Сложность'],
  ['discipline.name', 'Дисциплина'], ['author.name', 'Автор'],
  ['author.eyeColor', 'Цвет глаз'], ['author.hairColor', 'Цвет волос'],
  ['author.nationality', 'Страна'], ['author.location.name', 'Местоположение'],
]

const dateText = (date) => date ? new Date(date).toLocaleString('ru-RU') : '—'
const display = (value) => value == null || value === '' ? '—' : String(value)

function FieldSelect({ label, name, values, value }) {
  return <label>{label}<select name={name} defaultValue={value == null ? '' : String(value)}><option value="">Не выбран</option>{values.map((item) => <option key={item.id} value={item.id}>#{item.id} {item.name}</option>)}</select></label>
}

function WorkForm({ work, disciplines, people, onClose, onSaved, onReload }) {
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const [conflict, setConflict] = useState(false)
  async function submit(event) {
    event.preventDefault()
    if (conflict) return
    const data = new FormData(event.currentTarget)
    let body
    try {
      const name = String(data.get('name') ?? '').trim()
      const description = String(data.get('description') ?? '')
      if (!name || name.length > 255) throw new Error('Название обязательно и не может быть длиннее 255 символов.')
      if (description.length > 3429) throw new Error('Описание не может быть длиннее 3429 символов.')
      body = {
        name,
        coordinates: {
          x: readNumber(data.get('coordinateX'), 'Координата X', { minExclusive: -523 }),
          y: readNumber(data.get('coordinateY'), 'Координата Y', { integer: true, minExclusive: -643, maxInclusive: 2147483647 }),
        },
        description,
        difficulty: data.get('difficulty'),
        minimalPoint: readNumber(data.get('minimalPoint'), 'Минимальный балл', { minExclusive: 0 }),
        averagePoint: readNumber(data.get('averagePoint'), 'Средний балл', { integer: true, minExclusive: 0, maxInclusive: 2147483647 }),
        discipline: data.get('disciplineId') ? { id: Number(data.get('disciplineId')) } : null,
        author: data.get('authorId') ? { id: Number(data.get('authorId')) } : null,
      }
    } catch (problem) { setError(problem.message); return }
    if (work) body.version = work.version
    setBusy(true); setError('')
    try {
      await api(work ? `labworks/${work.id}` : 'labworks', { method: work ? 'PUT' : 'POST', body })
      onSaved(work ? 'Работа обновлена' : 'Работа добавлена')
    } catch (problem) { setError(problem.message); if (problem.status === 409) setConflict(true) }
    finally { setBusy(false) }
  }
  return <Modal title={work ? `Изменить работу #${work.id}` : 'Добавить лабораторную работу'} onClose={onClose}>
    <form onSubmit={submit} noValidate><div className="form-grid">
      <label className="wide">Название<input name="name" required maxLength="255" defaultValue={work?.name || ''} /></label>
      <label>Координата X <small>больше −523; запятая или точка</small><input name="coordinateX" type="text" inputMode="decimal" required defaultValue={work?.coordinates?.x ?? ''} /></label>
      <label>Координата Y <small>целое число больше −643</small><input name="coordinateY" type="text" inputMode="numeric" required defaultValue={work?.coordinates?.y ?? ''} /></label>
      <label className="wide">Описание <small>до 3429 символов</small><textarea name="description" maxLength="3429" defaultValue={work?.description || ''} /></label>
      <label>Сложность<select name="difficulty" required defaultValue={work?.difficulty || 'EASY'}>{['EASY', 'IMPOSSIBLE', 'INSANE', 'HOPELESS'].map((value) => <option key={value}>{value}</option>)}</select></label>
      <FieldSelect label="Дисциплина" name="disciplineId" values={disciplines} value={work?.discipline?.id} />
      <label>Минимальный балл <small>больше 0; запятая или точка</small><input name="minimalPoint" type="text" inputMode="decimal" required defaultValue={work?.minimalPoint ?? ''} /></label>
      <label>Средний балл <small>целое число больше 0</small><input name="averagePoint" type="text" inputMode="numeric" required defaultValue={work?.averagePoint ?? ''} /></label>
      <FieldSelect label="Автор" name="authorId" values={people} value={work?.author?.id} />
    </div><p className="error" role="alert">{error}</p>{conflict && <p><button type="button" className="secondary" onClick={onReload}>Загрузить актуальную версию</button> Несохранённые данные формы будут заменены.</p>}<div className="actions"><button disabled={busy || conflict}>{busy ? 'Сохраняем…' : 'Сохранить'}</button><button type="button" className="secondary" onClick={onClose}>Отмена</button></div></form>
  </Modal>
}

function WorkInfo({ work, onClose, onEdit }) {
  const fields = [
    ['ID', work.id], ['Название', work.name], ['Координаты', `${work.coordinates?.x}; ${work.coordinates?.y}`],
    ['Дата создания', dateText(work.creationDate)], ['Описание', work.description], ['Сложность', work.difficulty],
    ['Минимальный балл', work.minimalPoint], ['Средний балл', work.averagePoint],
    ['Дисциплина', work.discipline ? `#${work.discipline.id} ${work.discipline.name}` : null],
    ['Практические часы', work.discipline?.practiceHours], ['Самостоятельные часы', work.discipline?.selfStudyHours], ['Количество лабораторных', work.discipline?.labsCount],
    ['Автор', work.author ? `#${work.author.id} ${work.author.name}` : null], ['Цвет глаз', work.author?.eyeColor],
    ['Цвет волос', work.author?.hairColor], ['Дата рождения', dateText(work.author?.birthday)], ['Вес', work.author?.weight],
    ['Национальность', work.author?.nationality], ['Местоположение', work.author?.location ? `${work.author.location.name} (${work.author.location.x}; ${work.author.location.y}; ${work.author.location.z})` : null],
  ]
  return <Modal title={`Работа #${work.id}`} onClose={onClose}><dl>{fields.map(([label, value]) => <div className="detail-pair" key={label}><dt>{label}</dt><dd>{display(value)}</dd></div>)}</dl><div className="actions"><button onClick={onEdit}>Изменить</button><button className="secondary" onClick={onClose}>Закрыть</button></div></Modal>
}

export default function LabWorksView({ revision, onChanged, notify }) {
  const [data, setData] = useState({ items: [], total: 0, page: 0, size: 10 })
  const [page, setPage] = useState(0)
  const [filters, setFilters] = useState({ filterField: 'name', filterValue: '', sortField: '', direction: 'asc' })
  const [draft, setDraft] = useState(filters)
  const [refs, setRefs] = useState({ disciplines: [], people: [] })
  const [modal, setModal] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    const query = new URLSearchParams({ page, size: 10, ...filters })
    api(`labworks?${query}`).then((result) => { if (active) { setData(result); setError(''); if (page > 0 && result.items.length === 0) setPage(page - 1) } }).catch((problem) => { if (active) setError(problem.message) })
    return () => { active = false }
  }, [page, filters, revision])

  useEffect(() => {
    let active = true
    Promise.all([api('references/disciplines'), api('references/people')]).then(([disciplines, people]) => { if (active) setRefs({ disciplines, people }) }).catch((problem) => { if (active) setError(problem.message) })
    return () => { active = false }
  }, [revision])

  async function loadReferences() {
    const [disciplines, people] = await Promise.all([api('references/disciplines'), api('references/people')])
    setRefs({ disciplines, people })
  }

  async function createWork() {
    try { await loadReferences(); setModal({ mode: 'form', work: null }) }
    catch (problem) { notify(problem.message) }
  }

  async function openWork(id, mode) {
    try {
      if (mode === 'form') await loadReferences()
      setModal({ mode, work: await api(`labworks/${id}`) })
    }
    catch (problem) { notify(problem.message) }
  }

  async function remove() {
    try { await api(`labworks/${modal.work.id}?version=${modal.work.version}`, { method: 'DELETE' }); setModal(null); onChanged(); notify('Работа удалена') }
    catch (problem) { setModal({ ...modal, error: problem.message, conflict: problem.status === 409 }) }
  }

  async function reloadForDelete() {
    try { setModal({ mode: 'delete', work: await api(`labworks/${modal.work.id}`) }) }
    catch (problem) { setModal({ ...modal, error: problem.message }) }
  }

  const pages = Math.max(1, Math.ceil(data.total / 10))
  return <>
    <div className="section-head"><div><h1>Лабораторные работы</h1><p>Всего: {data.total}</p></div><button onClick={createWork}>Добавить работу</button></div>
    <form className="controls" onSubmit={(event) => { event.preventDefault(); setPage(0); setFilters(draft) }}>
      <label>Фильтр по полю<select value={draft.filterField} onChange={(event) => setDraft({ ...draft, filterField: event.target.value })}>{textFields.map(([value, label]) => <option key={value} value={value}>{label}</option>)}</select></label>
      <label>Содержит<input value={draft.filterValue} onChange={(event) => setDraft({ ...draft, filterValue: event.target.value })} placeholder="Часть слова или имени" /></label>
      <label>Сортировать по<select value={draft.sortField} onChange={(event) => setDraft({ ...draft, sortField: event.target.value })}><option value="">По умолчанию</option>{textFields.map(([value, label]) => <option key={value} value={value}>{label}</option>)}</select></label>
      <label>Порядок<select value={draft.direction} onChange={(event) => setDraft({ ...draft, direction: event.target.value })}><option value="asc">По возрастанию</option><option value="desc">По убыванию</option></select></label><button>Применить</button>
    </form>
    {error && <p className="error" role="alert">{error}</p>}
    <div className="table-wrap"><table><thead><tr>{['ID', 'Название', 'X', 'Y', 'Создана', 'Описание', 'Сложность', 'Дисциплина', 'Мин. балл', 'Ср. балл', 'Автор', 'Действия'].map((label) => <th key={label}>{label}</th>)}</tr></thead><tbody>{data.items.length ? data.items.map((work) => <tr key={work.id}><td>{work.id}</td><td>{work.name}</td><td>{display(work.coordinates?.x)}</td><td>{display(work.coordinates?.y)}</td><td>{dateText(work.creationDate)}</td><td className="description-cell" title={work.description}>{display(work.description)}</td><td>{work.difficulty}</td><td>{display(work.discipline?.name)}</td><td>{work.minimalPoint}</td><td>{work.averagePoint}</td><td>{display(work.author?.name)}</td><td className="table-actions"><button className="small secondary" onClick={() => openWork(work.id, 'info')}>Инфо</button><button className="small secondary" onClick={() => openWork(work.id, 'form')}>Изменить</button><button className="small danger" onClick={() => setModal({ mode: 'delete', work })}>Удалить</button></td></tr>) : <tr><td className="empty-table" colSpan="12">Работ пока нет или фильтр ничего не нашёл.</td></tr>}</tbody></table></div>
    <div className="pager"><button className="secondary small" disabled={page === 0} onClick={() => setPage(page - 1)}>← Назад</button><span>Страница {page + 1} из {pages} · всего {data.total}</span><button className="secondary small" disabled={page + 1 >= pages} onClick={() => setPage(page + 1)}>Далее →</button></div>
    {modal?.mode === 'form' && <WorkForm key={modal.work ? `${modal.work.id}-${modal.work.version}` : 'new'} work={modal.work} disciplines={refs.disciplines} people={refs.people} onClose={() => setModal(null)} onReload={() => openWork(modal.work.id, 'form')} onSaved={(message) => { setModal(null); onChanged(); notify(message) }} />}
    {modal?.mode === 'info' && <WorkInfo work={modal.work} onClose={() => setModal(null)} onEdit={() => openWork(modal.work.id, 'form')} />}
    {modal?.mode === 'delete' && <Modal title="Удалить работу" onClose={() => setModal(null)}><p>Удалить работу «{modal.work.name}»? Это действие нельзя отменить.</p><p className="error" role="alert">{modal.error}</p><div className="actions">{modal.conflict && <button className="secondary" onClick={reloadForDelete}>Загрузить актуальную версию</button>}<button className="danger" disabled={modal.conflict} onClick={remove}>Удалить</button><button className="secondary" onClick={() => setModal(null)}>Отмена</button></div></Modal>}
  </>
}
