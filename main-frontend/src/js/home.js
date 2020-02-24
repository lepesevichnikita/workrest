$('a.ui')
.click(function (event) {
  event.preventDefault();
  const item = $(this);
  const link = item.attr('href');
  const pageName = item.text()
                       .trim();
  replacePage(link, pageName);
});
