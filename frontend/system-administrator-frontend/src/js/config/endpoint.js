export const endpoint = {
  token: {
    verify: "token/verify", root: "token"
  }, file: {
    root: "file"
  }, administrators: {
    root: "administrators", by_id: (id) => `administrators/${id}`
  }
};

export default endpoint;
