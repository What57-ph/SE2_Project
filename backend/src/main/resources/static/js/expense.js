
function makeItemActive(selectorList) {
    selectorList.forEach(item => {
        item.addEventListener("click", function () {
            selectorList.forEach(i => i.classList.remove("active"));
            this.classList.add("active");
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
        previous: previousDay.toISOString().split('T')[0],
        next: nextDay.toISOString().split('T')[0]
    };
}
document.addEventListener("DOMContentLoaded", function () {
    const sidebarItems = document.querySelectorAll(".login-sidebar p");
    const pageItems = document.querySelectorAll(".page-item");
    const categoryItems=document.querySelectorAll(".category-list-item");
    const dayInput=document.getElementById("createdDate");
    const previousDay=document.getElementById("previous-day");
    const nextDay=document.getElementById("next-day");

    makeItemActive(sidebarItems);
    makeItemActive(pageItems);
    makeItemActive(categoryItems);

    let today = new Date().toISOString().split('T')[0];
    dayInput.value = today;
    previousDay.addEventListener("click", function(){
        dayInput.value=getAdjacentDays(today).previous;
        today=dayInput.value;
    })
    nextDay.addEventListener("click", function(){
        dayInput.value=getAdjacentDays(today).next;
        today=dayInput.value;
    })
});
document.getElementById('confirmDeleteBtn').addEventListener('click', function() {
    // Get the transaction id you want to delete
    const transactionId = 3; // Replace this with the actual id you want to delete

    // Send the DELETE request to the API
    fetch(`/transactions/deleteExpense/${transactionId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                // Handle successful deletion
                console.log('Transaction deleted successfully');
                // Close the modal
                const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
                modal.hide();
            } else {
                // Handle error response from the API
                console.error('Failed to delete transaction');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});


export {makeItemActive, getAdjacentDays};

