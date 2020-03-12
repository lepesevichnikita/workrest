import { Page } from "/frontend/src/js/domain/component/index.js";

export class Home extends Page {
  constructor(props) {
    super(props);
  }

  process() {
    this.replacePage("home")
        .then(() => {
          super.process();
        });
  }
}

export default Home;
