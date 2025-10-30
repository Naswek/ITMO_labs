document.addEventListener("DOMContentLoaded", () => {
    const minVal = -3;
    const maxVal = 5;
    const scale = 50;

    const defaultX = 0;
    const xRadios = document.querySelectorAll("[id$='xRadio'] input[type=radio]");
    xRadios.forEach(r => r.checked = parseFloat(r.value) === defaultX);

    const yInput = document.querySelector("[id$='yInput']");
    if (yInput && (yInput.value === null || yInput.value === "")) {
        yInput.value = "0";
    }

    const hiddenX = document.querySelector("[id$='hiddenX']");
    xRadios.forEach(r => {
        r.addEventListener("change", e => {
            if (hiddenX) hiddenX.value = parseFloat(e.target.value);
        });
    });

    const colors = [
        "rgba(0,128,255,0.4)",
        "rgba(0,255,128,0.4)",
        "rgba(255,128,0,0.4)",
        "rgba(255,0,255,0.4)",
        "rgba(255,255,0,0.4)"
    ];

    const canvas = document.querySelector("[id$='axisCanvas']");
    if (!canvas) return console.error("Canvas not found!");
    const ctx = canvas.getContext("2d");

    const width = (maxVal - minVal) * scale + 100;
    const height = (maxVal - minVal) * scale + 100;
    canvas.width = width;
    canvas.height = height;

    const centerX = -minVal * scale + 50;
    const centerY = maxVal * scale + 50;

    let selectedRs = new Set();
    let points = [];

    function drawAxes() {
        ctx.clearRect(0, 0, width, height);
        ctx.strokeStyle = "white";
        ctx.lineWidth = 2;

        ctx.beginPath();
        ctx.moveTo(0, centerY);
        ctx.lineTo(width, centerY);
        ctx.moveTo(centerX, 0);
        ctx.lineTo(centerX, height);
        ctx.stroke();

        ctx.fillStyle = "white";
        ctx.font = "12px Arial";
        ctx.textAlign = "center";

        for (let i = minVal; i <= maxVal; i++) {
            const x = centerX + i * scale;
            const y = centerY - i * scale;
            ctx.beginPath();
            ctx.moveTo(x, centerY - 5);
            ctx.lineTo(x, centerY + 5);
            ctx.moveTo(centerX - 5, y);
            ctx.lineTo(centerX + 5, y);
            ctx.stroke();
            if (i !== 0) {
                ctx.fillText(i, x, centerY + 15);
                ctx.fillText(i, centerX + 15, y + 5);
            }
        }
    }

    function drawShapes() {
        let idx = 0;
        selectedRs.forEach(r => {
            ctx.fillStyle = colors[idx % colors.length];
            ctx.globalAlpha = 0.4;

            ctx.fillRect(centerX, centerY - r*scale/2, r*scale, r*scale/2);

            ctx.beginPath();
            ctx.moveTo(centerX, centerY);
            ctx.arc(centerX, centerY, r*scale, Math.PI, Math.PI*1.5, false);
            ctx.closePath();
            ctx.fill();

            ctx.beginPath();
            ctx.moveTo(centerX, centerY);
            ctx.lineTo(centerX - r*scale, centerY);
            ctx.lineTo(centerX, centerY + r*scale);
            ctx.closePath();
            ctx.fill();

            ctx.globalAlpha = 1;
            idx++;
        });
    }

    function drawPointsFromTable() {
        points = [];
        const rows = document.querySelectorAll("#resultTable tbody tr");
        rows.forEach(row => {
            const cells = row.querySelectorAll("td");
            if (cells.length < 4) return;
            const x = parseFloat(cells[0].textContent);
            const y = parseFloat(cells[1].textContent);
            const r = parseFloat(cells[2].textContent);
            const hit = cells[3].textContent.trim() === "Попадание";
            points.push({ x, y, r, hit });
        });

        const grouped = {};
        points.forEach(p => {
            const key = `${p.x}_${p.y}`;
            if (!grouped[key]) grouped[key] = [];
            grouped[key].push(p);
        });

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawAxes();
        drawShapes();

        Object.values(grouped).forEach(arr => {
            const px = centerX + arr[0].x*scale;
            const py = centerY - arr[0].y*scale;
            let color;

            if (arr.length === 1) {
                color = arr[0].hit ? "green" : "red";
            } else {
                const hits = arr.map(p => p.hit);
                color = hits.every(h => h === hits[0]) ? (hits[0] ? "green" : "red") : "yellow";
            }

            ctx.beginPath();
            ctx.arc(px, py, 5, 0, 2*Math.PI);
            ctx.fillStyle = color;
            ctx.fill();
        });
    }

    function updateCanvas() {
        drawAxes();
        drawShapes();
        drawPointsFromTable();
    }

    const tooltip = document.createElement("div");
    tooltip.classList.add("tooltip");
    document.body.appendChild(tooltip);

    canvas.addEventListener("mousemove", e => {
        const rect = canvas.getBoundingClientRect();
        const mx = e.clientX - rect.left;
        const my = e.clientY - rect.top;

        const hovered = Object.values(points.reduce((acc, p) => {
            const key = `${p.x}_${p.y}`;
            if (!acc[key]) acc[key] = [];
            acc[key].push(p);
            return acc;
        }, {})).filter(arr => {
            const px = centerX + arr[0].x*scale;
            const py = centerY - arr[0].y*scale;
            return Math.hypot(px - mx, py - my) < 6;
        });

        if (hovered.length > 0) {
            const arr = hovered[0];
            if (arr.length > 1) {
                tooltip.innerHTML = arr.map(p =>
                    `<div><span style="color:${p.hit ? "green" : "red"};">●</span> R=${p.r}, ${p.hit ? "Попадание" : "Промах"}</div>`
                ).join("");
                tooltip.style.left = e.pageX+10 + "px";
                tooltip.style.top = e.pageY+10 + "px";
                tooltip.classList.add("visible");
            } else {
                tooltip.classList.remove("visible");
            }
        } else {
            tooltip.classList.remove("visible");
        }
    });

    function updateSelectedR() {
        selectedRs = new Set(Array.from(document.querySelectorAll("[id$='rCheckbox'] input[type=checkbox]:checked"))
            .map(cb => parseFloat(cb.value)));
        updateCanvas();
    }

    document.querySelectorAll("[id$='rCheckbox'] input[type=checkbox]").forEach(cb => {
        cb.addEventListener("change", updateSelectedR);
    });

    updateCanvas();

    function showError(el, msg) {
        clearError(el);
        const span = document.createElement("span");
        span.classList.add("error-msg");
        span.textContent = msg;
        el.insertAdjacentElement("afterend", span);
    }

    function clearError(el) {
        const next = el.nextElementSibling;
        if (next && next.classList.contains("error-msg")) next.remove();
    }

    canvas.addEventListener("click", event => {
        if (selectedRs.size === 0) {
            showError(canvas, "Выберите хотя бы одно R!");
            return;
        }
        clearError(canvas);

        const rect = canvas.getBoundingClientRect();
        const x = parseFloat(((event.clientX - rect.left - centerX) / scale).toFixed(2));
        const y = parseFloat(((centerY - (event.clientY - rect.top)) / scale).toFixed(2));

        if (hiddenX) hiddenX.value = x;
        const yInput = document.querySelector("[id$='yInput']");
        if (yInput) yInput.value = y;

        xRadios.forEach(r => r.checked = Math.round(x) === parseFloat(r.value));

        const sendBtn = document.querySelector("[id$='sendBtn']");
        if (sendBtn) sendBtn.click();
    });

    const checkBtn = document.querySelector("[id$='sendBtn']");
    if (checkBtn) {
        checkBtn.addEventListener("click", e => {
            let errors = [];
            const yInput = document.querySelector("[id$='yInput']");
            const yValue = yInput ? yInput.value : null;

            let xValue = null;
            if (hiddenX && hiddenX.value) {
                xValue = parseFloat(hiddenX.value);
            } else {
                const xRadio = document.querySelector("[id$='xRadio'] input[type=radio]:checked");
                if (xRadio) xValue = parseFloat(xRadio.value);
            }

            const yNum = parseFloat(yValue);

            if (xValue === null || isNaN(xValue)) errors.push("Выберите X");

            if (!yValue) errors.push("Введите Y");
            else if (isNaN(yNum)) errors.push("Y должно быть числом");
            else if (yNum < -3 || yNum > 5) errors.push("Y должен быть в диапазоне [-3, 5]");

            if (selectedRs.size === 0) errors.push("Выберите хотя бы одно R");

            if (errors.length > 0) {
                errors.forEach(msg => showError(checkBtn, msg));
                return false;
            }

            clearError(checkBtn);
        });
    }

    setInterval(drawPointsFromTable, 500);
});
