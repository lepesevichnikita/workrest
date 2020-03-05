import users from "./users.js";

export class UserService {
  getUsers() {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve({body: users}), 100);
    });
  }
}

export default UserService;
