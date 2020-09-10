package io.github.mvillafuertem.model

import io.circe.generic.auto._
import io.circe.parser.parse

import scala.scalajs.js

class Hero(val id: String, val superhero: String, val publisher: String, val alter_ego: String, val first_appearance: String, val characters: String)
    extends js.Object

object Hero {

  def apply(id: String, superhero: String, publisher: String, alter_ego: String, first_appearance: String, characters: String): Hero =
    new Hero(id, superhero, publisher, alter_ego, first_appearance, characters)

  lazy val heroes: Seq[Hero] = parse(jsonString).flatMap(_.as[Seq[Hero]]) match {
    case Left(value)  =>
      println(s"Error parsing jsonString data ~> $value")
      Seq[Hero]()
    case Right(value) => value
  }

  def getHeroByPublisher(publisher: String): Seq[Hero] = heroes.filter(_.publisher.equalsIgnoreCase(publisher))

  def getHeroById(id: String): Option[Hero] = heroes.find(_.id.equalsIgnoreCase(id))

  private lazy val jsonString: String =
    """
      |[
      |    {
      |        "id": "dc-batman",
      |        "superhero":"Batman",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Bruce Wayne",
      |        "first_appearance":"Detective Comics #27",
      |        "characters":"Bruce Wayne"
      |    },
      |    {
      |        "id": "dc-superman",
      |        "superhero":"Superman",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Kal-El",
      |        "first_appearance":"Action Comics #1",
      |        "characters":"Kal-El"
      |    },
      |    {
      |        "id": "dc-flash",
      |        "superhero":"Flash",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Jay Garrick",
      |        "first_appearance":"Flash Comics #1",
      |        "characters":"Jay Garrick, Barry Allen, Wally West, Bart Allen"
      |    },
      |    {
      |        "id": "dc-green",
      |        "superhero":"Green Lantern",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Alan Scott",
      |        "first_appearance":"All-American Comics #16",
      |        "characters":"Alan Scott, Hal Jordan, Guy Gardner, John Stewart, Kyle Raynor, Jade, Sinestro, Simon Baz"
      |    },
      |    {
      |        "id": "dc-arrow",
      |        "superhero":"Green Arrow",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Oliver Queen",
      |        "first_appearance":"More Fun Comics #73",
      |        "characters":"Oliver Queen"
      |    },
      |    {
      |        "id": "dc-wonder",
      |        "superhero":"Wonder Woman",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Princess Diana",
      |        "first_appearance":"All Star Comics #8",
      |        "characters":"Princess Diana"
      |    },
      |    {
      |        "id": "dc-martian",
      |        "superhero":"Martian Manhunter",
      |        "publisher":"DC Comics",
      |        "alter_ego":"J\"onn J\"onzz",
      |        "first_appearance":"Detective Comics #225",
      |        "characters":"Martian Manhunter"
      |    },
      |    {
      |        "id": "dc-robin",
      |        "superhero":"Robin/Nightwing",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Dick Grayson",
      |        "first_appearance":"Detective Comics #38",
      |        "characters":"Dick Grayson"
      |    },
      |    {
      |        "id": "dc-blue",
      |        "superhero":"Blue Beetle",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Dan Garret",
      |        "first_appearance":"Mystery Men Comics #1",
      |        "characters":"Dan Garret, Ted Kord, Jaime Reyes"
      |    },
      |    {
      |        "id": "dc-black",
      |        "superhero":"Black Canary",
      |        "publisher":"DC Comics",
      |        "alter_ego":"Dinah Drake",
      |        "first_appearance":"Flash Comics #86",
      |        "characters":"Dinah Drake, Dinah Lance"
      |    },
      |    {
      |        "id": "marvel-spider",
      |        "superhero":"Spider Man",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Peter Parker",
      |        "first_appearance":"Amazing Fantasy #15",
      |        "characters":"Peter Parker"
      |    },
      |    {
      |        "id": "marvel-captain",
      |        "superhero":"Captain America",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Steve Rogers",
      |        "first_appearance":"Captain America Comics #1",
      |        "characters":"Steve Rogers"
      |    },
      |    {
      |        "id": "marvel-iron",
      |        "superhero":"Iron Man",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Tony Stark",
      |        "first_appearance":"Tales of Suspense #39",
      |        "characters":"Tony Stark"
      |    },
      |    {
      |        "id": "marvel-thor",
      |        "superhero":"Thor",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Thor Odinson",
      |        "first_appearance":"Journey into Myster #83",
      |        "characters":"Thor Odinson"
      |    },
      |    {
      |        "id": "marvel-hulk",
      |        "superhero":"Hulk",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Bruce Banner",
      |        "first_appearance":"The Incredible Hulk #1",
      |        "characters":"Bruce Banner"
      |    },
      |    {
      |        "id": "marvel-wolverine",
      |        "superhero":"Wolverine",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"James Howlett",
      |        "first_appearance":"The Incredible Hulk #180",
      |        "characters":"James Howlett"
      |    },
      |    {
      |        "id": "marvel-daredevil",
      |        "superhero":"Daredevil",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Matthew Michael Murdock",
      |        "first_appearance":"Daredevil #1",
      |        "characters":"Matthew Michael Murdock"
      |    },
      |    {
      |        "id": "marvel-hawkeye",
      |        "superhero":"Hawkeye",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Clinton Francis Barton",
      |        "first_appearance":"Tales of Suspense #57",
      |        "characters":"Clinton Francis Barton"
      |    },
      |    {
      |        "id": "marvel-cyclops",
      |        "superhero":"Cyclops",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Scott Summers",
      |        "first_appearance":"X-Men #1",
      |        "characters":"Scott Summers"
      |    },
      |    {
      |        "id": "marvel-silver",
      |        "superhero":"Silver Surfer",
      |        "publisher":"Marvel Comics",
      |        "alter_ego":"Norrin Radd",
      |        "first_appearance":"The Fantastic Four #48",
      |        "characters":"Norrin Radd"
      |    }
      |]
      |""".stripMargin

}
