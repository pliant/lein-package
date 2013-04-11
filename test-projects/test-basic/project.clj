(defproject lein-package/test-basic "2.0.0-SNAPSHOT"
  :description "This project test the usage of the package command without any package configuration, which should default to making a JAR.
    To test, perform the following:

      lein package
      lein install
      lein deploy local

    The deploy task will put the artifact(s) in the target/deploy directory."

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]

  :plugins [[lein-package "2.0.0-SNAPSHOT"]]
  :hooks [leiningen.package.hooks.deploy
          leiningen.package.hooks.install]
  :deploy-repositories [["local" "file:./target/deploy"]])
