const API_URL = "http://localhost:8080";
const SEPARATOR = "|||";

let tasks = [];
let filter = "all";
let search = "";

async function saveEmail() {
    const email = document.getElementById("configEmail").value;
    if (!email) return alert("Digite um e-mail válido.");

    try {
        const response = await fetch(`${API_URL}/config/email`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: email })
        });
        if (response.ok) alert("✅ E-mail salvo!");
        else alert("❌ Erro ao salvar.");
    } catch (e) { console.error(e); }
}

function startTelegramFlow(botUrl) {
    window.open(botUrl, '_blank');
}

async function linkTelegram() {
    try {
        document.body.style.cursor = 'wait';
        const response = await fetch(`${API_URL}/config/telegram/vincular`, { method: 'POST' });
        document.body.style.cursor = 'default';

        const msg = await response.text();
        if (response.ok) alert("🎉 " + msg);
        else alert("⚠️ " + msg);
    } catch (e) {
        document.body.style.cursor = 'default';
        alert("Erro de conexão.");
    }
}

async function fetchTasks() {
    try {
        const response = await fetch(`${API_URL}/tarefas`);
        const data = await response.json();

        tasks = data.map(t => {
            const parts = t.descricao.split(SEPARATOR);
            let desc = t.descricao, date = "", time = "";

            if (parts.length >= 3) {
                desc = parts[0];
                date = parts[1];
                time = parts[2];
            }

            const isCompleted = (t.status === true);

            return {
                id: t.id,
                description: desc,
                date: date,
                time: time,
                completed: isCompleted,
                originalFullDesc: t.descricao
            };
        });
        renderList();
    } catch (e) { console.error(e); }
}

async function createTask(e) {
    e.preventDefault();
    const desc = document.getElementById("desc").value;
    const date = document.getElementById("date").value;
    const time = document.getElementById("time").value;

    const fullDesc = `${desc}${SEPARATOR}${date}${SEPARATOR}${time}`;

    try {
        await fetch(`${API_URL}/tarefas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ descricao: fullDesc })
        });
        fetchTasks();
        document.getElementById("taskForm").reset();
    } catch (e) { console.error(e); }
}

async function toggleStatus(id) {
    const task = tasks.find(t => t.id == id);
    if (!task) return;

    try {
        await fetch(`${API_URL}/tarefas/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                descricao: task.originalFullDesc,
                status: !task.completed
            })
        });
        fetchTasks();
    } catch (e) { console.error(e); }
}

async function deleteTask(id) {
    if (!confirm("Remover esta tarefa?")) return;
    try {
        await fetch(`${API_URL}/tarefas/${id}`, { method: 'DELETE' });
        fetchTasks();
    } catch (e) { console.error(e); }
}

function renderList() {
    const list = document.getElementById("list");
    const empty = document.getElementById("emptyState");
    list.innerHTML = "";

    let filtered = tasks.filter(t => {
        if (filter === "pending") return !t.completed;
        if (filter === "completed") return t.completed;
        return true;
    });

    if (search) filtered = filtered.filter(t => t.description.toLowerCase().includes(search));

    if (filtered.length === 0) {
        empty.style.display = "block";
        return;
    }
    empty.style.display = "none";

    filtered.forEach(t => {
        const dateDisplay = t.date ? `${formatDate(t.date)} às ${t.time}` : "";

        const card = document.createElement("div");
        card.className = `card ${t.completed ? 'completed' : ''}`;

        card.innerHTML = `
            <div class="card-info">
                <h4>${t.description}</h4>
                <div class="card-meta">
                    ${dateDisplay ? `<span><i class="far fa-calendar"></i> ${dateDisplay}</span>` : ''}
                </div>
            </div>
            <div class="card-actions">
                <button class="btn-icon check ${t.completed ? 'checked' : ''}" 
                        onclick="handleToggle(${t.id})" 
                        title="Concluir">
                    <i class="fas fa-check-circle"></i>
                </button>
                <button class="btn-icon delete" 
                        onclick="handleDelete(${t.id})" 
                        title="Excluir">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        `;
        list.appendChild(card);
    });
}

function formatDate(s) {
    if (!s) return "";
    const [y, m, d] = s.split("-");
    return `${d}/${m}/${y}`;
}

window.handleToggle = function(id) { toggleStatus(id); }
window.handleDelete = function(id) { deleteTask(id); }

document.getElementById("taskForm").onsubmit = createTask;

document.getElementById("search").oninput = (e) => {
    search = e.target.value.toLowerCase();
    renderList();
};

document.querySelectorAll(".filter-btn").forEach(b => {
    b.onclick = function() {
        document.querySelectorAll(".filter-btn").forEach(btn => btn.classList.remove("active"));
        this.classList.add("active");
        filter = this.dataset.status;
        renderList();
    }
});

fetchTasks();