(defproject lein-package/test-war-jar "0.1.0-SNAPSHOT"
  :description "This project test the usage of the package command when generating a JAR and a WAR with the lein-ring plugin.
    To test, perform the following:

      lein package
      lein install
      lein deploy local

    The deploy task will put the artifact(s) in the target/deploy directory."

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]

  :plugins [[lein-package "0.1.2"]
            [lein-ring "0.8.3"]]

  :hooks [leiningen.package.hooks.deploy
          leiningen.package.hooks.install]

  :ring {:handler lein-package.test-war/handler}

  :package {:skipjar false
            :autobuild true
            :artifacts [{:build "ring war" :extension "war" }]}

  :deploy-repositories [["local" "file:./target/deploy"]])
