import { FileService, PersonalDataService } from "../api";
import { Action } from "../constant";
import { checkIsAuthorized, redirectToPage } from "../main.js";
import { Page } from "./Page.js";

export class PersonalData extends Page {
  constructor(props) {
    super();
    this._authorizationService = props.authorizationService;
    this._fileService = new FileService(props);
    this._personalData = {attachment: null};
    this._personalDataService = new PersonalDataService(props);
    this.addListener(PersonalData.FILE_CHOOSE_BUTTON_SELECTOR, ["click", this._onChooseFileButtonClick.bind(this), false])
        .addListener(PersonalData.FILE_INPUT_SELECTOR, ["change", this._onChangeFileInput.bind(this), false])
        .addListener(PersonalData.FORM_SELECTOR, ["submit", this._onFormSubmit.bind(this), false]);
    this._fileService.subscribe(Action.LOADING_PROGRESS, this._onFileLoadingProgress.bind(this));
  }

  process() {
    this.showDimmer();
    checkIsAuthorized()
    .then(() => this._personalDataService.getPersonalData()
                    .then(response => this._personalData = response.body)
                    .catch(console.error)
                    .finally(() => this.replacePage("personal_data", this._personalData)
                                       .then(() => this._setValidationOnPersonalDataForm())))
    .finally(() => {
      this.hideDimmer();
      super.process();
    })
    .catch(error => {
      error && error.unauthorized && redirectToPage("login");
    });
  }

  _setValidationOnPersonalDataForm() {
    const personalDataForm = $(PersonalData.FORM_SELECTOR);
    personalDataForm.form({
                            firstName: {identifier: "firstName", rules: [{type: "empty", prompt: "First name is required"}]},
                            lastName: {identifier: "lastName", rules: [{type: "empty", prompt: "Last name is required"}]},
                            documentName: {identifier: "documentName", rules: [{type: "empty", prompt: "Document name is required"}]},
                            documentNumber: {identifier: "documentNumber", rules: [{type: "empty", prompt: "Last name is required"}]}
                          }, {
                            onSuccess: this._updatePersonalData.bind(this)
                          });
  }

  _onFormSubmit(event) {
    event.preventDefault();
    const personalDataForm = $(PersonalData.FORM_SELECTOR);
    personalDataForm.form("validate form");
  }

  _updatePersonalData(event, fields) {
    this._personalData = {...this._personalData, ...fields};
    this.showDimmer();
    this._personalDataService.updatePersonalData(this._personalData)
        .then(response => {
          this._personalData = response.body;
          return this._personalData;
        })
        .then(personalData => this.replacePage("personal_data", personalData)
                                  .finally(() => super.process()))
        .catch(error => this.addErrorsToForm(PersonalData.FORM_SELECTOR, error.response.body))
        .finally(() => {
          this.hideDimmer();
        });
  }

  _onFileLoadingProgress(event) {
    const fileProgressBar = $(PersonalData.FILE_PROGRESS_BAR_SELECTOR);
    fileProgressBar.progress("set percent", event.percent);
  }

  _onChooseFileButtonClick(event) {
    event.preventDefault();
    const fileInput = document.querySelector(PersonalData.FILE_INPUT_SELECTOR);
    fileInput.click();
  }

  _onChangeFileInput(event) {
    event.preventDefault();
    const file = event.target.files[0];
    const label = document.querySelector("label[for=fileChooseButton]");
    const progressBar = $(PersonalData.FILE_PROGRESS_BAR_SELECTOR);
    progressBar.css("visibility", "visible");
    progressBar.progress("remove error");
    this._fileService.uploadFile(file)
        .then(response => {
          this._personalData.attachment = response.body;
          progressBar.progress("complete");
          progressBar.css("visibility", "hidden");
          label.textContent = file.name;
        })
        .then(() => $("img")
        .attr("src", this._fileService.getFileUrl(this._personalData.attachment.id)))
        .catch(() => progressBar.progress("set error"));
  }
}

PersonalData.FORM_SELECTOR = "#personalDataForm";
PersonalData.FILE_PROGRESS_BAR_SELECTOR = "#fileProgressBar";
PersonalData.FILE_CHOOSE_BUTTON_SELECTOR = "#fileChooseButton";
PersonalData.FILE_INPUT_SELECTOR = "input[name=file]";

export default PersonalData;
