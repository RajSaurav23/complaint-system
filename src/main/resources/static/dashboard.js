let pieChart, barChart;

/* ================= UPDATE STATUS ================= */
function updateStatus(id, status) {

    fetch(`http://localhost:8081/complaint/update/${id}/${status}`, {
        method: "PUT"
    })
    .then(res => res.text())
    .then(() => {
        alert("Status Updated ✅");
        loadDashboard();
    })
    .catch(() => {
        alert("Error ❌");
    });
}

/* ================= POPUP ================= */
function openForm() {
    document.getElementById("formPopup").style.display = "block";
}

function closeForm() {
    document.getElementById("formPopup").style.display = "none";
}

/* ================= ADD COMPLAINT ================= */
function addComplaint() {

    let name = document.getElementById("name").value;
    let issue = document.getElementById("issue").value;

    if (!name || !issue) {
        alert("Please fill all fields");
        return;
    }

    fetch("http://localhost:8081/complaint/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: name,
            issue: issue,
            status: "PENDING"
        })
    })
    .then(res => res.text())
    .then(() => {
        alert("Complaint Added ✅");
        closeForm();
        loadDashboard();
    })
    .catch(err => {
        console.error(err);
        alert("Error adding complaint ❌");
    });
}

/* ================= LOGOUT ================= */
function logout() {
    window.location.href = "login.html";
}

/* ================= LOAD DASHBOARD ================= */
function loadDashboard() {

    fetch("http://localhost:8081/dashboard")
    .then(res => res.json())
    .then(data => {

        // KPI
        document.getElementById("total").innerText = data.total || 0;
        document.getElementById("pending").innerText = data.pending || 0;
        document.getElementById("progress").innerText = data.inProgress || 0;
        document.getElementById("resolved").innerText = data.resolved || 0;

        // TABLE
        let table = "";

        (data.allComplaints || []).forEach(c => {
            table += `
                <tr>
                    <td>${c.id}</td>
                    <td>${c.name}</td>
                    <td>${c.issue}</td>
                    <td>${c.status}</td>
                    <td>
                        <button onclick="updateStatus(${c.id}, 'IN_PROGRESS')">Start</button>
                        <button onclick="updateStatus(${c.id}, 'RESOLVED')">Resolve</button>
                    </td>
                </tr>
            `;
        });

        document.getElementById("tableData").innerHTML = table;

        // DESTROY OLD CHARTS
        if (pieChart) pieChart.destroy();
        if (barChart) barChart.destroy();

        // PIE CHART
        pieChart = new Chart(document.getElementById("pieChart"), {
            type: 'pie',
            data: {
                labels: ["Resolved", "In Progress", "Pending"],
                datasets: [{
                    data: [data.resolved, data.inProgress, data.pending],
                    backgroundColor: ["#4CAF50", "#2196F3", "#FFC107"]
                }]
            }
        });

        // BAR CHART
        barChart = new Chart(document.getElementById("barChart"), {
            type: 'bar',
            data: {
                labels: ["Total", "Pending", "Progress", "Resolved"],
                datasets: [{
                    label: "Complaints",
                    data: [data.total, data.pending, data.inProgress, data.resolved],
                    backgroundColor: "#009688"
                }]
            }
        });

    })
    .catch(err => {
        console.error(err);
        alert("Failed to load dashboard ❌");
    });
}

/* ================= AUTO LOAD ================= */
loadDashboard();
setInterval(loadDashboard, 10000);