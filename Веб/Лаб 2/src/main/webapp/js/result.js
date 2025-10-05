function fetchResults(clear = false) {
    const contextPath = window.location.pathname.split('/')[1];
    const url = `/${contextPath}/controller?clear=${clear}`;

    fetch(url)
        .then(res => res.json())
        .then(points => {
            if (!Array.isArray(points)) points = [];
            renderTable(points);
        })
        .catch(err => console.error("Ошибка:", err));
}

function renderTable(points) {
    const tableBody = document.getElementById("tableBody");
    tableBody.innerHTML = "";

    points.forEach(point => {
        const row = tableBody.insertRow();
        row.insertCell(0).textContent = point.x;
        row.insertCell(1).textContent = point.y;
        row.insertCell(2).textContent = point.r;
        row.insertCell(3).textContent = point.hit ? "Попадание" : "Мимо";
        row.insertCell(4).textContent = point.realTime || "";
        row.insertCell(5).textContent = point.timeSend || "";
    });
}

document.addEventListener("DOMContentLoaded", () => {
    fetchResults();

    const clearBtn = document.getElementById("clearHistoryBtn");
    if (clearBtn) {
        clearBtn.addEventListener("click", () => {
            fetchResults(true);
        });
    }
});
