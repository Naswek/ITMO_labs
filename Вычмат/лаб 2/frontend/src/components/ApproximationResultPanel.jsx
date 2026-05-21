import React from "react";

export default function ApproximationResultPanel({ result, error }) {
  if (error) return <div style={{ color: "red" }}>{error}</div>;
  if (!result) return null;

  return (
    <div style={{ marginTop: 20, border: "1px solid #ccc", padding: 15 }}>
      <h3>Результаты сравнения моделей</h3>
      <p><b>Наилучшее приближение:</b> <span style={{color: 'green'}}>{result.bestMethod}</span></p>
      
      <table border="1" cellPadding="10" style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr style={{ backgroundColor: "#f2f2f2" }}>
            <th>Метод</th>
            <th>Уравнение</th>
            <th>СКО (MSE)</th>
            <th>R² (Детерминация)</th>
            <th>Статус</th>
          </tr>
        </thead>
        <tbody>
          {result.results.map((res, i) => (
            <tr key={i} style={res.methodName === result.bestMethod ? { fontWeight: "bold", background: "#e6fffa" } : {}}>
              <td>{res.methodName}</td>
              <td>{res.equation || "—"}</td>
              <td>{res.mse}</td>
              <td>{res.rSquared}</td>
              <td style={{ color: res.message === "Успешно" ? "green" : "red" }}>
                {res.message}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}