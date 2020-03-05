export const endpoint = {
  token: {
    verify: "token/verify", root: "token"
  }, users: {
    root: "users",
    all: "users/all",
    by_id: id => `users/${id}`,
    block_by_id: id => `users/${id}/block`,
    unblock_by_id: id => `users/${id}/unblock`
  }, personal_data: {
    root: "personal_data",
    all: "personal_data/all",
    reject_by_id: id => `personal_data/${id}/reject`,
    approve_by_id: id => `personal_data/${id}/approve`
  }, file: {
    root: "file"
  }
};

export default endpoint;
