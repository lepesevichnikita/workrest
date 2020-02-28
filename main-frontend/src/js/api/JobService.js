import jobs from './jobs.js';

export class JobService {
  getJobs() {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve({body: jobs}), 100);
    });
  }
}

export default JobService;
