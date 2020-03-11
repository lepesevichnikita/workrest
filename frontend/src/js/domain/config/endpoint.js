export const endpoint = {
  token: {
    verify: "token/verify", root: "token"
  }, users: {
    root: "users",
    all: "users/all",
    by_id: id => `users/${id}`,
    block_by_id: id => `users/${id}/block`,
    unblock_by_id: id => `users/${id}/unblock`,
    freelancer: "users/freelancer", employer: "users/employer"
  }, personal_data: {
    root: "personal_data",
    all: "personal_data/all",
    reject_by_id: id => `personal_data/${id}/reject`,
    approve_by_id: id => `personal_data/${id}/approve`
  }, file: {
    root: "file",
    by_id: id => `file/${id}`
  },
  freelancers: {
    all: "freelancers/all"
  },
  jobs: {
    root: "jobs",
    all: "jobs/all",
    finish_by_id: id => `jobs/${id}/finish`,
    start_by_id: id => `jobs/${id}/start`
  },
  administrators: {
    root: "administrators", by_id: (id) => `administrators/${id}`
  }
};

export default endpoint;
