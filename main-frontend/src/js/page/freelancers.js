checkIsAuthorized().then(() => {
  const cardTemplateName = 'freelancer/card';
  const popupTemplateName = 'freelancer/popup';
  const containerSelector = '#freelancer-cards';
  const loadData = () => {
    freelancerService.getFreelancers().then(response => {
      const freelancers = response.body;
      $.get(templateHelper.getTemplatePath(cardTemplateName)).done(cardBody => {
        $.tmpl(cardBody, freelancers).appendTo(containerSelector);
        limitContentText('.ui.card > .content > .description', 70);
        $.get(templateHelper.getTemplatePath(popupTemplateName)).
          done(popupBody => {
            $('.ui.modals').remove();
            $.tmpl(popupBody, freelancers).appendTo(containerSelector);
            $('.ui.link.card').unbind();
            $('.ui.link.card').click(function(event) {
              event.preventDefault();
              const id = $(this).attr('id');
              $(`#freelancer${id}.ui.modal`).modal('show');
            });
          });
      });
    });
  };
  replacePage('freelancers');
  loadData();
}).catch(() => redirectToPage('login'));
