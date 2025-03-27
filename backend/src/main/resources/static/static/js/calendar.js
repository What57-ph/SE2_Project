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

document.addEventListener("DOMContentLoaded", function () {
  const dayInput = document.getElementById("createdDate");
  const previousDay = document.getElementById("previous-day");
  const nextDay = document.getElementById("next-day");

  let currentDate = new Date();

  function updateDayInput() {
    const monthYear = formatMonthYear(currentDate);
    const monthScope = getMonthScope(currentDate);
    dayInput.value = `${monthYear} (${monthScope.first} - ${monthScope.last})`;
    createDateCalendar();
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

  function createDateCalendar() {
    let year = currentDate.getFullYear();
    let month = currentDate.getMonth();
    let transaction = "120.000";
    const day = document.querySelector(".calendar-dates");

    let dayone = new Date(year, month, 1).getDay();
    let lastdate = new Date(year, month + 1, 0).getDate();
    let dayend = new Date(year, month, lastdate).getDay();
    let monthlastdate = new Date(year, month, 0).getDate();

    let lit = "";

    for (let i = dayone; i > 0; i--) {
      lit += `<li class="inactive out-month">${monthlastdate - i + 1}</li>`;
    }
    for (let i = 1; i <= lastdate; i++) {
      let isToday =
        i === new Date().getDate() &&
        month === new Date().getMonth() &&
        year === new Date().getFullYear()
          ? "active"
          : "";
      lit += `<li class="${isToday} in-month">${i} <p>${transaction}</p></li>`;
    }
    for (let i = dayend; i < 6; i++) {
      lit += `<li class="inactive out-month">${i - dayend + 1}</li>`;
    }

    day.innerHTML = lit;
  }

  createDateCalendar();
});
