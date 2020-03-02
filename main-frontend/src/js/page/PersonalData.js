import Page from "./Page.js";
import {FileService, PersonalDataService} from "../api";
import {Action} from "../constant";

export class PersonalData extends Page {
  constructor(props) {
    super(props);
    this._fileService = new FileService();
    this._personalDataService = new PersonalDataService();
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
    .then(() => replacePage("personal_data"))
    .then(() => super.process())
    .catch(() => redirectToPage("login"));
  }

  _onFileLoadingProgress(event) {
    console.dir(event.percent);
  }

  _onChooseFileButtonClick(event) {
    event.preventDefault();
    const fileInput = document.querySelector("input[name=file]");
    fileInput.click();
  }

  _onChangeFileInput(event) {
    event.preventDefault();
    const file = event.target.files[0];
    const form = document.querySelector("#personalDataForm");
    form.className += "loading";
    this._fileService.uploadFile(file)
        .then(response => {
          this._requestBody.fileInfo = response.body;
          const label = document.querySelector("label[for=fileChooseButton]");
          label.textContent = file.name;
          form.className = form.className.replace("loading", "");
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
