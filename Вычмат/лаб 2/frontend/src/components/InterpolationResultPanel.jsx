import React, { useState } from "react";

function RenderDifferenceTable({ table, sourcePoints }) {
  if (!table || table.length === 0) return null;
  const n = table.length;

  const headers = ["i", "x_i", "y_i"];
  for (let j = 1; j < n; j++) {
    headers.push(`Δ^${j} y_i`);
  }

  return (
    <div style={{ 
      marginTop: 15, 
      overflowX: "auto", 
      border: "1px solid #ddd", 
      padding: 12, 
      borderRadius: 8, 
      backgroundColor: "#fcfcfc" 
    }}>
      <h5 style={{ marginTop: 0, marginBottom: 10, color: "#333" }}>Таблица разностей (конечных / разделенных)</h5>
      <table border="1" cellPadding="6" style={{ 
        width: "100%", 
        borderCollapse: "collapse", 
        fontSize: "12px", 
        textAlign: "center",
        borderColor: "#eee" 
      }}>
        <thead>
          <tr style={{ backgroundColor: "#f2f2f2" }}>
            {headers.map((h, idx) => <th key={idx}>{h}</th>)}
          </tr>
        </thead>
        <tbody>
          {Array(n).fill(null).map((_, i) => {
            const xVal = sourcePoints[i] != null ? sourcePoints[i].x.toFixed(4) : "—";
            return (
              <tr key={i}>
                <td style={{ backgroundColor: "#fafafa", fontWeight: "bold" }}>{i}</td>
                <td style={{ backgroundColor: "#fafafa" }}>{xVal}</td>
                {Array(n).fill(null).map((_, j) => {
                  if (i + j < n) {
                    const val = table[i][j];
                    return (
                      <td key={j} style={{ fontWeight: j === 0 ? "bold" : "normal" }}>
                        {typeof val === 'number' ? val.toFixed(5) : "—"}
                      </td>
                    );
                  } else {
                    return <td key={j} style={{ backgroundColor: "#f7f7f7", color: "#ccc" }}>—</td>;
                  }
                })}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}

export default function InterpolationResultPanel({ result, error }) {
  const [activeTableIdx, setActiveTableIdx] = useState(null);

  if (error) return <div style={{ color: "red", padding: 10, fontWeight: "bold" }}>{error}</div>;
  if (!result) return null;

  const toggleTable = (idx) => {
    setActiveTableIdx(activeTableIdx === idx ? null : idx);
  };

  return (
    <div style={{ marginTop: 20, border: "1px solid #ccc", padding: 15, borderRadius: 12, backgroundColor: "#fff" }}>
      <h3 style={{ marginTop: 0, borderBottom: "1px solid #eee", paddingBottom: 10 }}>Результаты интерполирования</h3>
      
      <table border="1" cellPadding="10" style={{ width: "100%", borderCollapse: "collapse", borderColor: "#ddd" }}>
        <thead>
          <tr style={{ backgroundColor: "#f2f2f2" }}>
            <th>Метод</th>
            <th>Вычисленное значение f(X)</th>
            <th>Статус</th>
            <th>Разности</th>
          </tr>
        </thead>
        <tbody>
          {result.results.map((res, i) => {
            const isSuccess = res.isSuccess;
            const hasTable = res.differenceTable && res.differenceTable.length > 0;
            return (
              <React.Fragment key={i}>
                <tr>
                  <td style={{ fontWeight: "bold" }}>
                    {res.methodName}
                    {res.isExtrapolated && (
                      <span style={{ 
                        marginLeft: 8, 
                        fontSize: "11px", 
                        color: "#fd7e14", 
                        backgroundColor: "#fff3cd", 
                        padding: "2px 6px", 
                        borderRadius: 4, 
                        border: "1px solid #ffeeba",
                        fontWeight: "normal"
                      }}>
                        Экстраполяция
                      </span>
                    )}
                  </td>
                  <td style={{ fontSize: "15px", fontWeight: "bold", color: isSuccess ? "#007bff" : "#ccc" }}>
                    {isSuccess && res.value != null ? res.value.toFixed(6) : "—"}
                  </td>
                  <td style={{ color: isSuccess ? "green" : "red", fontSize: "13px" }}>
                    {res.message}
                  </td>
                  <td>
                    {isSuccess && hasTable ? (
                      <button 
                        type="button" 
                        onClick={() => toggleTable(i)}
                        style={{ 
                          padding: "4px 8px", 
                          fontSize: "12px", 
                          cursor: "pointer",
                          backgroundColor: activeTableIdx === i ? "#6c757d" : "#007bff",
                          color: "white",
                          border: "none",
                          borderRadius: "4px"
                        }}
                      >
                        {activeTableIdx === i ? "Скрыть" : "Показать"}
                      </button>
                    ) : "—"}
                  </td>
                </tr>
                {activeTableIdx === i && isSuccess && hasTable && (
                  <tr>
                    <td colSpan="4">
                      <RenderDifferenceTable table={res.differenceTable} sourcePoints={result.sourcePoints} />
                    </td>
                  </tr>
                )}
              </React.Fragment>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
