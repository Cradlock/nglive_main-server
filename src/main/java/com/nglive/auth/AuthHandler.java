package com.nglive.auth;

import io.vertx.core.Handler;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;

public class AuthHandler implements Handler<RoutingContext>{
  
  @Override
  public void handle(RoutingContext ctx){
    Cookie auth_cookie = ctx.getCookie("access_token");

    if(auth_cookie == null || auth_cookie.getValue().isEmpty()){
      ctx.fail(403);
      return;
    }

    String token = auth_cookie.getValue();

    if(isValid(token)){
      
      ctx.next();
    }else{
      ctx.fail(401);
    }

  }
  
  public boolean isValid(String token){
    return true;
  }
}
