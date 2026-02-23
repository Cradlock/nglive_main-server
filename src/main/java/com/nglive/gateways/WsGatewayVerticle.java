package com.nglive.gateways;

import com.nglive.pcconnect.PcChatRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class WsGatewayVerticle extends AbstractVerticle {
  
  public void connectRouters(Router sourceRouter){
    
    sourceRouter.mountSubRouter("/pc", PcChatRouter.createRouter(vertx));
  }



  @Override
  public void start(Promise<Void> startPromise){
    int port = config().getInteger("WS_PORT",9000);
      
    Router mainRouter = Router.router(vertx);
    
    connectRouters(mainRouter);

    vertx.createHttpServer()
      .requestHandler(mainRouter)
      .listen(port, res -> {
        if(res.succeeded()){
          System.out.println("WebSocket шлюз запущен на порту "+port);
        } else {
          System.out.println("Ошибка создания WebSocket Gateway:"+res.cause());
        }
      });
  }
}
