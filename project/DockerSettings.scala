import com.typesafe.sbt.packager.Keys.{ dockerExposedPorts, dockerUsername, packageName }
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.Docker
import sbt.{ Project, _ }
import sbt.Keys.name

object DockerSettings {

  lazy val value: Project => Project =
    _.settings(
      dockerExposedPorts ++= Seq(8080),
      Docker / packageName := name.value,
      dockerUsername       := Some("user")
    ).enablePlugins(DockerPlugin)

}
