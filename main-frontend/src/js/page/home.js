replacePage('home').then(() => {
  const linkSelector = 'div.ui.link';
  $(linkSelector).click(function(event) {
    event.preventDefault();
    const item = $(this);
    const pageName = item.attr('name');
    redirectToPage(pageName);
  });
});
