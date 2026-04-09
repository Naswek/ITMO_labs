import React, { useEffect, useRef, useState } from "react";
import * as Desmos from "desmos";

function toLatex(f) {
  if (!f) return "";
  return String(f)
    .replace(/\s+/g, "")       
    .replace(/,/g, ".")       
    .replace(/×/g, "*")      
    .replace(/sin/gi, "\\sin")
    .replace(/cos/gi, "\\cos")
    .replace(/tan/gi, "\\tan") 
    .replace(/³/g, "^3")      
    .replace(/²/g, "^2")       
    .replace(/xy/g, "x*y")     
    .replace(/х/g, "x")        
    .replace(/у/g, "y")
   // .replace(/(\d)([a-zA-Z])/g, "$1*$2");   
}

function formatForDesmos(num) {
  if (num == null) return "0";
  const str = String(num);
  if (str.toLowerCase().includes('e')) {
    return str.replace(/e([+-]?\d+)/i, '\\cdot 10^{$1}');
  }
  return str;
}

export default function DesmosGraph({ mode, formula, system, interval, result }) {
  const containerRef = useRef(null);
  const [calc, setCalc] = useState(null);


  useEffect(() => {
    if (!containerRef.current) return;

    const calculator = Desmos.GraphingCalculator(containerRef.current, {
      expressions: true,
      keypad: false
    });

    setCalc(calculator);

    return () => {
      calculator.destroy();
    };
  }, []);


  useEffect(() => {
    if (!calc) return;

    calc.setBlank();

    if (mode === "function" && formula) {
      calc.setExpression({
        id: "f",
        latex: `y=${toLatex(formula)}`
      });

      if (interval?.a != null && interval?.b != null) {
        calc.setExpression({
          id: "int",
          latex: `${formatForDesmos(interval.a)}<=x<=${formatForDesmos(interval.b)}`
        });
      }

      if (result?.x != null && !result?.message?.includes("нет корня")) {
        calc.setExpression({
          id: "root",
          latex: `(${formatForDesmos(result.x)}, ${formatForDesmos(result.fx)})`
        });
      }
    }


    if (mode === "system" && Array.isArray(system)) {
      system.forEach((eq, i) => {
        let latexStr = toLatex(eq);
        

        if (!latexStr.includes("=")) {
          latexStr += "=0";
        }

        calc.setExpression({
          id: `s${i}`,
          latex: latexStr
        });
      });


      if (result?.x) {
        const [x, y] = Array.isArray(result.x) ? result.x : [result.x, result.fx ?? 0];
        calc.setExpression({
          id: "sol",
          latex: `(${formatForDesmos(x)}, ${formatForDesmos(y)})`
        });
      }
    }
  }, [calc, mode, formula, system, interval, result]);

  return <div ref={containerRef} style={{ width: "100%", height: 450 }} />;
}