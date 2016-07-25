package com.ahancer.rr.service.impl;



import java.util.Date;

import org.springframework.security.core.authority.AuthorityUtils;

import com.ahancer.rr.models.User;
import com.ahancer.rr.security.CerberusUser;

public class CerberusUserFactory {

  public static CerberusUser create(User user) {
    return new CerberusUser(
      user.getUserId(),
      user.getName(),
      user.getPassword(),
      user.getEmail(),
      new Date(),
      AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE")
    );
  }

}
