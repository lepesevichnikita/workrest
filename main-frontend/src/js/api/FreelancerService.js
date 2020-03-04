import freelancers from "./freelancers.js";

export class FreelancerService {
  constructor(props) {
    this._authorizationService = props.authorizationService;
  }

  getFreelancers() {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve({body: freelancers}), 100);
    });
  }
}

export default FreelancerService;
