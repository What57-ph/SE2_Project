function makeItemActive(selectorList) {
  selectorList.forEach((item) => {
    item.addEventListener("click", function () {
      selectorList.forEach((i) => i.classList.remove("active"));
      this.classList.add("active");
      console.log(item);
    });
  });
}
function getAdjacentDays(dateString) {
  const date = new Date(dateString);

  const previousDay = new Date(date);
  previousDay.setDate(date.getDate() - 1);

  const nextDay = new Date(date);
  nextDay.setDate(date.getDate() + 1);

  return {
    previous: previousDay.toISOString().split("T")[0],
    next: nextDay.toISOString().split("T")[0],
  };
}
document.addEventListener("DOMContentLoaded", function () {
  const pageItems = document.querySelectorAll(".page-item");
  const categoryItems = document.querySelectorAll(".category-list-item");
  const type = document.querySelectorAll(".expense-detail p span");
  const dayInput = document.getElementById("createdDate");
  const previousDay = document.getElementById("previous-day");
  const nextDay = document.getElementById("next-day");
  makeItemActive(pageItems);
  makeItemActive(categoryItems);
  makeItemActive(type);

  let today = new Date().toISOString().split("T")[0];
  dayInput.value = today;
  previousDay.addEventListener("click", function () {
    dayInput.value = getAdjacentDays(today).previous;
    today = dayInput.value;
  });
  nextDay.addEventListener("click", function () {
    dayInput.value = getAdjacentDays(today).next;
    today = dayInput.value;
  });
});
