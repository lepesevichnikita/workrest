$('#user-cards')
.dimmer('show');

$.get('src/template/user/card.html', (cardBody) => {
  const users = [{
    id: 1,
    login: "Mathew",
    currentState: "Unverified",
    hasFreelancerProfile: false,
    hasCustomerProfile: false
  }, {
    id: 2,
    login: "Allan",
    currentState: "Blocked",
    hasFreelancerProfile: true,
    hasCustomerProfile: false
  }, {
    id: 3,
    login: "Peter",
    currentState: "Unverified",
    hasFreelancerProfile: false,
    hasCustomerProfile: false
  }, {
    id: 4,
    login: "Jessika",
    currentState: "Blocked",
    hasFreelancerProfile: true,
    hasCustomerProfile: false
  }, {
    id: 5,
    login: "Maria",
    currentState: "Verified",
    hasFreelancerProfile: false,
    hasCustomerProfile: false
  }, {
    id: 6,
    login: "Carl",
    currentState: "Verified",
    hasFreelancerProfile: true,
    hasCustomerProfile: true
  }];
  $('#user-cards')
  .dimmer('hide');
  $.tmpl(cardBody, users)
   .appendTo('#user-cards');
});
