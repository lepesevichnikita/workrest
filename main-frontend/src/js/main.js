let menuContainerId = '#menu';
let homePage = 'src/template/page/home.html';

export const cachedScript = (url, options) => {
  options = $.extend(options || {}, {
    dataType: "script",
    cache: true,
    url: url
  });
  return $.ajax(options);
};

export const loadTemplateAndScript = (selector, link, name, templateData) => {
  $.get(link).done(pageTemplate => {
    const pageHtml = $.tmpl(pageTemplate, templateData);
    $(selector).html(pageHtml);
    cachedScript(`src/js/${name}.js`);
  });
};

export const replacePage = (link, pageName) => {
  document.title = `WorkRest | ${pageName}`;
  loadTemplateAndScript('#page', link, pageName.toLowerCase(), {});
};

window.replacePage = replacePage;
window.cachedScript = replacePage;
window.loadTemplateAndScript = loadTemplateAndScript;

$(menuContainerId).dimmer('show');
$.get('src/template/main/menu.html').done(menuTemplate => {
  $(menuContainerId).dimmer('hide');
  $(menuContainerId).replaceWith($.tmpl(menuTemplate, {}));
  $('.ui.menu a.item').click(function(event) {
    event.preventDefault();
    const item = $(this);
    const link = item.attr('href');
    const pageName = item.text().trim();
    replacePage(link, pageName);
  });
});

replacePage(homePage, 'Home');
