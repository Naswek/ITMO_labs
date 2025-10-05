const minVal = -3;
const maxVal = 5;
const scale = 50;
const colors = ["lightblue", "lightgreen", "lightcoral", "khaki", "plum"];

const canvas = document.getElementById("axisCanvas");
const ctx = canvas.getContext("2d");
const width = (maxVal - minVal) * scale + 100;
const height = (maxVal - minVal) * scale + 100;
canvas.width = width;
canvas.height = height;
const centerX = -minVal * scale + 50;
const centerY = maxVal * scale + 50;

let selectedR = null;

function drawAxes() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Оси
    ctx.beginPath();
    ctx.moveTo(centerX + minVal * scale, centerY);
    ctx.lineTo(centerX + maxVal * scale, centerY);
    ctx.moveTo(centerX, centerY - minVal * scale);
    ctx.lineTo(centerX, centerY - maxVal * scale);
    ctx.strokeStyle = "black";
    ctx.lineWidth = 2;
    ctx.stroke();

    ctx.font = "14px Arial";
    ctx.fillStyle = "black";
    ctx.textAlign = "center";
    ctx.textBaseline = "top";

    for (let val = minVal; val <= maxVal; val++) {
        const x = centerX + val * scale;
        ctx.beginPath();
        ctx.moveTo(x, centerY - 5);
        ctx.lineTo(x, centerY + 5);
        ctx.stroke();
        ctx.fillText(val.toString(), x, centerY + 10);
    }

    ctx.textAlign = "left";
    ctx.textBaseline = "middle";

    for (let val = minVal; val <= maxVal; val++) {
        const y = centerY - val * scale;
        ctx.beginPath();
        ctx.moveTo(centerX - 5, y);
        ctx.lineTo(centerX + 5, y);
        ctx.stroke();
        if (val !== 0) ctx.fillText(val.toString(), centerX + 10, y);
    }
}

function drawShapes(r, color) {
    ctx.fillStyle = color;
    ctx.globalAlpha = 0.4;
    ctx.fillRect(centerX - r * scale, centerY - r * scale / 2, r * scale, r * scale / 2);

    ctx.beginPath();
    ctx.arc(centerX, centerY, r * scale, 0, -Math.PI / 2, true);
    ctx.lineTo(centerX, centerY);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(centerX + r * scale / 2, centerY);
    ctx.lineTo(centerX, centerY + r * scale / 2);
    ctx.lineTo(centerX, centerY);
    ctx.closePath();
    ctx.fill();

    ctx.globalAlpha = 1;
}

function drawPoint(x, y, color) {
    const px = centerX + x * scale;
    const py = centerY - y * scale;
    ctx.beginPath();
    ctx.arc(px, py, 4, 0, Math.PI * 2);
    ctx.fillStyle = color;
    ctx.fill();
}

document.getElementById("sendBtn").addEventListener("click", () => {
    const xCheckboxes = Array.from(document.querySelectorAll("input[name='x']:checked"));
    const yInput = document.getElementById("yInput");
    const y = parseFloat(yInput.value);
    const r = selectedR;

    let hasError = false;

    // Проверка X
    if (xCheckboxes.length === 0) {
        showError(document.getElementById("xContainer"), "Выберите хотя бы один X!");
        hasError = true;
    } else clearError(document.getElementById("xContainer"));

    // Проверка Y
    if (isNaN(y) || y < -3 || y > 5) {
        showError(yInput, "Введите корректный Y от -3 до 5!");
        hasError = true;
    } else clearError(yInput);

    // Проверка R
    if (!r) {
        showError(document.getElementById("rContainer"), "Выберите R!");
        hasError = true;
    } else clearError(document.getElementById("rContainer"));

    if (hasError) return;

    const points = xCheckboxes.map(cb => ({ x: parseFloat(cb.value), y, r }));
    points.forEach((p, idx) => drawPoint(p.x, p.y, colors[idx % colors.length]));
    sendData(points);
});



function sendData(points, clear = false) {
    const contextPath = window.location.pathname.split('/')[1];
    let url = `/${contextPath}/controller?`;
    const params = new URLSearchParams();

    if (clear) {
        params.append("clear", "true");
    } else {
        const now = new Date().toLocaleTimeString();
        const startTime = performance.now();

        const payload = points.map(p => ({
            x: p.x,
            y: p.y,
            r: p.r,
            realTime: now,
            timeSend: startTime.toFixed(2)
        }));

        params.append("data", JSON.stringify(payload));
        params.append("clear", "false");
    }

    url += params.toString();

    fetch(url, { method: "GET", headers: { "Accept": "application/json" } })
        .then(res => res.json())
        .then(data => {
            console.log("Ответ сервера:", data); // выводим в консоль для отладки
        })
        .catch(err => console.error("Ошибка:", err));
}

function setupCanvasClick() {
    canvas.addEventListener("click", (event) => {
        if (!selectedR) return;

        const rect = canvas.getBoundingClientRect();
        const clickX = event.clientX - rect.left;
        const clickY = event.clientY - rect.top;

        const x = parseFloat(((clickX - centerX) / scale).toFixed(2));
        const y = parseFloat(((centerY - clickY) / scale).toFixed(2));

        drawPoint(x, y, colors[0]);
        sendData([{ x, y, r: selectedR }]);
    });
}

document.querySelectorAll('.rBtn').forEach(btn => {
    btn.addEventListener('click', () => {
        selectedR = parseFloat(btn.dataset.value);
        document.querySelectorAll('.rBtn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        drawAxes();
        drawShapes(selectedR, colors[0]);
    });
});

// Показывает сообщение об ошибке рядом с input или контейнером
function showError(element, message) {
    // Если уже есть ошибка — удаляем
    clearError(element);

    // Создаем новый элемент для ошибки
    const errorSpan = document.createElement("span");
    errorSpan.classList.add("error-msg"); // класс для CSS
    errorSpan.textContent = message;

    // Вставляем после элемента
    element.insertAdjacentElement("afterend", errorSpan);

    // Подсветка поля или контейнера
    element.classList.add("input-error");
}

// Убирает сообщение об ошибке и подсветку
function clearError(element) {
    const next = element.nextElementSibling;
    if (next && next.classList.contains("error-msg")) {
        next.remove();
    }
    element.classList.remove("input-error");
}


document.addEventListener("DOMContentLoaded", () => {
    drawAxes();
    setupCanvasClick();
});
