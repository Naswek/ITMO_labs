import { useEffect, useState } from 'react'
import { api } from '../api'
import { readNumber } from '../validation'
import Modal from './Modal'

const colors = ['GREEN', 'YELLOW', 'ORANGE', 'WHITE']
const countries = ['USA', 'SPAIN', 'NORTH_KOREA', 'JAPAN']

function ReferenceForm({ kind, item, onClose, onSaved }) {
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const discipline = kind === 'disciplines'
  async function submit(event) {
    event.preventDefault()
    const data = new FormData(event.currentTarget)
    let body
    try {
      const name = String(data.get('name') ?? '').trim()
      if (!name || name.length > 255) throw new Error(`${discipline ? 'Название дисциплины' : 'Имя автора'} обязательно и не может быть длиннее 255 символов.`)
      if (discipline) {
        body = {
          name,
          practiceHours: readNumber(data.get('practiceHours'), 'Часы практики', { integer: true }),
          selfStudyHours: readNumber(data.get('selfStudyHours'), 'Часы самостоятельной работы', { integer: true }),
          labsCount: readNumber(data.get('labsCount'), 'Количество лабораторных', { integer: true, optional: true, maxInclusive: 2147483647 }),
        }
        if (body.labsCount !== null && body.labsCount < -2147483648) throw new Error('Количество лабораторных: число слишком мало.')
      } else {
        const birthday = String(data.get('birthday') ?? '')
        const date = new Date(`${birthday}T00:00:00Z`)
        if (!/^\d{4}-\d{2}-\d{2}$/.test(birthday) || Number.isNaN(date.getTime()) || date.toISOString().slice(0, 10) !== birthday) {
          throw new Error('Дата рождения: укажите корректную дату.')
        }
        const locationName = String(data.get('locationName') ?? '')
        if (locationName.length > 500) throw new Error('Название местоположения не может быть длиннее 500 символов.')
        body = {
          name,
          eyeColor: data.get('eyeColor'),
          hairColor: data.get('hairColor') || null,
          birthday: date.toISOString(),
          weight: readNumber(data.get('weight'), 'Вес', { integer: true, optional: true, minExclusive: 0 }),
          nationality: data.get('nationality'),
          location: {
            x: readNumber(data.get('locationX'), 'Местоположение X', { integer: true }),
            y: readNumber(data.get('locationY'), 'Местоположение Y'),
            z: readNumber(data.get('locationZ'), 'Местоположение Z', { integer: true }),
            name: locationName,
          },
        }
      }
    } catch (problem) { setError(problem.message); return }
    setBusy(true); setError('')
    try {
      await api(item ? `references/${kind}/${item.id}` : `references/${kind}`, { method: item ? 'PUT' : 'POST', body })
      onSaved(discipline ? 'Дисциплина сохранена' : 'Автор сохранён')
    } catch (problem) { setError(problem.message) }
    finally { setBusy(false) }
  }
  return <Modal title={`${item ? 'Изменить' : 'Добавить'} ${discipline ? 'дисциплину' : 'автора'}`} onClose={onClose}><form onSubmit={submit} noValidate><div className="form-grid">
    <label className="wide">{discipline ? 'Название' : 'Имя'}<input name="name" required maxLength="255" defaultValue={item?.name || ''} /></label>
    {discipline ? <>
      <label>Часы практики<input name="practiceHours" type="text" inputMode="numeric" required defaultValue={item?.practiceHours ?? ''} /></label>
      <label>Часы самостоятельной работы<input name="selfStudyHours" type="text" inputMode="numeric" required defaultValue={item?.selfStudyHours ?? ''} /></label>
      <label>Количество лабораторных <small>необязательно</small><input name="labsCount" type="text" inputMode="numeric" defaultValue={item?.labsCount ?? ''} /></label>
    </> : <>
      <label>Цвет глаз<select name="eyeColor" required defaultValue={item?.eyeColor || 'GREEN'}>{colors.map((color) => <option key={color}>{color}</option>)}</select></label>
      <label>Цвет волос <small>необязательно</small><select name="hairColor" defaultValue={item?.hairColor || ''}><option value="">Не указан</option>{colors.map((color) => <option key={color}>{color}</option>)}</select></label>
      <label>Дата рождения<input name="birthday" type="date" required defaultValue={item?.birthday ? new Date(item.birthday).toISOString().slice(0, 10) : ''} /></label>
      <label>Вес <small>больше 0, необязательно</small><input name="weight" type="text" inputMode="numeric" defaultValue={item?.weight ?? ''} /></label>
      <label>Гражданство<select name="nationality" required defaultValue={item?.nationality || 'USA'}>{countries.map((country) => <option key={country}>{country}</option>)}</select></label>
      <label>Местоположение: название<input name="locationName" maxLength="500" defaultValue={item?.location?.name || ''} /></label>
      <label>Местоположение: X<input name="locationX" type="text" inputMode="numeric" required defaultValue={item?.location?.x ?? ''} /></label>
      <label>Местоположение: Y <small>запятая или точка</small><input name="locationY" type="text" inputMode="decimal" required defaultValue={item?.location?.y ?? ''} /></label>
      <label>Местоположение: Z<input name="locationZ" type="text" inputMode="numeric" required defaultValue={item?.location?.z ?? ''} /></label>
    </>}
  </div><p className="error" role="alert">{error}</p><div className="actions"><button disabled={busy}>{busy ? 'Сохраняем…' : 'Сохранить'}</button><button type="button" className="secondary" onClick={onClose}>Отмена</button></div></form></Modal>
}

function DeleteReference({ modal, values, onClose, onDeleted }) {
  const [replacement, setReplacement] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)
  const isDiscipline = modal.kind === 'disciplines'
  async function submit(event) {
    event.preventDefault(); setBusy(true); setError('')
    try {
      const query = replacement ? `?replacementId=${replacement}` : ''
      await api(`references/${modal.kind}/${modal.item.id}${query}`, { method: 'DELETE' })
      onDeleted()
    } catch (problem) { setError(problem.message) }
    finally { setBusy(false) }
  }
  return <Modal title={isDiscipline ? 'Удалить дисциплину' : 'Удалить автора'} onClose={onClose}><p>Удалить «{modal.item.name}»? Если запись используется, выберите замену: связанные работы перейдут к ней.</p><form onSubmit={submit}><label>Новая {isDiscipline ? 'дисциплина' : 'запись автора'}<select value={replacement} onChange={(event) => setReplacement(event.target.value)}><option value="">Не выбрана</option>{values.filter((value) => value.id !== modal.item.id).map((value) => <option key={value.id} value={value.id}>#{value.id} {value.name}</option>)}</select></label><p className="error" role="alert">{error}</p><div className="actions"><button className="danger" disabled={busy}>{busy ? 'Удаляем…' : 'Удалить'}</button><button type="button" className="secondary" onClick={onClose}>Отмена</button></div></form></Modal>
}

export default function ReferencesView({ revision, onChanged, notify }) {
  const [items, setItems] = useState({ disciplines: [], people: [] })
  const [modal, setModal] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true
    Promise.all([api('references/disciplines'), api('references/people')]).then(([disciplines, people]) => { if (active) { setItems({ disciplines, people }); setError('') } }).catch((problem) => { if (active) setError(problem.message) })
    return () => { active = false }
  }, [revision])

  function saved(message) { setModal(null); onChanged(); notify(message) }
  return <>
    <div className="section-head"><h1>Справочники</h1></div>
    {error && <p className="error" role="alert">{error}</p>}
    <div className="reference-grid">{[['disciplines', 'Дисциплины'], ['people', 'Авторы']].map(([kind, label]) => <section className="panel" key={kind}><div className="panel-head"><h2>{label}</h2><button className="small" onClick={() => setModal({ mode: 'form', kind, item: null })}>+ Добавить</button></div>{items[kind].length ? items[kind].map((item) => <div className="reference-item" key={item.id}><div><strong>#{item.id} {item.name}</strong><small>{kind === 'disciplines' ? `Практика: ${item.practiceHours}, самостоятельная работа: ${item.selfStudyHours}, лабораторные: ${item.labsCount ?? '—'}` : `${item.nationality}, ${item.eyeColor}, ${item.location?.name ?? '—'}`}</small></div><div><button className="small secondary" onClick={() => setModal({ mode: 'form', kind, item })}>Изменить</button><button className="small danger" onClick={() => setModal({ mode: 'delete', kind, item })}>Удалить</button></div></div>) : <p>Записей пока нет.</p>}</section>)}</div>
    {modal?.mode === 'form' && <ReferenceForm key={`${modal.kind}-${modal.item?.id || 'new'}`} kind={modal.kind} item={modal.item} onClose={() => setModal(null)} onSaved={saved} />}
    {modal?.mode === 'delete' && <DeleteReference key={`${modal.kind}-${modal.item.id}`} modal={modal} values={items[modal.kind]} onClose={() => setModal(null)} onDeleted={() => saved('Запись удалена, связи перенесены')} />}
  </>
}
