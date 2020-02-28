replacePage('jobs').then(() => {
  const cardTemplateName = 'job/card';
  const popupTemplateName = 'job/popup';
  const containerSelector = '#job-cards';
  const loadData = () => {
    jobService.getJobs().then((response) => {
      const jobs = response.body;
      $.get(templateHelper.getTemplatePath(cardTemplateName), cardBody => {
        $.tmpl(cardBody, jobs).appendTo(containerSelector);
        limitContentText('.ui.card > .content > .description', 70);
        $.get(templateHelper.getTemplatePath(popupTemplateName), popupBody => {
          $('.ui.modals').remove();
          $.tmpl(popupBody, jobs).appendTo(containerSelector);
          $(containerSelector).dimmer('hide');
          $('.ui.link.card').unbind();
          $('.ui.link.card').click(function(event) {
            event.preventDefault();
            const id = $(this).attr('id');
            $(`#job${id}.ui.modal`).modal('show');
          });
        });
      });
    });
  };
  loadData();
});
