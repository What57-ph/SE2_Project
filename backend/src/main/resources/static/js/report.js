(() => {
  'use strict'

  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  const forms = document.querySelectorAll('.needs-validation')

  // Loop over them and prevent submission
  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }

      form.classList.add('was-validated')
    }, false)
  })
})()
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
