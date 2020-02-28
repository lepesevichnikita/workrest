{
  const cardTemplateName = 'job/card';
  const popupTemplateName = 'job/popup';
  const containerSelector = '#job-cards';
  const maxDescriptionLength = 70;
  const cardDescriptionSelector = '.ui.card > .content > .description';
  const modalsSelector = '.ui.modals';
  const cardSelector = '.ui.link.card';
  const getModalSelectorById = id => `#job${id}.ui.modal`;
  const loadData = () => {
    jobService.getJobs().then(response => {
      const jobs = response.body;
      $.get(templateHelper.getTemplatePath(cardTemplateName), cardBody => {
        $.tmpl(cardBody, jobs).appendTo(containerSelector);
        limitContentText(cardDescriptionSelector, maxDescriptionLength);
        $.get(templateHelper.getTemplatePath(popupTemplateName), popupBody => {
          $(modalsSelector).remove();
          $.tmpl(popupBody, jobs).appendTo(containerSelector);
          $(containerSelector).dimmer('hide');
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
  replacePage('jobs').then(() => {
    loadData();
  });
}
