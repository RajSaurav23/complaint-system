fetch("http://localhost:8081/complaint/all")
.then(function(res) {
    return res.json();
})
.then(function(data) {

    let total = data.length;
    let progress = 0;
    let resolved = 0;
    let table = "";

    data.forEach(function(c) {

        if (c.status === "IN_PROGRESS") progress++;
        if (c.status === "RESOLVED") resolved++;

        table += "<tr>"
            + "<td>" + c.id + "</td>"
            + "<td>" + c.name + "</td>"
            + "<td>" + c.issue + "</td>"
            + "<td>" + c.status + "</td>"
            + "</tr>";
    });

    document.getElementById("tableData").innerHTML = table;

    document.getElementById("total").innerText = total;
    document.getElementById("progress").innerText = progress;

    let percent = total === 0 ? 0 : Math.round((resolved / total) * 100);
    document.getElementById("percent").innerText = percent + "%";

    // PIE
    new Chart(document.getElementById("pieChart"), {
        type: 'pie',
        data: {
            labels: ["Resolved", "In Progress", "Others"],
            datasets: [{
                data: [resolved, progress, total - resolved - progress],
                backgroundColor: ["#4caf50", "#2196f3", "#90a4ae"]
            }]
        }
    });

    // BAR
    new Chart(document.getElementById("barChart"), {
        type: 'bar',
        data: {
            labels: ["Total", "In Progress", "Resolved"],
            datasets: [{
                label: "Complaints",
                data: [total, progress, resolved],
                backgroundColor: "#009688"
            }]
        }
    });

})
.catch(function(err) {
    console.error("Error fetching data:", err);
    alert("Backend not running!");
});