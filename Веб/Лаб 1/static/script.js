const minVal = -3;
const maxVal = 5;
const stepValue = 1;
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

const STORAGE_KEY = "savedPoints";

function drawAxes() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.beginPath();
    ctx.moveTo(centerX + minVal * scale, centerY);
    ctx.lineTo(centerX + maxVal * scale, centerY);
    ctx.strokeStyle = "black";
    ctx.lineWidth = 2;
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(centerX, centerY - minVal * scale);
    ctx.lineTo(centerX, centerY - maxVal * scale);
    ctx.stroke();

    ctx.font = "14px Arial";
    ctx.fillStyle = "black";
    ctx.textAlign = "center";
    ctx.textBaseline = "top";

    for (let val = minVal; val <= maxVal; val += stepValue) {
        const x = centerX + val * scale;
        ctx.beginPath();
        ctx.moveTo(x, centerY - 5);
        ctx.lineTo(x, centerY + 5);
        ctx.stroke();
        ctx.fillText(val.toString(), x, centerY + 10);
    }

    ctx.textAlign = "left";
    ctx.textBaseline = "middle";

    for (let val = minVal; val <= maxVal; val += stepValue) {
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

    ctx.fillRect(centerX - r * scale, centerY - r * scale, r * scale, r * scale);

    ctx.beginPath();
    ctx.arc(centerX, centerY, r * scale / 2, 0, Math.PI / 2);
    ctx.lineTo(centerX, centerY);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(centerX - r * scale / 2, centerY);
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

function savePointToStorage(pointData) {
    let savedPoints = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];
    savedPoints.push(pointData);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(savedPoints));
}

function loadPointsFromStorage() {
    const savedPoints = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];

    const tableBody = document.querySelector("#coordTable tbody");
    tableBody.innerHTML = "";

    savedPoints.forEach(point => {
        const colorIndex = point.r % colors.length;
        drawPoint(point.x, point.y, colors[colorIndex]);

        const newRow = tableBody.insertRow();
        newRow.insertCell(0).textContent = point.x;
        newRow.insertCell(1).textContent = point.y;
        newRow.insertCell(2).textContent = point.r;
        newRow.insertCell(3).textContent = point.hit ? "Попадание" : "Мимо";
        newRow.insertCell(4).textContent = point.timestamp;
        newRow.insertCell(5).textContent = point.execTime;
    });
}

function clearStorage() {
    localStorage.removeItem(STORAGE_KEY);
    updateShapes();
    const tableBody = document.querySelector("#coordTable tbody");
    tableBody.innerHTML = "";
}

const massive = [-3, -2, -1, 0, 1, 2, 3, 4, 5];
const ySelect = document.getElementById("ySelect");

for (let y = -3; y <= 5; y++) {
    if (typeof y === "number" && massive.includes(y)) {
        const opt = document.createElement("option");
        opt.value = y;
        opt.textContent = y;
        ySelect.appendChild(opt);
    }
}

drawAxes();

const xInput = document.getElementById("xInput");

function showError(input, message) {
    let error = input.nextElementSibling;
    if (error && error.classList.contains("error-msg")) {
        error.remove();
    }

    const span = document.createElement("span");
    span.classList.add("error-msg");
    span.style.color = "red";
    span.style.fontSize = "12px";
    span.textContent = message;
    input.insertAdjacentElement("afterend", span);

    input.style.border = "2px solid red";
}

function clearError(input) {
    let error = input.nextElementSibling;
    if (error && error.classList.contains("error-msg")) error.remove();
    input.style.border = "";
}

function sendData(payload) {
    const params = new URLSearchParams({
        data: JSON.stringify(payload)
    });

    const startTime = performance.now();

    fetch(`fcgi-bin/lab1.jar?${params.toString()}`, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(res => {
            if (!res.ok) throw new Error("Ошибка сервера: " + res.status);
            return res.json();
        })
        .then(data => {
            const endTime = performance.now();
            const execTime = (endTime - startTime).toFixed(2);
            const now = new Date().toLocaleTimeString();

            const tableBody = document.querySelector("#coordTable tbody");
            const items = Array.isArray(data) ? data : [data];

            items.forEach(item => {
                const newRow = tableBody.insertRow();
                newRow.insertCell(0).textContent = x;
                newRow.insertCell(1).textContent = item.y;
                newRow.insertCell(2).textContent = item.r;
                newRow.insertCell(3).textContent = item.hit ? "Попадание" : "Мимо";
                newRow.insertCell(4).textContent = now;
                newRow.insertCell(5).textContent = execTime;

                const pointData = {
                    x: item.x,
                    y: item.y,
                    r: item.r,
                    hit: item.hit,
                    timestamp: now,
                    execTime: execTime
                };
                savePointToStorage(pointData);
            });
        })
        .catch(err => console.error("Ошибка:", err));
}

document.getElementById("sendBtn").addEventListener("click", () => {
    x = parseFloat(xInput.value);
    const y = parseFloat(ySelect.value);
    const rValues = Array.from(document.querySelectorAll(".multiR:checked")).map(cb => parseFloat(cb.value));

    let hasError = false;

    if (isNaN(x)) {
        showError(xInput, "Введите X!");
        hasError = true;
    } else {
        clearError(xInput);
    }

    if (isNaN(y)) {
        showError(ySelect, "Неправильный ввод Y!");
        hasError = true;
    } else {
        clearError(ySelect);
    }

    if (!rValues.length) {
        const btn = document.getElementById("sendBtn");
        showError(btn, "Выберите хотя бы одно R!");
        hasError = true;
    } else {
        clearError(document.getElementById("sendBtn"));
    }

    if (hasError) return;
    x = (xInput.value);

    const validRValues = rValues.filter(r => !isNaN(r));
    if (validRValues.length === 0) {
        const btn = document.getElementById("sendBtn");
        showError(btn, "Неправильные значения R!");
        return;
    } else clearError(document.getElementById("sendBtn"));

    const payload = [];
    validRValues.forEach((r, idx) => {
        const color = colors[idx % colors.length];
        drawPoint(x, y, color);
        x = xInput.value;
        payload.push({x, y, r });
        console.log(payload)
    });

    sendData(payload);
});


function updateShapes() {
    drawAxes();
    const rValues = Array.from(document.querySelectorAll(".multiR:checked")).map(cb => parseFloat(cb.value));
    rValues.forEach((r, idx) => {
        const color = colors[idx % colors.length];
        drawShapes(r, color);
    });
    loadPointsFromStorage();
}

document.querySelectorAll(".multiR").forEach(cb => {
    cb.addEventListener("change", updateShapes);
});

function addClearButton() {
    const clearBtn = document.createElement("button");
    clearBtn.textContent = "Очистить историю";
    clearBtn.style.margin = "10px";
    clearBtn.style.padding = "5px 10px";
    clearBtn.addEventListener("click", clearStorage);

    const tableContainer = document.querySelector("#coordTable").parentNode;
    tableContainer.insertBefore(clearBtn, document.querySelector("#coordTable"));
}

document.addEventListener("DOMContentLoaded", function() {
    updateShapes();
    addClearButton();
});
