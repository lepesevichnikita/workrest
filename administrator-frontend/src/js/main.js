let menuContainerId = '#menu';
let homePage = 'src/template/page/home.html';

const cachedScript = (url, options) => {
  options = $.extend(options || {}, {
    dataType: "script",
    cache: true,
    url: url
  });
  return $.ajax(options);
};

const replacePage = (link, pageName) => {
  document.title = `WorkRest | ${pageName}`;
  $.get(link)
   .done((pageTemplate) => {
     const templateData = {};
     const pageHtml = $.tmpl(pageTemplate, templateData);
     $('#page')
     .html(pageHtml);
     cachedScript(`src/js/${pageName.toLowerCase()}.js`);
   })
   .fail((xhr) => console.warn(link, xhr.statusText));
};

$(menuContainerId)
.dimmer('show');
$.get('src/template/main/main.html')
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
