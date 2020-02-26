{
  const loadData = () => {
    const users = [
      {
        id: 1,
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
      $.tmpl(cardBody, users).appendTo('#freelancer-cards');
      $.get(templateHelper.getTemplatePath('freelancer/popup'), popupBody => {
        $.tmpl(popupBody, users).appendTo('#freelancer-cards');
        $('#freelancer-cards').dimmer('hide');
        $('.ui.link.card').click(function(event) {
          event.preventDefault();
          const id = $(this).attr('id');
          console.dir(this);
          $(`#freelancer${id}.ui.modal`).modal('show');
        });
      });
    });
  };

  loadData();
}
