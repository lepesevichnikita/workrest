export const endpoint = {
  token: {
    verify: "token/verify", root: "token"
  },
  users: {
    root: "users", freelancer: "users/freelancer", employer: "users/employer"
  },
  file: {
    root: "file", by_id: id => `file/${id}`
  }, jobs: {
    root: "jobs",
    all: "jobs/all"
  }, personal_data: {
    root: "personal_data"
  }
};

export default endpoint;
