<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Веб, лаб 2 — Ввод данных</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="container">
    <header>Ровкова Анастасия, P3216, 407893</header>
    <h2>Выбор параметров</h2>

    <div>
        <h3>X:</h3>
        <div id="xContainer">
            <% for(double i = 2; i >= -2; i -= 0.5) { %>
            <label>
                <input type="checkbox" name="x" value="<%= i %>"> <%= i %>
            </label>
            <% } %>
        </div>
    </div>

    <label>
        <h3>Y:</h3>
        <input type="text" id="yInput" placeholder="Введите Y">
    </label>

    <div>
        <h3>R:</h3>
        <div id="rContainer">
            <% for(double r = 1; r <= 3; r += 0.5) { %>
            <button type="button" class="rBtn btn" data-value="<%= r %>"><%= r %></button>
            <% } %>
        </div>
    </div>

    <div class="actions">
        <button id="sendBtn" class="btn">Отправить</button>
        <a href="${pageContext.request.contextPath}/result.jsp" class="btn">Перейти к результатам</a>
    </div>
</div>

<canvas id="axisCanvas"></canvas>

<img src="${pageContext.request.contextPath}/pictures/cat1.gif" alt="Гифка" class="corner-gif">

<script type="module" src="js/canvas.js"></script>

</body>
</html>
