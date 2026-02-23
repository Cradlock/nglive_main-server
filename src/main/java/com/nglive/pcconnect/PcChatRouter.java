package com.nglive.pcconnect;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class PcChatRouter{
  
  public static void init(Router router){

    router.route("/chat").handler(ctx -> {
      ctx.request().toWebSocket()
        .onSuccess(ws -> {
          System.out.println("Клиент подключился к серверу pc/chat/");
          
          ws.writeTextMessage("Привет");

          ws.handler(buffer -> {
            System.out.println(buffer.toString());
          });

          ws.closeHandler(v -> System.out.println("Сессия закрыта"));

        })
        .onFailure(ws -> {
          ctx.fail(400);
        });
    });

  }
  
  public static Router createRouter(Vertx vertx){
    Router router = Router.router(vertx);

    init(router);

    return router;
  }

}
