import { useState } from 'react'
import { api } from '../api'

export default function AuthView({ onLogin }) {
  const [mode, setMode] = useState('login')
  const [login, setLogin] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function submit(event) {
    event.preventDefault()
    const normalizedLogin = login.trim()
    if (!normalizedLogin || normalizedLogin.length > 100) { setError('Логин обязателен и не может быть длиннее 100 символов.'); return }
    if (!password.trim()) { setError('Пароль не может быть пустым.'); return }
    setBusy(true)
    setError('')
    try {
      if (mode === 'register') await api('users', { method: 'POST', body: { login: normalizedLogin, password } })
      const user = await api('users/login', { method: 'POST', body: { login: normalizedLogin, password } })
      onLogin(user)
    } catch (problem) { setError(problem.message) }
    finally { setBusy(false) }
  }

  return <section className="auth-card">
    <h1>{mode === 'login' ? 'Вход' : 'Регистрация'}</h1>
    <form onSubmit={submit} noValidate><label>Логин<input autoComplete="username" required maxLength="100" value={login} onChange={(event) => setLogin(event.target.value)} /></label><label>Пароль<input type="password" autoComplete={mode === 'login' ? 'current-password' : 'new-password'} required value={password} onChange={(event) => setPassword(event.target.value)} /></label><p className="error" role="alert">{error}</p><div className="auth-actions"><button disabled={busy}>{busy ? 'Подождите…' : mode === 'login' ? 'Войти' : 'Зарегистрироваться'}</button><button type="button" className="secondary" onClick={() => { setMode(mode === 'login' ? 'register' : 'login'); setError('') }}>{mode === 'login' ? 'Регистрация' : 'Уже есть аккаунт? Войти'}</button></div></form>
  </section>
}
