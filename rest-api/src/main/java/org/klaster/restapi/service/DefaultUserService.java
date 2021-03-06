package org.klaster.restapi.service;

/*
 * workrest
 *
 * 13.02.2020
 *
 */

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.klaster.domain.builder.general.EmployerProfileBuilder;
import org.klaster.domain.builder.general.FreelancerProfileBuilder;
import org.klaster.domain.builder.general.UserBuilder;
import org.klaster.domain.constant.AuthorityName;
import org.klaster.domain.dto.EmployerProfileDTO;
import org.klaster.domain.dto.FreelancerProfileDTO;
import org.klaster.domain.model.context.User;
import org.klaster.domain.model.entity.EmployerProfile;
import org.klaster.domain.model.entity.FreelancerProfile;
import org.klaster.domain.model.entity.LoginInfo;
import org.klaster.domain.model.entity.Skill;
import org.klaster.domain.model.entity.UserAuthority;
import org.klaster.domain.model.state.user.AbstractUserState;
import org.klaster.domain.model.state.user.BlockedUserState;
import org.klaster.domain.model.state.user.DeletedUserState;
import org.klaster.domain.model.state.user.VerifiedUserState;
import org.klaster.domain.repository.FreelancerProfileRepository;
import org.klaster.domain.repository.LoginInfoRepository;
import org.klaster.domain.repository.SkillRepository;
import org.klaster.domain.repository.UserAuthorityRepository;
import org.klaster.domain.repository.UserRepository;
import org.klaster.domain.util.MessageUtil;
import org.klaster.restapi.properties.SystemAdministratorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RegistrationService
 *
 * @author Nikita Lepesevich
 */

@Service
@Transactional
public class DefaultUserService {

  private static String[] userAuthoritiesNames = {AuthorityName.USER};

  @Autowired
  private FreelancerProfileRepository freelancerProfileRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserBuilder defaultUserBuilder;

  @Autowired
  private UserAuthorityRepository userAuthorityRepository;

  @Autowired
  private SystemAdministratorProperties systemAdministratorProperties;

  @Autowired
  private TokenBasedUserDetailsService defaultTokenBasedUserDetailsService;

  @Autowired
  private LoginInfoRepository loginInfoRepository;

  @Autowired
  private EmployerProfileBuilder defaultEmployerProfileBuilder;

  @Autowired
  private FreelancerProfileBuilder defaultFreelancerProfileBuilder;

  @Autowired
  private SkillRepository skillRepository;

  /**
   * Registers user's by passed login info
   * @param loginInfo login and password of user
   * @return registered user
   */
  @Transactional
  public User registerUserByLoginInfo(LoginInfo loginInfo) {
    if (systemAdministratorProperties.getLogin()
                                     .equals(loginInfo.getLogin())) {
      throw new InvalidParameterException();
    }
    LoginInfo savedLoginInfo = loginInfoRepository.save(loginInfo);
    Set<UserAuthority> userAuthorities = userAuthorityRepository.findOrCreateAllByNames(userAuthoritiesNames);
    User user = defaultUserBuilder.setLoginInfo(savedLoginInfo)
                                  .setAuthorities(userAuthorities)
                                  .build();
    return userRepository.saveAndFlush(user);
  }

  /**
   * Changes user's current state to blocked
   * @param id updated user id
   * @return updated user
   */
  @Transactional
  public User deleteById(long id) {
    User deletedUser = userRepository.findById(id)
                                     .orElseThrow(EntityNotFoundException::new);
    deletedUser.setCurrentState(new DeletedUserState());
    defaultTokenBasedUserDetailsService.deleteAllTokensByUserId(deletedUser.getId());
    return userRepository.saveAndFlush(deletedUser);
  }

  /**
   * Changes user current state to blocked
   * @param id updated user id
   * @return updated user
   */
  @Transactional
  public User blockById(long id) {
    User foundUser = userRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    foundUser.setCurrentState(new BlockedUserState());
    return userRepository.saveAndFlush(foundUser);
  }

  /**
   * Unblocks current user if it is blocked.
   * Reverts current user state to previous;
   * @param id updated user id
   * @return updated user
   */
  @Transactional
  public User unblockById(long id) {
    User foundUser = userRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    if (foundUser.getCurrentState() instanceof BlockedUserState) {
      AbstractUserState previousState = foundUser.getPreviousState();
      if (previousState != null) {
        foundUser.setCurrentState(previousState);
      }
    }
    return userRepository.saveAndFlush(foundUser);
  }

  /**
   * Finds first user by passed id
   * @throws EntityNotFoundException if user with passed id doesnt exist
   * @param id required user id
   * @return found user
   */
  @Transactional
  public User findFirstById(long id) throws EntityNotFoundException {
    return userRepository.findById(id)
                         .orElseThrow(() -> new EntityNotFoundException(MessageUtil.getEntityByIdNotFoundMessage(User.class, id)));
  }

  /**
   * Update user's employer profile
   * @param user updated user
   * @param employerProfileDTO employer profile DTO
   * @return updated user
   */
  @Transactional
  public User updateEmployerProfile(User user, EmployerProfileDTO employerProfileDTO) {
    EmployerProfile employerProfile = defaultEmployerProfileBuilder.setDescription(employerProfileDTO.getDescription())
                                                                   .build();
    user.getCurrentState()
        .updateEmployer(employerProfile);
    return userRepository.saveAndFlush(user);
  }

  /**
   * Updates user's freelancer profile
   * @param user updated user
   * @param freelancerProfileDTO freelancer profile DTO
   * @return updated user
   */
  @Transactional
  public User updateFreelancerProfile(User user, FreelancerProfileDTO freelancerProfileDTO) {
    Set<Skill> skills = skillRepository.findAllByNamesOrCreate(freelancerProfileDTO.getSkills());
    FreelancerProfile freelancerProfile = defaultFreelancerProfileBuilder.setDescription(freelancerProfileDTO.getDescription())
                                                                         .setSkills(skills)
                                                                         .build();
    user.getCurrentState()
        .updateFreelancerProfile(freelancerProfile);
    FreelancerProfile finalFreelancerProfile = freelancerProfileRepository.saveAndFlush(user.getFreelancerProfile());
    return finalFreelancerProfile.getOwner();
  }

  /**
   * Changes current user's state to verified if it isn't verifed already
   * @param id updated user id
   * @return updated user
   */
  @Transactional
  public User verifyById(long id) {
    User foundUser = userRepository.findById(id)
                                   .orElseThrow(EntityNotFoundException::new);
    if (!(foundUser.getCurrentState() instanceof VerifiedUserState)) {
      foundUser.setCurrentState(new VerifiedUserState());
    }
    return userRepository.saveAndFlush(foundUser);
  }

  /**
   * Finds all users with base user authoritites
   * @return found users
   */
  public List<User> findAll() {
    return userRepository.findAllByAuthorities(getPlainUserAuthority());
  }

  private UserAuthority getPlainUserAuthority() {
    return userAuthorityRepository.findFirstOrCreateByAuthority(AuthorityName.USER);
  }
}
