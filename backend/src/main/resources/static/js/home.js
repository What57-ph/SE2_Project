
const ctx = document.querySelector(".saving-line-chart").getContext("2d");
const income = document.querySelector(".income-donut-chart");
const expense = document.querySelector(".expense-donut-chart");
const totalSavingContainer = document.querySelector(".total-saving-review");
const labelsIncome = ["Salary", "Freelance"];
const incomeBgColor = ["rgb(255, 99, 132)", "rgb(54, 162, 235)"];
const labelsSaving = ["", "Mar", "Jun", "Sep", "Dec"];
const labelsExpense = ["Rent", "Retail", "Utilities", "Dining Out"];
const expenseBgColor = [
    "rgb(54, 170, 200)",
    "rgb(54, 162, 235)",
    "rgb(54, 162,200)",
    "rgb(54, 162,150)",
];
const reviewCategory = document.querySelectorAll(".filter-options li");
let totalSavingData = [{
    month: "January",
    income: "$6500",
    expense: "$3655",
},
    {
        month: "February",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "March",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "April",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "May",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "June",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "July",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "August",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "September",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "October",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "November",
        income: "$6500",
        expense: "$3655"
    }, {
        month: "December",
        income: "$6500",
        expense: "$3655"
    }
];

function setActive(element) {
    reviewCategory.forEach(item => {
        item.classList.remove("active");


    });
    totalSavingContainer.innerHTML="";
    element.classList.add("active");
    totalSavingInfo(element.textContent);
}
let totalSavingInfo= (categoryName)=>{
    for (let i = 0; i < totalSavingData.length; i++) {
        let index=0;
        let circleColor = 0 <= i && i <= 2 ? "blue" : 3 <= i && i <= 5 ? "green" : 6 <= i && i <= 8 ? "orange" : "red"
        totalSavingContainer.innerHTML += `
          <div class="monthly-saving-box rounded shadow-sm bg-opacity-100 p-3 col-xl-2 col-3">
            <h5 class="h5 fw-semibold mb-3">
              <i class="fa-solid fa-circle-dot" style="color:${circleColor}"></i>
              &nbsp;&nbsp;${totalSavingData[i].month}
            </h5>
            <p>Income:&nbsp;${totalSavingData[0].income}</p>
            <p>Expense:&nbsp;${totalSavingData[0].expense}</p>
          </div>`;
        if (categoryName==="Quarter 1"){
            if (i===2){
                return;
            }
        } else if (categoryName==="Quarter 2"){
            if (i<3) totalSavingContainer.innerHTML="";
            if (i===5) return;
        } else if (categoryName==="Quarter 3"){
            if (i<6) totalSavingContainer.innerHTML="";
            if (i===8) return;
        } else if (categoryName==="Quarter 4"){
            if (i<9) totalSavingContainer.innerHTML="";
        }
    }
}
totalSavingInfo();

let expenseLegend = document.querySelector(".custom-legend-expense");

for (var i = 0; i < labelsExpense.length; i++) {
    expenseLegend.innerHTML += `<span class="legend-item">
                        <span class="legend-color" style="background-color:${expenseBgColor[i]}"></span> ${labelsExpense[i]}
                    </span>`;
}
let incomeLegend = document.querySelector(".custom-legend-income");

for (var i = 0; i < labelsIncome.length; i++) {
    incomeLegend.innerHTML += `<span class="legend-item">
                        <span class="legend-color" style="background-color:${incomeBgColor[i]}"></span> ${labelsIncome[i]}
                    </span>`;

}
const gradient = ctx.createLinearGradient(0, 0, 0, 400);
gradient.addColorStop(0, "rgba(75, 192, 192, 0.5)");
gradient.addColorStop(1, "rgba(255, 255, 255, 0)");
//Saving chart
new Chart(ctx, {
    type: "line",
    data: {
        labels: labelsSaving,
        datasets: [{
            label: "Balance",
            data: [0, 7.2, 15, 5, 6],
            borderWidth: 2,
            borderColor: "rgb(75, 192, 192)",
            backgroundColor: gradient,
            tension: 0.4,
        }, ],
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
    },
});
//Income chart
new Chart(document.querySelector(".income-donut-chart"), {
    type: "doughnut",
    data: {
        labels: ["Salary", "Freelance"],
        datasets: [{
            data: [5000, 3000],
            backgroundColor: incomeBgColor,
            hoverOffset: 4,
            borderWidth: 1,
            weight: 5,
        }, ],
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        cutoutPercentage: 85,

        legend: {
            display: false,
        },
    },
});
//Expense chart
new Chart(document.querySelector(".expense-donut-chart"), {
    type: "doughnut",
    data: {
        labels: labelsExpense,
        datasets: [{
            data: [5000, 3000, 2000, 1000],
            backgroundColor: expenseBgColor,
            hoverOffset: 4,
            borderWidth: 1,
            weight: 5,
        }, ],
    },
    options: {
        legend: {
            display: false,
        },
        responsive: true,
        maintainAspectRatio: false,
        cutoutPercentage: 85,
    },
});
