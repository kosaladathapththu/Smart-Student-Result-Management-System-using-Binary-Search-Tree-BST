const API = "http://localhost:8080";

const out = (msg) => {
  document.getElementById("out").textContent =
    (typeof msg === "string") ? msg : JSON.stringify(msg, null, 2);
};

const val = (id) => document.getElementById(id).value.trim();

function payload() {
  return {
    indexNo: val("indexNo"),
    name: val("name"),
    pdsa: Number(val("pdsa")),
    se: Number(val("se")),
    dm2: Number(val("dm2")),
  };
}

async function addStudent() {
  const res = await fetch(`${API}/students`, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify(payload())
  });
  out(await res.json());
}

async function loadAll() {
  const res = await fetch(`${API}/students`);
  out(await res.json());
}

async function searchStudent() {
  const id = val("indexNo");
  const res = await fetch(`${API}/student?id=${encodeURIComponent(id)}`);
  out(await res.json());
}

async function deleteStudent() {
  const id = val("indexNo");
  const res = await fetch(`${API}/student?id=${encodeURIComponent(id)}`, { method: "DELETE" });
  out(await res.json());
}

async function saveCsv() {
  const res = await fetch(`${API}/save`, { method: "POST" });
  out(await res.json());
}

async function loadCsv() {
  const res = await fetch(`${API}/load`, { method: "POST" });
  out(await res.json());
}


async function updateStudent() {
  const id = val("indexNo");
  const p = payload();
  // backend uses id as the key; other fields from body
  const res = await fetch(`${API}/student?id=${encodeURIComponent(id)}`, {
    method: "PUT",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify(p)
  });
  out(await res.json());
}
