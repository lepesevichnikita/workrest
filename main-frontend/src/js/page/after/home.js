{
  $('a.ui').click(function(event) {
    event.preventDefault();
    const item = $(this);
    const pageName = item.attr("name");
    redirectToPage(pageName);
  });
}
