package com.nglive.auth;


import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class AuthRouter{
  
  // Инициализация всех эндпоинтов 
  private static void init(Router router){
    Handler<RoutingContext> authHandler = new AuthHandler();

    // О себе
    router.get("/me")
      .handler(authHandler)
      .handler(ctx -> {
        
        JsonObject o = new JsonObject().put("msg", "Привет");
        ctx.response().end(o.encode());      
    });
    
    // Авторизация 
    router.post("/login").handler(ctx -> {

    });

    // Обновление Access  
    router.post("/refresh").handler(ctx -> {

    });

  }


  

  // Основная точка
  public static Router createRouter(Vertx vertx){
    Router router = Router.router(vertx);
      
    router.route()
      .handler(BodyHandler.create());
    
    init(router);  
    
    return router;
  }
}
