function updateClock() {
    const now = new Date();
    const formatted = now.toLocaleTimeString('ru-RU', { hour12: false });
    document.getElementById('clock').textContent = formatted;
}
updateClock();
setInterval(updateClock, 6000);