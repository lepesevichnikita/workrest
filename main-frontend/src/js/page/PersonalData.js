import Page from './Page.js';
import {Action, FileService} from '../api';

export class PersonalData extends Page {
  constructor(props) {
    super(props);
    this._fileService = new FileService();
    this._requestBody = {};
    this._eventListeners = {
      '#fileChooseButton': [
        'click',
        this._onChooseFileButtonClick.bind(this),
        false],
      'input[name=file]': ['change', this._onChangeFileInput.bind(this), false],
      'input[name=firstName]': [
        'change',
        this._onFirstNameChange.bind(this),
        false],
      'input[name=lastName]': [
        'change',
        this._onFirstNameChange.bind(this),
        false],
      'input[name=documentName]': [
        'change',
        this._onFirstNameChange.bind(this),
        false],
      'input[name=firstName]': [
        'change',
        this._onFirstNameChange.bind(this),
        false],
    };
  }

  process() {
    checkIsAuthorized().then(() => replacePage('personal_data').then(() => {
      this._addEventListeners();
      this._fileService.subscribe(Action.LOADING_PROGRESS,
                                  this._onFileLoadingProgress.bind(this));
    })).catch(() => {
      redirectToPage('login');
    });
  }

  _onFileLoadingProgress(event) {
    console.dir(event);
  }

  _onChooseFileButtonClick(event) {
    event.preventDefault();
    this._fileInput.click();
  }

  _onChangeFileInput(event) {
    event.preventDefault();
    this._fileService.uploadFile(event.target.files[0]).then(response => {
      this._requestBody.fileInfo = response.body;
    });
  }

  _addOnChangeForFileInput() {
    this._fileInput.addEventListener('change',
                                     this._onChangeFileInput.bind(this), false);
  }

  _addEventListeners() {
    this._initialzieItems();
  }

  _initialzieItems() {
    this._firstNameInput = document.querySelector();
    this._lastNameInput = document.querySelector('input[name=lastName]');
    this._documentNameInput = document.querySelector(
        'input[name=documentName]');
    this._documentNumberInput = document.querySelector(
        'input[name=documentNumber]');
  }
}
