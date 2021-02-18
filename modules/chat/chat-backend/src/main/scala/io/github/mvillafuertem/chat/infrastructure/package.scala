package io.github.mvillafuertem.chat

import com.mongodb.reactivestreams.client.{ MongoClient, MongoDatabase }
import zio.Has

package object infrastructure {

  type ZMongoDatabase = Has[MongoDatabase]
  type ZMongoClient   = Has[MongoClient]

}
