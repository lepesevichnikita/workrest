import freelancers from './freelancers.js';

export class FreelancerService {
  getFreelancers() {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve({body: freelancers}), 100);
    });
  }
}

export default FreelancerService;
