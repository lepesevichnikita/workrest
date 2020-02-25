const loginForm = document.getElementById('loginForm');
const port = 9091;
const url = `http://localhost${port ? `:${port}` : ''}`;
const authUrl = `${url}/token`;

$(loginForm).submit(function(event) {
  event.preventDefault();
  const formData = new FormData(loginForm);
  const formDataAsObject = {};
  formData.forEach((value, key) => {
    formDataAsObject[key] = value;
  });
  $.post(authUrl, formDataAsObject, 'json').done(data => {
    console.dir(data);
  });
});
