export const endpoint = {
  token: {
    verify: "token/verify", root: "token"
  },
  users: {
    root: "users", freelancer: "users/freelancer", employer: "users/employer"
  },
  freelancers: {
    all: "freelancers/all"
  },
  file: {
    root: "file", by_id: id => `file/${id}`
  }, jobs: {
    root: "jobs",
    all: "jobs/all",
    finish_by_id: id => `jobs/${id}/finish`,
    start_by_id: id => `jobs/${id}/start`
  }, personal_data: {
    root: "personal_data"
  }
};

export default endpoint;
