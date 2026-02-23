package com.nglive.gateways;

import com.nglive.auth.AuthRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class HttpGatewayVerticle extends AbstractVerticle{
  

  public void connectRouters(Router sourceRouter){
    
    // Подключение auth - сервиса 
    sourceRouter.mountSubRouter("/auth", AuthRouter.createRouter(vertx));

  } 
  

  @Override
  public void start(Promise<Void> startPromise){
    int port = config().getInteger("HTTP_PORT");
    String host = config().getString("HTTP_HOST");
    
    Router mainRouter = Router.router(vertx);
    
    connectRouters(mainRouter);

    vertx.createHttpServer()
      .requestHandler(mainRouter)    
      .listen(port,host,ar -> {
        if(ar.succeeded()){
          System.out.println("Http шлюз запущен на порту:"+port);
        }else{
          System.err.println("Не удалось запустить HTTP шлюз");
        }
      });
  } 

}
