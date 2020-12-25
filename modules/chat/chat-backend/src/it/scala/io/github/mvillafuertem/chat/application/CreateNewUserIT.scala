package io.github.mvillafuertem.chat.application

import io.github.mvillafuertem.chat.infrastructure.RunnableIntegrationSpec.ZIntegrationSpecEnv
import io.github.mvillafuertem.chat.infrastructure.{ MongoUserRepository, RunnableIntegrationSpec }
import io.github.mvillafuertem.shared.User
import zio.Chunk
import zio.test.Assertion.equalTo
import zio.test._

object CreateNewUserIT extends RunnableIntegrationSpec {
  override def spec: ZSpec[ZIntegrationSpecEnv, Any] =
    suite(getClass.getSimpleName)(
      testM("create an user")(
        assertM(
          // w h e n
          for {
            createdUser <- CreateNewUser.createUser(User("asd", "", "")).runCollect
            // t h e n
          } yield createdUser
        )(equalTo(Chunk.single(User("asd", "", ""))))
      )
    ).provideSomeLayer[ZIntegrationSpecEnv](MongoUserRepository.live >>> CreateNewUser.live)
}
