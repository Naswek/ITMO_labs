<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Веб, лаб 2 — Результаты</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <header>Ровкова Анастасия, P3216, 407893</header>
    <h2>Таблица результатов</h2>

    <div style="max-height: 500px; overflow-y: auto;">
        <table id="coordTable" border="1">
            <thead>
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Попадание</th>
                <th>Время</th>
                <th>Время работы (мс)</th>
            </tr>
            </thead>
            <tbody id="tableBody"></tbody>
        </table>
    </div>
    <a href="${pageContext.request.contextPath}/index.jsp" class="btn">Вернуться к вводу данных</a>
    <button id="clearHistoryBtn" class="btn">Очистить историю</button>
</div>

<script src="${pageContext.request.contextPath}/js/result.js"></script>

</body>
</html>
