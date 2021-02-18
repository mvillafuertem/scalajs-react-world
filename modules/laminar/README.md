# Scala.js and Snowpack example

This example project shows how to use [Snowpack](https://snowpack.dev) together
with [Scala.js](https://scala-js.org).

## Getting started

You need to:

-   Install the javascript libraries:

    > yarn --cwd modules/laminar/ install

-   Create the bundle (in a separate terminal):

    > sbt "project laminar; ~fullLinkJS"

-   Run Snowpack dev server:

    > yarn --cwd modules/laminar/ snowpack dev
