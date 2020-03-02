import Component from './Component.js';

export class Page extends Component {

  _defineLinksClick() {
    const linkSelector = '.ui.link';
    document.querySelectorAll(linkSelector).forEach(node => {
      node.addEventListener('click', (event) => {
        event.preventDefault();
        const pageName = node.getAttribute('name');
        redirectToPage(pageName);
      });
    });
  }

  process() {
    this._defineLinksClick();
  }
}

export default Page;
