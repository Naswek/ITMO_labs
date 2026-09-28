export class ApiError extends Error {
  constructor(message, status) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

export function apiUrl(path) {
  return new URL(`api/${path}`, document.baseURI)
}

export async function api(path, options = {}) {
  const { body, headers: customHeaders, ...rest } = options
  const headers = { Accept: 'application/json', ...customHeaders }
  if (body != null) headers['Content-Type'] = 'application/json'

  let response
  try {
    response = await fetch(apiUrl(path), {
      credentials: 'same-origin',
      headers,
      body: body == null ? undefined : JSON.stringify(body),
      ...rest,
    })
  } catch {
    throw new ApiError('Сервер недоступен. Проверьте подключение и повторите попытку.', 0)
  }

  const data = response.status === 204 ? null : await response.json().catch(() => null)
  if (!response.ok) {
    if (response.status === 401 && path !== 'users/me' && path !== 'users/login') {
      window.dispatchEvent(new Event('labwork:unauthorized'))
    }
    throw new ApiError(data?.error || `Ошибка HTTP ${response.status}`, response.status)
  }
  return data
}
