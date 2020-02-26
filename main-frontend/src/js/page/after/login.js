{
  const form = document.getElementById('loginForm');
  const sendForm = formData => authorizationService.signIn(formData,
                                                           response => {
                                                             redirectToPage(
                                                                 'home');
                                                           }, () => {
        $(form).dimmer('hide');
      });
  defineFormSubmitCallback(form, sendForm);
}
