import { FileService, PersonalDataService } from "../api";
import { Action } from "../constant";
import { checkIsAuthorized, redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class PersonalData extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._fileService = new FileService(props);
    this._personalDataService = new PersonalDataService(props);
    this._requestBody = {};
    this.addListener("#fileChooseButton", ["click", this._onChooseFileButtonClick.bind(this), false])
        .addListener("input[name=file]", ["change", this._onChangeFileInput.bind(this), false])
        .addListener("input[name=firstName]", ["change", this._onFirstNameChange.bind(this), false])
        .addListener("input[name=lastName]", ["change", this._onLastNameChange.bind(this), false])
        .addListener("input[name=documentName]", ["change", this._onDocumentNameChange.bind(this), false])
        .addListener("input[name=documentNumber]", ["change", this._onDocumentNumberChange.bind(this), false])
        .addListener("#personalDataForm", ["submit", this._onPersonalDataFormSubmit.bind(this), false]);
    this._fileService.subscribe(Action.LOADING_PROGRESS, this._onFileLoadingProgress.bind(this));
  }

  process() {
    checkIsAuthorized()
    .then(() => this.replacePage("personal_data"))
    .then(() => super.process())
    .catch(() => redirectToPage("login"));
  }

  _onFileLoadingProgress(event) {
    $("#fileProgressBar")
    .progress("set percent", event.percent);
  }

  _onChooseFileButtonClick(event) {
    event.preventDefault();
    const fileInput = document.querySelector("input[name=file]");
    fileInput.click();
  }

  _onChangeFileInput(event) {
    event.preventDefault();
    const file = event.target.files[0];
    const label = document.querySelector("label[for=fileChooseButton]");
    const progressBar = $("#fileProgressBar");
    progressBar.css("visibility", "visible");
    progressBar.progress("remove error");
    this._fileService.uploadFile(file)
        .then(response => {
          this._requestBody.attachment = response.body;
          progressBar.progress("complete");
          progressBar.css("visibility", "hidden");
          label.textContent = file.name;
        })
        .catch(() => {
          $("#fileProgressBar")
          .progress("set error");
        });
  }

  _onFirstNameChange(event) {
    event.preventDefault();
    this._requestBody.firstName = event.target.value;
  }

  _onLastNameChange(event) {
    event.preventDefault();
    this._requestBody.lastName = event.target.value;
  }

  _onDocumentNameChange(event) {
    event.preventDefault();
    this._requestBody.documentName = event.target.value;
  }

  _onDocumentNumberChange(event) {
    event.preventDefault();
    this._requestBody.documentNumber = event.target.value;
  }

  _onPersonalDataFormSubmit(event) {
    event.preventDefault(event);
    this._personalDataService.updatePersonalData(this._requestBody)
        .then(console.dir)
        .catch(console.warn);
  }
}
