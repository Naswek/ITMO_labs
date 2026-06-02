import React, { useEffect, useRef, useState } from "react";
import * as Desmos from "desmos";

function toLatex(f) {
  if (!f) return "";
  let s = String(f);
  if (s.includes("=")) s = s.split("=")[1];

  return s
    .replace(/\s+/g, "")                
    .replace(/,/g, ".")                 
    .replace(/×/g, "*")                 
    .replace(/\*/g, "\\cdot ")          
    .replace(/ln\(x\)/g, "\\ln x")      
    .replace(/exp\(([^)]+)\)/gi, "e^{$1}") 
    .replace(/e\^([-+]?[0-9]*\.?[0-9]+[x]?)/g, "e^{$1}")
    .replace(/e\^\(([^)]+)\)/g, "e^{$1}")
    .replace(/³/g, "^3")
    .replace(/²/g, "^2")
    .replace(/\^([-+]?[0-9]*\.?[0-9]+[x]?)/g, "^{$1}") 
    .replace(/\+-/g, "-")
    .replace(/sin/gi, "\\sin")
    .replace(/cos/gi, "\\cos")
    .replace(/tg/gi, "\\tan")     
    .replace(/sqrt\(([^)]+)\)/gi, "\\sqrt{$1}")
    .replace(/abs\(([^)]+)\)/gi, "\\left|$1\\right|")
    .replace(/--/g, "+");
}

export default function InterpolationGraph({ originalFormula, targetX, result }) {
  const containerRef = useRef(null);
  const [calc, setCalc] = useState(null);

  useEffect(() => {
    if (!containerRef.current) return;
    const calculator = Desmos.GraphingCalculator(containerRef.current, {
      expressions: true,
      keypad: false,
      settingsMenu: false,
    });
    setCalc(calculator);
    return () => calculator.destroy();
  }, []);

  useEffect(() => {
    if (!calc) return;
    calc.setBlank();

    if (originalFormula) {
      calc.setExpression({
        id: "original",
        latex: `y=${toLatex(originalFormula)}`,
        color: "#000000",
        lineStyle: Desmos.Styles.DASHED,
      });
    }

    if (!result) return;

    if (Array.isArray(result.sourcePoints) && result.sourcePoints.length > 0) {
      const pointsLatex = result.sourcePoints
        .map(p => `(${p.x},${p.y})`)
        .join(",");
      
      calc.setExpression({
        id: "source_points",
        latex: pointsLatex,
        color: Desmos.Colors.RED,
        pointStyle: Desmos.Styles.POINT,
        pointSize: 10
      });
    }

    const tX = Number(String(targetX).trim().replace(",", "."));
    if (!isNaN(tX)) {
      calc.setExpression({
        id: "target_line",
        latex: `x=${tX}`,
        color: "#FFA500", 
        lineStyle: Desmos.Styles.DASHED,
        lineWidth: 1.5
      });
    }

    if (Array.isArray(result.results)) {
      const pointsList = [];
      result.results.forEach((res, i) => {
        if (res.isSuccess && res.value != null && !isNaN(res.value)) {
          pointsList.push(`(${tX},${res.value})`);
          
          calc.setExpression({
            id: `sol_point_${i}`,
            latex: `(${tX},${res.value})`,
            color: Desmos.Colors.BLUE,
            pointStyle: Desmos.Styles.POINT,
            pointSize: 12,
            label: res.methodName,
            showLabel: i === 0 
          });
        }
      });
    }
  }, [calc, originalFormula, targetX, result]); 

  return (
    <div style={{ marginTop: 20 }}>
      <div ref={containerRef} style={{ width: "100%", height: 500, borderRadius: 8, border: "1px solid #ddd" }} />
      <p style={{ fontSize: "0.85rem", color: "#666", textAlign: "center" }}>
        Чёрный пунктир — идеальная функция (если сгенерировано). Красные точки — узлы интерполяции. Оранжевый пунктир — целевой X. Синие точки — интерполированные значения.
      </p>
    </div>
  );
} 
