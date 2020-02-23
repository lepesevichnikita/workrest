$('#request-cards')
.dimmer('show');

$.get('src/template/request/card.html', (cardBody) => {
  const requests = [
    {id: 1, login: "Mathew"},
    {id: 2, login: "Allan"},
    {id: 3, login: "Peter"},
    {id: 4, login: "Jessika"},
    {id: 5, login: "Maria"},
    {id: 6, login: "Carl"}
  ];
  $('#request-cards')
  .dimmer('hide');
  $.tmpl(cardBody, requests)
   .appendTo('#request-cards');
});
