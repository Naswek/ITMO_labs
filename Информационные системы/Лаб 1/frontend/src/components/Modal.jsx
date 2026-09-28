import { useEffect, useRef } from 'react'

export default function Modal({ title, children, onClose, className = '' }) {
  const dialogRef = useRef(null)

  useEffect(() => {
    const dialog = dialogRef.current
    dialog.showModal()
    return () => dialog.close()
  }, [])

  return (
    <dialog
      ref={dialogRef}
      className={`modal ${className}`}
      aria-label={title}
      onCancel={(event) => { event.preventDefault(); onClose() }}
      onClick={(event) => { if (event.target === dialogRef.current) onClose() }}
    >
      <div className="modal-content">
        <div className="modal-header"><h2>{title}</h2><button type="button" className="quiet-button modal-close" aria-label="Закрыть" onClick={onClose}>×</button></div>
        {children}
      </div>
    </dialog>
  )
}
