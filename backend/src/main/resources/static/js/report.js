
document.addEventListener('DOMContentLoaded', function() {
  const form = document.getElementById('reportForm');
  const buttons = document.querySelectorAll('.report-type-btn');

  buttons.forEach(button => {
    button.addEventListener('click', function() {
      // Remove active class from all buttons
      buttons.forEach(btn => {
        btn.classList.remove('active');
        btn.classList.replace('btn-primary', 'btn-outline-primary');
      });

      // Add active class to clicked button
      this.classList.add('active');
      this.classList.replace('btn-outline-primary', 'btn-primary');

      // Update form action
      form.action = this.dataset.action;
    });
  });
});
