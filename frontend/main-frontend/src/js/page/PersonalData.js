import { FileService, PersonalDataService } from "/frontend/main-frontend/src/js/api/index.js";
import { AuthorizationService } from "/frontend/src/js/domain/api/index.js";
import { Page } from "/frontend/src/js/domain/component/index.js";
import { Action } from "/frontend/src/js/domain/constant/index.js";

export class PersonalData extends Page {
  constructor(props) {
    super(props);
    this._personalData = {attachment: null};
    this.addListener(PersonalData.FILE_CHOOSE_BUTTON_SELECTOR,
                     ["click", this._onChooseFileButtonClick.bind(this), false])
        .addListener(PersonalData.FILE_INPUT_SELECTOR, ["change", this._onChangeFileInput.bind(this), false])
        .addListener(PersonalData.FORM_SELECTOR, ["submit", this._onFormSubmit.bind(this), false]);
    this._fileService.subscribe(Action.LOADING_PROGRESS, this._onFileLoadingProgress.bind(this));
  }

  get _authorizationService() {
    return this.locator.getServiceByClass(AuthorizationService);
  }

  _setValidationOnPersonalDataForm() {
    const personalDataForm = $(PersonalData.FORM_SELECTOR);
    personalDataForm.form({
                            firstName: {
                              identifier: "firstName",
                              rules: [{type: "empty", prompt: "First name is required"}]
                            },
                            lastName: {
                              identifier: "lastName",
                              rules: [{type: "empty", prompt: "Last name is required"}]
                            },
                            documentName: {
                              identifier: "documentName",
                              rules: [{type: "empty", prompt: "Document name is required"}]
                            },
                            documentNumber: {
                              identifier: "documentNumber",
                              rules: [{type: "empty", prompt: "Last name is required"}]
                            }
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

  get _fileService() {
    return this.locator.getServiceByClass(FileService);
  }

  get _personalDataService() {
    return this.locator.getServiceByClass(PersonalDataService);
  }

  process() {
    this.showDimmer();
    this._authorizationService.checkIsAuthorized()
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
          console.error(error);
          error && error.unauthorized && this.redirectToPage("login");
        });
  }
}

PersonalData.FORM_SELECTOR = "#personalDataForm";
PersonalData.FILE_PROGRESS_BAR_SELECTOR = "#fileProgressBar";
PersonalData.FILE_CHOOSE_BUTTON_SELECTOR = "#fileChooseButton";
PersonalData.FILE_INPUT_SELECTOR = "input[name=file]";

export default PersonalData;
