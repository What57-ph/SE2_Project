function makeItemActive(selectorList) {
  selectorList.forEach((item) => {
    item.addEventListener("click", function () {
      selectorList.forEach((i) => i.classList.remove("active"));
      this.classList.add("active");
    });
  });
}

function formatMonthYear(date) {
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${month}/${year}`;
}

function formatDayMonth(date) {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  return `${day}/${month}`;
}

function getMonthScope(date) {
  const firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
  const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
  return {
    first: formatDayMonth(firstDay),
    last: formatDayMonth(lastDay),
  };
}

const createReportChart = (type) => {
  const ctx = document.getElementById(`${type}-chart`);
  document.getElementById(`${type}-chart`).style.width = "300px";
  document.getElementById(`${type}-chart`).style.height = "300px";
  return new Chart(ctx, {
    type: "pie",
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: true,
          position: "bottom",
        },
      },
    },
    data: {
      labels: ["Food", "Save", "Debt", "Travel", "Drink"],
      datasets: [
        {
          label: "Expenses Breakdown",
          data: [500000, 50000, 40000, 60000, 70000],
          backgroundColor: [
            "rgb(255, 99, 132)",
            "rgb(54, 162, 235)",
            "rgb(255, 205, 86)",
            "rgb(75, 192, 192)",
            "rgb(255, 159, 64)",
          ],
          hoverOffset: 4,
          borderWidth: 1,
        },
      ],
    },
  });
};
document.addEventListener("DOMContentLoaded", function () {
  const sidebarItems = document.querySelectorAll(".report-sidebar p");
  const dayInput = document.getElementById("createdDate");
  const previousDay = document.getElementById("previous-day");
  const nextDay = document.getElementById("next-day");
  const overviewBtn = document.querySelectorAll(".transaction-overview button");

  makeItemActive(overviewBtn);
  makeItemActive(sidebarItems);

  let currentDate = new Date();

  function updateDayInput() {
    const monthYear = formatMonthYear(currentDate);
    const monthScope = getMonthScope(currentDate);
    dayInput.value = `${monthYear} (${monthScope.first} - ${monthScope.last})`;
  }

  updateDayInput();

  previousDay.addEventListener("click", function () {
    currentDate.setMonth(currentDate.getMonth() - 1);
    updateDayInput();
  });

  nextDay.addEventListener("click", function () {
    currentDate.setMonth(currentDate.getMonth() + 1);
    updateDayInput();
  });
  const incomeBtn = document.getElementById("show-income");
  const expenseBtn = document.getElementById("show-expense");
  const incomeSection = document.getElementById("income-section");
  const expenseSection = document.getElementById("expense-section");

  incomeBtn.addEventListener("click", function () {
    incomeSection.classList.remove("d-none");
    expenseSection.classList.add("d-none");
    incomeBtn.classList.add("type-active");
    expenseBtn.classList.remove("type-active");
    createReportChart("income");
  });

  expenseBtn.addEventListener("click", function () {
    expenseSection.classList.remove("d-none");
    incomeSection.classList.add("d-none");
    expenseBtn.classList.add("type-active");
    incomeBtn.classList.remove("type-active");
    createReportChart("expense");
  });

  createReportChart("expense");
});
