checkIsAuthorized().then(() => {
  replacePage('user').then(() => {
    const user = {
      id: 1,
      personalData: {
        firstName: 'Nikola',
        lastName: 'Tesla',
      },
      freelancerProfile: {
        id: 1,
        description: '',
      },
      customerProfile: {
        id: 2,
        description: '',
      },
      currentState: {
        id: 3,
        name: 'Blocked',
      },
    };

  });
}).catch(() => {
  redirectToPage('login');
});