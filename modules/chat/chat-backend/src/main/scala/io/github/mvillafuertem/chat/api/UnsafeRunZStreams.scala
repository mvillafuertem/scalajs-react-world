package io.github.mvillafuertem.chat.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import akka.util.ByteString
import io.circe.generic.auto._
import io.circe.syntax._
import io.github.mvillafuertem.chat.domain.error.ChatError
import zio.stream.ZSink.Push
import zio.stream.{ ZSink, ZStream }
import zio.{ Chunk, ZIO, ZManaged }

object UnsafeRunZStreams {

  def run[R, E](
    zstream: ZStream[R, E, String]
  )(
    zsink: ZSink[R, Nothing, Either[E, Source[ByteString, NotUsed]], Nothing, Either[E, Source[ByteString, NotUsed]]]
  ): ZIO[R, Nothing, Either[E, Source[ByteString, NotUsed]]] =
    zstream
      // .map(_.asJson.noSpaces)
      .map(ByteString(_))
      .map(Source.single)
      .either
      .run(zsink)

  type ZSinkEither = ZSink[
    Any,
    Nothing,
    Either[ChatError, Source[ByteString, NotUsed]],
    Nothing,
    Either[ChatError, Source[ByteString, NotUsed]]
  ]

  // @see ZSink.head
  def zsinkHeadEither: ZSinkEither = ZSink(ZManaged.succeed {
    case Some(ch) => if (ch.isEmpty) Push.more else Push.emit(ch.head, Chunk.empty)
    case None     => Push.emit(Left[ChatError, Source[ByteString, NotUsed]](ChatError.NonExistentEntityError()), Chunk.empty)
  })

  // @see ZSink.collection
  def zsinkEither: ZSinkEither =
    ZSink
      .foldLeft[Either[ChatError, Source[ByteString, NotUsed]], Either[ChatError, Source[ByteString, NotUsed]]](Right(Source.empty)) { case (input, users) =>
        for {
          source <- input
          d      <- users
        } yield source.concat(d)
      }

}
