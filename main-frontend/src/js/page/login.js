checkIsAuthorized().
then(response => redirectToPage('home')).
catch(() => replacePage('login').then(() => {
  const form = document.getElementById('loginForm');
  const sendForm = formData => authorizationService.signIn(formData).
                                                    then(response => {
                                                      $(form).dimmer('hide');
                                                    });
  defineFormSubmitCallback(form, sendForm);
}));
