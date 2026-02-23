package com.nglive;

import com.nglive.gateways.HttpGatewayVerticle;
import com.nglive.gateways.WsGatewayVerticle;
import com.nglive.shared.DatabaseHolder;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;


public class MainVerticle extends AbstractVerticle{
  
  // точка входа
  @Override
  public void start(Promise<Void> startPromise){
    System.out.println("I worked!!!");
    init(startPromise);  
  }
  
  // Конфигурации проекта
  public void init(Promise<Void> startPromise){
    ConfigStoreOptions envStore = new ConfigStoreOptions().setType("env");
    
    ConfigStoreOptions dotEnvStore = new ConfigStoreOptions()
      .setType("file")
      .setFormat("properties")
      .setConfig(new JsonObject().put("path", ".env"))
      .setOptional(true);
  
    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(dotEnvStore)
      .addStore(envStore);

    ConfigRetriever retriever = ConfigRetriever.create(vertx,options);

    retriever.getConfig(ar -> {
      if (ar.succeeded()) {
        JsonObject config = ar.result();
        deploy(config, startPromise);    
      } else {
        startPromise.fail("Не получилось загрузить конфиги" + ar.cause());
      }
    });

  }
  
  // Деплой всех вертикалей 
  public void deploy(JsonObject config,Promise<Void> startPromise){
    
    // Инициализация баз данных
    DatabaseHolder.init(vertx, config)
      // Запускаем если бд успешно подключился
      .compose(v -> {

        DeploymentOptions options = new DeploymentOptions()
          .setInstances(1)
          .setConfig(config);
        
        return Future.all(
            vertx.deployVerticle(new HttpGatewayVerticle(),options),
            vertx.deployVerticle(new WsGatewayVerticle(),options)
        );

      })
      .onFailure(ar -> {
        System.err.println("Ошибка в инициализации:"+ar.getMessage());
        ar.printStackTrace();
      });
  


  }
  
 

}





