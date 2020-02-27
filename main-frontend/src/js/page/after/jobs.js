{
  const loadData = () => {
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
        title: "Nothing",
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
    $.get(templateHelper.getTemplatePath('job/card'), cardBody => {
      $.tmpl(cardBody, jobs)
       .appendTo('#job-cards');
      $('.ui.card > .content > .description')
      .each(function (i) {
        const maxTextLength = 70;
        len = $(this)
        .text().length;
        if (len > maxTextLength) {
          $(this)
          .text($(this)
                .text()
                .substr(0, maxTextLength) + '...');
        }
      });
      $.get(templateHelper.getTemplatePath('job/popup'), popupBody => {
        $.tmpl(popupBody, jobs)
         .appendTo('#job-cards');
        $('#job-cards')
        .dimmer('hide');
        $('.ui.link.card')
        .click(function (event) {
          event.preventDefault();
          const id = $(this)
          .attr('id');
          $(`#job${id}.ui.modal`)
          .modal('show');
        });
      });
    });
  };

  loadData();
}
