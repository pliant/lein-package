(defproject lein-package/test-classifier "2.0.1"
  :description "This project test the usage of the package command when jar with a classifier.
    To test, perform the following:

      lein package
      lein install
      lein deploy local

    The deploy task will put the artifact(s) in the target/deploy directory."

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]

  :plugins [[lein-package "2.0.1"]
            [lein-ring "0.8.3"]]

  :hooks [leiningen.package.hooks.deploy
          leiningen.package.hooks.install]

  :ring {:handler lein-package.test-classifier/handler}

  :package {:skipjar true 
            :autobuild true
            :artifacts [{:build "ring uberjar" :classifier "standalone" :extension "jar"}]}

  :deploy-repositories [["local" "file:./target/deploy"]])
