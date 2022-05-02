package io.github.mvillafuertem.firebase

import typings.firebase.mod.auth.GoogleAuthProvider
import typings.firebase.mod.firestore.Firestore
import typings.firebase.mod.{ app, firestore, initializeApp }

import scala.scalajs.js

class FirebaseConfiguration(
  val apiKey:            String,
  val authDomain:        String,
  val databaseURL:       String,
  val projectId:         String,
  val storageBucket:     String,
  val messagingSenderId: String,
  val appId:             String
) extends js.Object

object FirebaseConfiguration {

  val firebaseConfig = new FirebaseConfiguration(
    "AIzaSyCRDBI2xfHlYIhwE5iDhnQ5jxmC-wwdEj8",
    "journal-79bb0.firebaseapp.com",
    "https://journal-79bb0.firebaseio.com",
    "journal-79bb0",
    "journal-79bb0.appspot.com",
    "844633303948",
    "1:844633303948:web:a3227b07ba1589d3f204a9"
  )
  // Initialize Firebase
  val firebase: app.App = initializeApp(firebaseConfig)

  val db = firestore.^

  val googleAuthProvider: GoogleAuthProvider = new GoogleAuthProvider()

}
