{
  const loadData = () => {
    const freelancers = [
      {
        id: 1,
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
        personalData: {
          firstName: 'Mat',
          lastName: 'Thomson',
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
        id: 2,
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
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
        personalData: {
          firstName: 'Henrik',
          lastName: 'Sienkievicz',
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
        id: 4,
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
        personalData: {
          firstName: 'Anna',
          lastName: 'Akhmatova',
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
        id: 5,
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
        personalData: {
          firstName: 'Josef',
          lastName: 'Brodsky',
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
        id: 6,
        description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris id odio ac erat gravida accumsan at nec ex. Nam ornare facilisis mauris, eu lobortis ante vestibulum in. Aenean elit lectus, feugiat in efficitur in, lobortis eget dolor. Integer porttitor metus quis neque condimentum, et consectetur ipsum varius. Vestibulum volutpat, est at convallis aliquam, tortor nisl sodales erat, a convallis massa augue ac nisi. Phasellus vel dolor metus. Cras venenatis augue sapien, sed varius nulla vulputate et. Sed in tortor ligula.',
        personalData: {
          firstName: 'Björn',
          lastName: 'Straustrup',
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
      }];
    $.get(templateHelper.getTemplatePath('freelancer/card'), cardBody => {
      $.tmpl(cardBody, freelancers)
       .appendTo('#freelancer-cards');
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
      $.get(templateHelper.getTemplatePath('freelancer/popup'), popupBody => {
        $.tmpl(popupBody, freelancers)
         .appendTo('#freelancer-cards');
        $('#freelancer-cards')
        .dimmer('hide');
        $('.ui.link.card')
        .click(function (event) {
          event.preventDefault();
          const id = $(this)
          .attr('id');
          $(`#freelancer${id}.ui.modal`)
          .modal('show');
        });
      });
    });
  };

  loadData();
}
