{
  const form = document.getElementById('signupForm');
  const sendForm = formData => authorizationService.signUp(formData)
                                                   .then(response => {
                                                     redirectToPage('login');
                                                     $(form)
                                                     .dimmer('hide');
                                                   });
  defineFormSubmitCallback(form, sendForm);
}
