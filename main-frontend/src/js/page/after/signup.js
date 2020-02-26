{
  const form = document.getElementById('signupForm');
  const sendForm = formData => authorizationService.signIn(formData,
                                                           response => {
                                                             redirectToHomePage();
                                                           }, () => {
        $(form).dimmer('hide');
      });
  defineFormSubmitCallback(form, sendForm);
}
