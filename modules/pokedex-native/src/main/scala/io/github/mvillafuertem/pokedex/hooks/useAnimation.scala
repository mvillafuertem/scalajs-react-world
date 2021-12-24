package io.github.mvillafuertem.pokedex.hooks

import typings.react.mod.useRef
import typings.reactNative.mod.Animated
import typings.reactNative.mod.Animated.TimingAnimationConfig

object useAnimation {

  // Scala no permite parametros por defecto en lambdas
  //
  // However, here is somewhat of a "hack" to do it regardless.
  // https://stackoverflow.com/questions/25234682/in-scala-can-you-make-an-anonymous-function-have-a-default-argument/25235029#25235029
  trait DurationDefault {
    def apply(duration: Int = 300): Unit
  }

  def apply() = {

    val opacity  = useRef(new Animated.Value(0)).current.asInstanceOf[Animated.Value]
    val position = useRef(new Animated.Value(0)).current.asInstanceOf[Animated.Value]

    val fadeIn: DurationDefault = (duration: Int) => Animated.timing(opacity, TimingAnimationConfig(1, true).setDuration(duration)).start()

    val fadeOut: DurationDefault = (duration: Int) =>
      Animated
        .timing(
          opacity,
          TimingAnimationConfig(0, true)
            .setDuration(duration)
        )
        .start()

    val startMovingPosition = (initPosition: Int) =>
      ((duration: Int) => {
        position.setValue(initPosition)

        Animated
          .timing(
            position,
            TimingAnimationConfig(0, true)
              .setDuration(duration)
          )
          .start()
      }): DurationDefault

    (opacity, position, fadeIn, fadeOut, startMovingPosition)
  }

}
