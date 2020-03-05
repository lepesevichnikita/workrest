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
    root: "personal_data"
  }
};

export default endpoint;
