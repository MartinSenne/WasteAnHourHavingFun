name := "WasteAnHourHavingFun"

version := "1.1"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest"          %% "scalatest"            % "2.2.5"    % Test

connectInput in test := true

fork in test := false

    