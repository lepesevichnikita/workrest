checkIsAuthorized().then(() => {
  const cardTemplateName = 'freelancer/card';
  const popupTemplateName = 'freelancer/popup';
  const containerSelector = '#freelancer-cards';
  const maxDescriptionLength = 70;
  const cardDescriptionSelector = '.ui.card > .content > .description';
  const modalsSelector = '.ui.modals';
  const cardSelector = '.ui.link.card';
  const getModalSelectorById = id => `#freelancer${id}.ui.modal`;
  const loadData = () => {
    freelancerService.getFreelancers().then(response => {
      const freelancers = response.body;
      $.get(templateHelper.getTemplatePath(cardTemplateName)).done(cardBody => {
        $.tmpl(cardBody, freelancers).appendTo(containerSelector);
        limitContentText(cardDescriptionSelector, maxDescriptionLength);
        $.get(templateHelper.getTemplatePath(popupTemplateName)).
          done(popupBody => {
            $(modalsSelector).remove(); // Remove previous placed by Semantic UI modals
            $.tmpl(popupBody, freelancers).appendTo(containerSelector);
            $(cardSelector).unbind();
            $(cardSelector).click(function(event) {
              event.preventDefault();
              const id = $(this).attr('id');
              $(getModalSelectorById(id)).modal('show');
            });
          });
      });
    });
  };
  replacePage('freelancers').then(() => loadData());
}).catch(() => redirectToPage('login'));
