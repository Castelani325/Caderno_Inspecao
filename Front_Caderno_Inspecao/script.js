const API_BASE_URL = 'http://localhost:8081'; // Confirme se é 8080 ou 8081

document.addEventListener('DOMContentLoaded', () => {
    loadCadernos();
});

// --- Funções para Cadernos ---

async function loadCadernos() {
    const cadernosContainer = document.getElementById('cadernosContainer');
    cadernosContainer.innerHTML = '<p>Carregando cadernos...</p>';

    try {
        // ATENÇÃO: O endpoint no seu Java deve ser /caderno ou /caderno. 
        // Vou assumir o plural padrão, mas verifique seu Controller.
        const response = await fetch(`${API_BASE_URL}/caderno`); 
        
        if (!response.ok) throw new Error('Erro ao carregar cadernos');
        const cadernos = await response.json();

        cadernosContainer.innerHTML = '';

        if (cadernos.length === 0) {
            cadernosContainer.innerHTML = '<p>Nenhum caderno encontrado. Crie um novo!</p>';
            return;
        }

        cadernos.forEach(caderno => {
            // ADAPTAÇÃO 1 e 2: Usando 'Tipo_de_caderno' e 'cadernoId' que vêm do Java
            const nomeExibicao = caderno.Tipo_de_caderno || caderno.tipo_de_caderno; // Tenta maiúsculo ou minúsculo por segurança
            const idExibicao = caderno.cadernoId; 
            
            // ADAPTAÇÃO 3: A lista de tarefas no Java chama 'tarefas'
            const listaTarefas = caderno.tarefas || []; 

            const cadernoCard = document.createElement('div');
            cadernoCard.className = 'caderno-card';
            cadernoCard.innerHTML = `
                <h3>${nomeExibicao} (ID: ${idExibicao})</h3>
                <div class="caderno-actions">
                    <button class="add-task-btn" onclick="openTaskModal(${idExibicao}, null)">Adicionar Tarefa</button>
                    <button onclick="deleteCaderno(${idExibicao})">Deletar Caderno</button>
                </div>
                <ul class="task-list" id="taskList-${idExibicao}">
                </ul>
            `;
            cadernosContainer.appendChild(cadernoCard);
            
            // Passamos a lista correta de tarefas
            loadTasksForCaderno(idExibicao, listaTarefas); 
        });

    } catch (error) {
        console.error('Erro ao carregar cadernos:', error);
        cadernosContainer.innerHTML = `<p style="color: red;">Erro ao carregar cadernos: ${error.message}. Sua API está rodando?</p>`;
    }
}

async function createCaderno() {
    const cadernoNomeInput = document.getElementById('cadernoNome');
    const nome = cadernoNomeInput.value.trim();

    if (!nome) {
        alert('Por favor, insira um nome para o caderno.');
        return;
    }

    try {
                const payload = {
            Tipo_de_caderno: nome
        };

        const response = await fetch(`${API_BASE_URL}/caderno`, { 
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) throw new Error('Erro ao criar caderno');
        
        cadernoNomeInput.value = '';
        loadCadernos();
    } catch (error) {
        console.error('Erro ao criar caderno:', error);
        alert(`Erro ao criar caderno: ${error.message}`);
    }
}

async function deleteCaderno(cadernoId) {
    if (!confirm('Tem certeza que deseja deletar este caderno e todas as suas tarefas?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/caderno/${cadernoId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Erro ao deletar caderno');
        
        loadCadernos();
    } catch (error) {
        console.error('Erro ao deletar caderno:', error);
        alert(`Erro ao deletar caderno: ${error.message}`);
    }
}


// --- Funções para Tarefas (Quase sem alterações, exceto status/description se mudou no Java) ---

function loadTasksForCaderno(cadernoId, tasks) {
    const taskListElement = document.getElementById(`taskList-${cadernoId}`);
    taskListElement.innerHTML = '';

    if (!tasks || tasks.length === 0) {
        taskListElement.innerHTML = '<li style="font-style: italic; color: #666;">Nenhuma tarefa neste caderno.</li>';
        return;
    }

    tasks.forEach(task => {
        const taskItem = document.createElement('li');
        // Verifique se no Java é 'status' e 'description' mesmo. Se for diferente, mude aqui.
        taskItem.className = `task-item ${task.status === 'COMPLETED' ? 'completed' : ''}`;
        taskItem.innerHTML = `
            <span>${task.description}</span>
            <div class="task-actions">
                <button class="toggle-status-btn" onclick="toggleTaskStatus(${task.id}, '${task.status}')">
                    ${task.status === 'PENDING' ? 'Marcar como Concluída' : 'Marcar como Pendente'}
                </button>
                <button class="edit-task-btn" onclick="openTaskModal(${cadernoId}, ${task.id}, '${task.description}')">Editar</button>
                <button class="delete-task-btn" onclick="deleteTask(${task.id})">Deletar</button>
            </div>
        `;
        taskListElement.appendChild(taskItem);
    });
}

function openTaskModal(cadernoId, taskId = null, description = '') {
    const modal = document.getElementById('taskModal');
    const modalTitle = document.getElementById('modalTitle');
    const modalCadernoId = document.getElementById('modalCadernoId');
    const modalTaskId = document.getElementById('modalTaskId');
    const taskDescriptionInput = document.getElementById('taskDescription');
    const saveTaskButton = document.getElementById('saveTaskButton');

    modalCadernoId.value = cadernoId;
    modalTaskId.value = taskId;
    taskDescriptionInput.value = description;

    if (taskId) {
        modalTitle.textContent = 'Editar Tarefa';
        saveTaskButton.textContent = 'Atualizar Tarefa';
    } else {
        modalTitle.textContent = 'Adicionar Nova Tarefa';
        saveTaskButton.textContent = 'Salvar Tarefa';
    }

    modal.style.display = 'block';
}

function closeModal() {
    document.getElementById('taskModal').style.display = 'none';
}

async function saveTask() {
    const cadernoId = document.getElementById('modalCadernoId').value;
    const taskId = document.getElementById('modalTaskId').value;
    const description = document.getElementById('taskDescription').value.trim();

    if (!description) {
        alert('Por favor, insira uma descrição para a tarefa.');
        return;
    }

    try {
        let response;
        if (taskId) { // Editar tarefa
            response = await fetch(`${API_BASE_URL}/tasks/${taskId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ description: description, status: 'PENDING' })
            });
        } else { // Criar nova tarefa
            // ATENÇÃO: Certifique-se que seu Java usa esta rota aninhada
            response = await fetch(`${API_BASE_URL}/caderno/${cadernoId}/tasks`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ description: description })
            });
        }

        if (!response.ok) throw new Error('Erro ao salvar tarefa');

        closeModal();
        loadCadernos();
    } catch (error) {
        console.error('Erro ao salvar tarefa:', error);
        alert(`Erro ao salvar tarefa: ${error.message}`);
    }
}

async function toggleTaskStatus(taskId, currentStatus) {
    const newStatus = currentStatus === 'PENDING' ? 'COMPLETED' : 'PENDING';
    
    try {
        const getResponse = await fetch(`${API_BASE_URL}/tasks/${taskId}`);
        if (!getResponse.ok) throw new Error('Erro ao buscar detalhes da tarefa');
        const taskDetails = await getResponse.json();

        const response = await fetch(`${API_BASE_URL}/tasks/${taskId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ description: taskDetails.description, status: newStatus })
        });

        if (!response.ok) throw new Error('Erro ao atualizar status da tarefa');
        
        loadCadernos(); 
    } catch (error) {
        console.error('Erro ao atualizar status da tarefa:', error);
        alert(`Erro ao atualizar status da tarefa: ${error.message}`);
    }
}

async function deleteTask(taskId) {
    if (!confirm('Tem certeza que deseja deletar esta tarefa?')) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/tasks/${taskId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Erro ao deletar tarefa');
        
        loadCadernos(); 
    } catch (error) {
        console.error('Erro ao deletar tarefa:', error);
        alert(`Erro ao deletar tarefa: ${error.message}`);
    }
}

window.onclick = function(event) {
    const modal = document.getElementById('taskModal');
    if (event.target == modal) {
        closeModal();
    }
}