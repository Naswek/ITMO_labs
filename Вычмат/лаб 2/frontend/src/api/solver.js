import { adaptSamples, adaptFunctionResult, adaptSystemResult, adaptIntegralResult } from "./adapter";

const BASE_URL = "http://localhost:8080/api/v1"; 

async function readJson(res) {
  const json = await res.json().catch(() => null);
  if (!res.ok) throw new Error(json?.error || `HTTP ${res.status}`);
  return json;
}

function unwrapResponse(raw) {
  if (raw?.success === false || raw?.error) throw new Error(raw?.error || "Unknown error");
  return raw;
}

export async function getSamples(type) {
  const res = await fetch(`${BASE_URL}/${type}/samples`);
  const raw = await readJson(res);
  return adaptSamples(raw);
}

export async function solveFunction(type, payload) {
  const res = await fetch(`${BASE_URL}/${type}/function`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  return adaptFunctionResult(unwrapResponse(await readJson(res)));
}

export async function solveSystem(type, payload) {
  const res = await fetch(`${BASE_URL}/${type}/system`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  return adaptSystemResult(unwrapResponse(await readJson(res)));
}

export async function solveIntegral(payload) {
  const res = await fetch(`${BASE_URL}/integrals/function`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  return adaptIntegralResult(unwrapResponse(await readJson(res)));
}