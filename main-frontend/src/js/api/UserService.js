import user from './user.js';

export class UserService {
  getCurrentUser() {
    return new Promise((resolve, reject) => {
      setTimeout(() => resolve({body: user}), 100);
    });
  }
}

export default UserService;
