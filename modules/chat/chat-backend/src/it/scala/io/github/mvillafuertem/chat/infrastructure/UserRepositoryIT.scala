package io.github.mvillafuertem.chat.infrastructure

import io.github.mvillafuertem.chat.domain.error.ChatError.DuplicateEntityError
import io.github.mvillafuertem.chat.infrastructure.RunnableIntegrationSpec.ZIntegrationSpecEnv
import zio._
import zio.test.Assertion._
import zio.test._

object UserRepositoryIT extends RunnableIntegrationSpec {

  override def spec: ZSpec[ZIntegrationSpecEnv, Any] =
    suite(getClass.getSimpleName)(
      testM("create an user")(
        // w h e n
        for {
          createdUser <- MongoUserRepository
            .createUser(UserDBO("hola", "adios@email.com", "qwerty"))
            .runCollect
          foundUser <- MongoUserRepository
            .findUserByEmail("adios@email.com")
            .runCollect
          // t h e n
        } yield assert(createdUser)(equalTo(foundUser)) // assert(_id)(equalTo(user.flatMap(_._id)))
      ),
      testM("error duplicate entity")(
        assertM(
          // w h e n
          (for {
            _ <- MongoUserRepository
              .createUser(UserDBO("hola", "adios@email.com", "qwerty"))
              .runCollect
            _ <- MongoUserRepository
              .createUser(UserDBO("hola", "adios@email.com", "qwerty"))
              .runCollect
          } yield ()).run
            // t h e n
        )(fails(equalTo(DuplicateEntityError())))
      ),
      testM("get an user")(
        assertM(
          // w h e n
          for {
            user <- MongoUserRepository.findUserByEmail("adios@email.com").runCollect
          } yield user
          // t h e n
        )(equalTo(Chunk.empty))
      )
    )
      .provideSomeLayer[ZIntegrationSpecEnv](MongoUserRepository.live)

}
