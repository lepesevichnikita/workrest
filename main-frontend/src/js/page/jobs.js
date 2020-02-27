replacePage('jobs').then(() => {
  const jobs = [
    {
      id: 1,
      title: 'Create Facebook',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
      skills: [
        {
          id: 0,
          name: 'PHP',
        }, {
          id: 1,
          name: 'HTML',
        }, {
          id: 2,
          name: 'JS',
        }],
    }, {
      id: 2,
      title: 'Create Twitter',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
      personalData: {
        firstName: 'Allan',
        lastName: 'Cooper',
      },
      skills: [
        {
          id: 0,
          name: 'PHP',
        }, {
          id: 1,
          name: 'HTML',
        }, {
          id: 2,
          name: 'JS',
        }],
    }, {
      id: 3,
      title: 'Create Instagram',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
      skills: [
        {
          id: 0,
          name: 'PHP',
        }, {
          id: 1,
          name: 'HTML',
        }, {
          id: 2,
          name: 'JS',
        }],
    }, {
      id: 4,
      title: 'Create Telegram',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
      skills: [
        {
          id: 0,
          name: 'C++',
        }, {
          id: 1,
          name: 'Qt',
        }, {
          id: 2,
          name: 'JS',
        }],
    }, {
      id: 5,
      title: 'Create everything',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
      skills: [
        {
          id: 0,
          name: 'React',
        }, {
          id: 1,
          name: 'Node.js',
        }, {
          id: 2,
          name: 'JS',
        }],
    }, {
      id: 6,
      title: 'Nothing',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
      skills: [
        {
          id: 0,
          name: 'PHP',
        }, {
          id: 1,
          name: 'HTML',
        }, {
          id: 2,
          name: 'JS',
        }],
    }];
  const cardTemplateName = 'job/card';
  const popupTemplateName = 'job/popup';
  const containerSelector = '#job-cards';
  const loadData = () => {
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
  };

  loadData();
});
