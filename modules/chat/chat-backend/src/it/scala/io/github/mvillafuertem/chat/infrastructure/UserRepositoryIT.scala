package io.github.mvillafuertem.chat.infrastructure

import io.github.mvillafuertem.chat.infrastructure.RunnableIntegrationSpec.{ ZDockerInfrastructure, ZIntegrationSpecEnv }
import io.github.mvillafuertem.shared.User
import zio._
import zio.test.Assertion.equalTo
import zio.test._

object UserRepositoryIT extends RunnableIntegrationSpec {

  override def spec: ZSpec[ZIntegrationSpecEnv, Any] =
    (suite(getClass.getSimpleName)(
      testM("create an user")(
        assertM(
          // w h e n
          for {
            _    <- UserRepository.createUser(User("hola", "adios@email.com", "qwerty")).runCollect
            user <- UserRepository.findUserByEmail("adios@email.com").runCollect
          } yield user
          // t h e n
        )(equalTo(Chunk.single(User("hola", "adios@email.com", "qwerty"))))
      ),
      testM("get an user")(
        assertM(
          // w h e n
          for {
            user <- UserRepository.findUserByEmail("adios@email.com").runCollect
          } yield user
          // t h e n
        )(equalTo(Chunk.empty))
      )
    ) @@ TestAspect.around(
      for {
        container <- ZIO.access[ZDockerInfrastructure](_.get)
        _         <- Task.effect(container.start())
      } yield container
    )(container => Task.effect(container.stop()).run))
      .provideSomeLayer[ZIntegrationSpecEnv](UserRepository.live)

}
