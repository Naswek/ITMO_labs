import { useEffect, useRef, useState } from 'react'
import { api, apiUrl } from './api'
import AuthView from './components/AuthView'
import LabWorksView from './components/LabWorksView'
import ReferencesView from './components/ReferencesView'
import SpecialOperationsView from './components/SpecialOperationsView'

const AUTH_EVENT_KEY = 'labworks:auth-change'

export default function App() {
  const [user, setUser] = useState(null)
  const [checking, setChecking] = useState(true)
  const [view, setView] = useState('works')
  const [revision, setRevision] = useState(0)
  const [online, setOnline] = useState(false)
  const [toast, setToast] = useState('')
  const userRef = useRef(null)
  const authCheck = useRef(0)

  function setCurrentUser(next) {
    userRef.current = next
    setUser(next)
  }

  function publishAuthChange(kind) {
    try { localStorage.setItem(AUTH_EVENT_KEY, JSON.stringify({ kind, at: Date.now(), nonce: Math.random() })) }
    catch { /* Сессия остаётся доступной через cookie и проверку при возврате во вкладку. */ }
  }

  async function checkSession(showNetworkError = false) {
    const requestNumber = ++authCheck.current
    try {
      const current = await api('users/me')
      if (requestNumber === authCheck.current) setCurrentUser(current)
    } catch (error) {
      if (requestNumber !== authCheck.current) return
      if (error.status === 401) {
        if (userRef.current) {
          setToast('Сессия завершена. Войдите снова.')
          publishAuthChange('logout')
        }
        setCurrentUser(null)
      } else if (showNetworkError) setToast(error.message)
    } finally {
      if (requestNumber === authCheck.current) setChecking(false)
    }
  }

  useEffect(() => {
    queueMicrotask(() => checkSession(true))
    const unauthorized = () => {
      ++authCheck.current
      setCurrentUser(null)
      setChecking(false)
      setOnline(false)
      setToast('Сессия завершена. Войдите снова.')
      publishAuthChange('logout')
    }
    const changedInOtherTab = (event) => {
      if (event.key !== AUTH_EVENT_KEY) return
      let kind
      try { kind = JSON.parse(event.newValue)?.kind } catch { return }
      if (kind === 'logout') {
        ++authCheck.current
        if (userRef.current) setToast('В другом окне выполнен выход из аккаунта.')
        setCurrentUser(null)
        setChecking(false)
        setOnline(false)
      } else if (kind === 'login') checkSession()
    }
    const becameVisible = () => { if (!document.hidden) checkSession() }
    window.addEventListener('labwork:unauthorized', unauthorized)
    window.addEventListener('storage', changedInOtherTab)
    document.addEventListener('visibilitychange', becameVisible)
    return () => {
      window.removeEventListener('labwork:unauthorized', unauthorized)
      window.removeEventListener('storage', changedInOtherTab)
      document.removeEventListener('visibilitychange', becameVisible)
    }
  }, [])

  useEffect(() => {
    if (!user) return undefined
    const stream = new EventSource(apiUrl('labworks/events'))
    stream.addEventListener('changed', () => setRevision((value) => value + 1))
    stream.onopen = () => { setOnline(true); setRevision((value) => value + 1) }
    stream.onerror = () => { setOnline(false); if (navigator.onLine) checkSession() }
    return () => { stream.close(); setOnline(false) }
  }, [user])

  useEffect(() => {
    if (!toast) return undefined
    const timer = window.setTimeout(() => setToast(''), 4500)
    return () => window.clearTimeout(timer)
  }, [toast])

  async function logout() {
    try {
      await api('users/logout', { method: 'POST' })
      ++authCheck.current
      setCurrentUser(null)
      publishAuthChange('logout')
    } catch (error) { setToast(error.message) }
  }

  function loggedIn(current) {
    ++authCheck.current
    setCurrentUser(current)
    publishAuthChange('login')
  }

  const changed = () => setRevision((value) => value + 1)

  return <>
    <header className="topbar">
      <strong className="brand">LabWork</strong>
      {user && <nav aria-label="Разделы"><button className={view === 'works' ? 'active' : ''} onClick={() => setView('works')}>Работы</button><button className={view === 'references' ? 'active' : ''} onClick={() => setView('references')}>Справочники</button><button className={view === 'special' ? 'active' : ''} onClick={() => setView('special')}>Операции</button></nav>}
      {user && <div id="account"><span className="live-status">{online ? 'Связь есть' : 'Подключение…'}</span><span id="current-user">{user.login}</span><button className="secondary" onClick={logout}>Выйти</button></div>}
    </header>
    <main>{checking ? <div className="panel loading">Проверяем сессию…</div> : !user ? <AuthView onLogin={loggedIn} /> : view === 'works' ? <LabWorksView revision={revision} onChanged={changed} notify={setToast} /> : view === 'references' ? <ReferencesView revision={revision} onChanged={changed} notify={setToast} /> : <SpecialOperationsView onChanged={changed} />}</main>
    <div id="toast" className={toast ? 'visible' : ''} role="status" aria-live="polite">{toast}</div>
  </>
}
