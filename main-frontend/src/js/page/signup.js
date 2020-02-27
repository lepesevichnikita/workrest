checkIsAuthorized().
then(() => redirectToPage('home')).
catch(() => replacePage('signup').then(() => {
  const form = document.getElementById('signupForm');
  const sendForm = formData => authorizationService.signUp(formData).
                                                    then(() => $(form).
                                                    dimmer('hide'));
  defineFormSubmitCallback(form, sendForm);
}));
