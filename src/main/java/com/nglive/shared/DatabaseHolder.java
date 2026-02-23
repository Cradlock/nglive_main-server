package com.nglive.shared;

import org.flywaydb.core.Flyway;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseHolder {
  private static PgPool Pg_client;
  



  public static synchronized void initClients(Vertx vertx,JsonObject config){
    if(Pg_client == null){
      Flyway flyway = Flyway.configure()
        .dataSource(
          config.getString("PG_URL"),
          config.getString("PG_USER"),
          config.getString("PG_PASSWORD")
        )
        .locations("classpath:db/migration")
        .baselineOnMigrate(true)
        .load();
      
      vertx.executeBlocking(promise -> {
        try{
          flyway.migrate();
          promise.complete();
        } catch(Exception e){
          promise.fail(e.getMessage());
        }
      }, res -> {
        if(res.succeeded()){
          System.out.println("Миграции для PostgreSQL проведены успешно");
        }else{
          System.out.println("Ошибка миграции"+res.cause());
        }
      });

      PgConnectOptions connectOptions = new PgConnectOptions()
        .setHost(config.getString("PG_HOST"))
        .setPort(config.getInteger("PG_PORT"))
        .setDatabase(config.getString("PG_NAME"))
        .setUser(config.getString("PG_USER"))
        .setPassword(config.getString("PG_PASSWORD"));

      Pg_client = PgPool.pool(vertx, connectOptions, new PoolOptions().setMaxSize(5));
    }

  }



  public static synchronized Future<Void> init(Vertx vertx,JsonObject config){
    
    try{
      
      initClients(vertx, config);
      checkPg();
      

      return Future.succeededFuture();
    } catch(Exception e){
      System.out.println(e);
      return Future.failedFuture(e);
    
    }
     
  }
  

  public static PgPool getPg(){
    return Pg_client;
  }

  private static Future<Void> checkPg(){
    if(Pg_client == null){
      return Future.failedFuture("PostgreSQL не подключен");
    }

    return Pg_client
      .query("SELECT 1")
      .execute()
      .onSuccess(v -> System.out.println("✅ PostgreSQL подключен"))
      .onFailure(err -> System.err.println("❌ PostgreSQL ошибка подключения: " + err.getMessage()))
      .mapEmpty();
  }  



}


