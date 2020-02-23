let menuContainerId = '#menu';
let homePage = 'src/template/page/home.html';
let loadScriptAndExecute = (url) => $.get(url)
                                     .done((script) => eval(script))
                                     .fail((xhr) => console.warn(url, xhr.statusText));

const replacePage = (link, pageName) => {
  document.title = `WorkRest | ${pageName}`;
  $.get(link)
   .done((pageTemplate) => {
     const templateData = {};
     const pageHtml = $.tmpl(pageTemplate, templateData);
     $('#page')
     .html(pageHtml);
     loadScriptAndExecute(`src/js/${pageName.toLowerCase()}.js`);
   })
   .fail((xhr) => console.warn(link, xhr.statusText));
};

$(menuContainerId)
.dimmer('show');
$.get('src/template/main/menu.html')
 .done((menuTemplate) => {
   $(menuContainerId)
   .dimmer('hide');
   $(menuContainerId)
   .replaceWith($.tmpl(menuTemplate, {}));
   $('.ui.menu a.item')
   .click(function (event) {
     event.preventDefault();
     const item = $(this);
     const link = item.attr('href');
     const pageName = item.text()
                          .trim();
     replacePage(link, pageName);
   });
 });

replacePage(homePage, 'Home');
