addSbtPlugin("com.eed3si9n"       % "sbt-unidoc"           % "0.3.3")
addSbtPlugin("com.github.gseitz"  % "sbt-release"          % "1.0.3")
addSbtPlugin("com.jsuereth"       % "sbt-pgp"              % "1.0.0")
addSbtPlugin("org.tpolecat"       % "tut-plugin"           % "0.4.6")
addSbtPlugin("com.typesafe.sbt"   % "sbt-ghpages"          % "0.5.4")
addSbtPlugin("com.typesafe"       % "sbt-mima-plugin"      % "0.1.11")
addSbtPlugin("com.typesafe.sbt"   % "sbt-site"             % "0.8.1")
addSbtPlugin("net.virtual-void"   % "sbt-dependency-graph" % "0.8.2")
addSbtPlugin("org.xerial.sbt"     % "sbt-sonatype"         % "1.1")
addSbtPlugin("pl.project13.scala" % "sbt-jmh"              % "0.2.16")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"          % "0.6.13")

scalacOptions += "-deprecation"
