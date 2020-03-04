import Page from './Page.js';

export class Home extends Page {
  process() {
    document.title = 'Home';
    replacePage('home')
    .then(() => super.process());
  }
}

export default Home;
