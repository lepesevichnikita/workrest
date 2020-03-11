import { Page } from "./Page.js";

export class Home extends Page {
  constructor() {
    super();
  }

  process() {
    this.replacePage("home")
        .then(() => {
          super.process();
        });
  }
}

export default Home;
