import jobs from "./jobs.js";

export class JobService {
  constructor(props) {
    this._authorizationService = props.authorizationService;
  }

  getJobs() {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve({body: jobs}), 100);
    });
  }
}

export default JobService;
