import Page from './Page.js';

export class Freelancers extends Page {
  constructor(props) {
    super(props);
    this._cardTemplateName = 'freelancer/card';
    this._popupTemplateName = 'freelancer/popup';
    this._containerSelector = '#freelancer-cards';
    this._cardDescriptionSelector = '.ui.card > .content > .description';
    this._modalsSelector = '.ui.modals';
    this._cardSelector = '.ui.link.card';
  }

  static _getModalSelectorById(id) {
    return `#freelancer${id}.ui.modal`;
  }

  _loadData() {
    freelancerService.getFreelancers().then(response => {
      const freelancers = response.body;
      $.get(templateHelper.getTemplatePath(this._cardTemplateName)).
        done(cardBody => {
          $.tmpl(cardBody, freelancers).appendTo(this._containerSelector);
          limitContentText(this._cardDescriptionSelector,
                           Freelancers.DEFAULT_MAX_DESCRIPTION_LENGTH);
          $.get(templateHelper.getTemplatePath(this._popupTemplateName)).
            done(popupBody => {
              $(this._modalsSelector).remove(); // Remove previous placed by Semantic UI modals
              $.tmpl(popupBody, freelancers).appendTo(this._containerSelector);
              $(this._cardSelector).unbind();
              $(this._cardSelector).click(function(event) {
                event.preventDefault();
                const id = $(this).attr('id');
                $(Freelancers._getModalSelectorById(id)).modal('show');
              });
            });
        });
    });
  }

  process() {
    checkIsAuthorized().then(() => {
      replacePage('freelancers').then(() => {
        this._loadData();
        super.process();
      });
    }).catch(() => redirectToPage('login'));
  }
}

Freelancers.DEFAULT_MAX_DESCRIPTION_LENGTH = 70;

export default Freelancers;