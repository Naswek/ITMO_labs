export default function ResultPanel({ result, mode, error }) {
  if (error) {
    return <p style={{ color: "red", marginTop: 12, fontWeight: "bold" }}>{error}</p>;
  }

  if (!result) return null;

  return (
    <div style={{ marginTop: 20, padding: 15, border: "1px solid #ccc", borderRadius: 8 }}>
      <h3>Результат:</h3>
      
      {result.isError ? (
        <p style={{ color: "red", fontSize: "18px", fontWeight: "bold" }}>{result.message}</p>
      ) : (
        <>
          {mode === "function" && (
            <>
              <p><strong>x:</strong> {result.x}</p>
              <p><strong>f(x):</strong> {result.fx}</p>
              <p><strong>Итерации:</strong> {result.iterations}</p>
            </>
          )}

          {mode === "system" && (
            <>
              <p><strong>x:</strong> [{Array.isArray(result.x) ? result.x.join("; ") : ""}]</p>
              <p><strong>f(x):</strong> [{Array.isArray(result.fx) ? result.fx.join("; ") : ""}]</p>
              <p><strong>Итерации:</strong> {result.iterations}</p>
            </>
          )}

          {mode === "integral" && (
            <>
              <p><strong>Значение интеграла:</strong> {result.value}</p>
              <p><strong>Число разбиений (n):</strong> {result.n}</p>
            </>
          )}

          <p style={{ color: "gray", marginTop: 10 }}>Статус: {result.message}</p>
        </>
      )}
    </div>
  );
}