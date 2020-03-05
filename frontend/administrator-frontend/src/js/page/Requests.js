import { RequestService } from "../api";
import { Page } from "./Page.js";

export class Requests extends Page {
  constructor(props) {
    super(props);
    this._requestService = new RequestService();
  }

  _loadData() {
    $("#request-cards")
    .dimmer("show");
    this._requestService.getRequests()
        .then(response => {
          const requests = response.body;
          $.get("src/template/request/card.html", cardBody => {
            $("#request-cards")
            .dimmer("hide");
            $.tmpl(cardBody, requests)
             .appendTo("#request-cards");
          });
        });
  }

  process() {
    checkIsAuthorized()
    .then(() => {
      this._loadData();
    })
    .then(() => {
      super.process();
    })
    .catch(() => redirectToPage("login"));
  }
}
