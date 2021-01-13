package io.github.mvillafuertem.chat.configuration

import com.mongodb.reactivestreams.client.{ MongoClient, MongoClients, MongoDatabase }
import com.mongodb.{ ConnectionString, MongoClientSettings }
import io.github.mvillafuertem.chat.configuration.properties.MongoDBConfigurationProperties
import io.github.mvillafuertem.chat.configuration.properties.MongoDBConfigurationProperties.ZMongoDBConfigurationProperties
import io.github.mvillafuertem.chat.infrastructure.{ UserDBO, ZMongoClient, ZMongoDatabase }
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodec
import org.bson.codecs.configuration.CodecRegistries.{ fromCodecs, fromProviders, fromRegistries }
import org.mongodb.scala.bson.codecs.Macros
import zio.ZLayer

trait InfrastructureConfiguration {

  val mongoDBLayer: ZLayer[Any, Nothing, ZMongoDatabase] =
    ZLayer.succeed(MongoDBConfigurationProperties()) >+>
      mongoClientLayer >>>
      mongoDatabaseLayer

  private lazy val mongoDatabaseLayer: ZLayer[ZMongoDBConfigurationProperties with ZMongoClient, Nothing, ZMongoDatabase] =
    ZLayer.fromServices[MongoDBConfigurationProperties, MongoClient, MongoDatabase]((properties, client) => client.getDatabase(properties.database))

  private lazy val mongoClientLayer: ZLayer[ZMongoDBConfigurationProperties, Nothing, ZMongoClient] =
    ZLayer
      .fromService[MongoDBConfigurationProperties, MongoClient] { properties: MongoDBConfigurationProperties =>
        val connectionString = {
          val user     = properties.user
          val password = properties.password
          val hostname = properties.hostname
          val port     = properties.port
          val database = properties.database
          s"mongodb://$user:$password@$hostname:$port/$database"
        }
        //              val streamFactoryFactory = AsynchronousSocketChannelStreamFactoryFactory
        //                .builder()
        //                .group(java.nio.channels.AsynchronousChannelGroup.withThreadPool(executorService))
        //                .build()
        MongoClients.create(
          MongoClientSettings
            .builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .codecRegistry(
              fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry,
                fromCodecs(new UuidCodec(UuidRepresentation.STANDARD)),
                //fromProviders(classOf[UserDBO])
                fromProviders(Macros.createCodecProviderIgnoreNone[UserDBO]())
              )
            )
            //.streamFactoryFactory(streamFactoryFactory)
            .build()
        )

      }
}
