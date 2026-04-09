export default function ResultPanel({ result, mode, error }) {
  if (error) {
    return <p style={{ color: "red", marginTop: 12 }}>{error}</p>;
  }

  if (!result) return null;

  return (
    <div style={{ marginTop: 12 }}>
      {mode === "function" ? (
        <>
          <p>x = {result.x}</p>
          <p>f(x) = {result.fx}</p>
        </>
      ) : (
        <>
          <p>x = [{Array.isArray(result.x) ? result.x.join(", ") : ""}]</p>
          <p>f(x) = [{Array.isArray(result.fx) ? result.fx.join(", ") : ""}]</p>
        </>
      )}

      <p>iterations = {result.iterations}</p>
      <p>{result.message}</p>
    </div>
  );
}