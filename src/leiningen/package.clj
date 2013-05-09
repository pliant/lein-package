(ns leiningen.package
  "Provides the packaging functionality for the plugin."
  (:require [leiningen.package.artifact :as artifact]
            [leiningen.clean :as clean]
            [leiningen.jar :as jar]
            [leiningen.pom :as pom]))

(def cleaned (atom {}))

(defn clean?
  [project]
  (if (or (get-in project [:package :reuse]) (@cleaned (artifact/coordinates project)))
    false
    true))

(defn clean
  [project]
  (if (clean? project)
    (let [coord (artifact/coordinates project)]
      (clean/clean project)
      (swap! cleaned assoc coord coord)
      true)))

(defn lessen
  [extensions artifacts]
  (let [extset (set extensions)]
    (filter #(extset (:extension %)) artifacts)))

(defn package
  "Builds the artifacts that have been configured."
  [project & extensions]
  (let [artifacts (artifact/buildable-artifacts project)
        buildable (if (> (count extensions) 0) (lessen extensions artifacts) artifacts)]
   (if (> (count buildable) 0)
      (do
        (clean project)
        (artifact/build-artifacts project buildable)))))
