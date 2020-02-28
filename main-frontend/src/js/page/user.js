checkIsAuthorized()
.then(() => {
  replacePage('user');
})
.catch(() => {
  redirectToPage('login');
});