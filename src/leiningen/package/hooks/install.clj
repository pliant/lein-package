(ns leiningen.package.hooks.install
  "Allows for the addition of files to be installed in the local repository."
  (:require [leiningen.package :as package]
            [leiningen.package.artifact :as artifact]
            [robert.hooke]
            [leiningen.install]
            [leiningen.jar :as jar]
            [leiningen.pom :as pom]
            [cemerick.pomegranate.aether :as aether]))

(defn- val* [e] (if e (val e)))

(defn install
  [f project]
  (let [artifacts (artifact/artifacts project)]
    (if artifacts
      (do
        (package/clean project)
        (let [jar-file (val* (first (artifact/make-jar project)))
              pom-file (pom/pom project)
              jar-coord (artifact/coordinates project)
              pom-coord (artifact/coordinates project artifact/pom)
              base-coords (if jar-file
                            [jar-coord pom-coord]
                            [pom-coord])
              base-files (if jar-file
                           {jar-coord jar-file pom-coord pom-file}
                           {pom-coord pom-file})
              built-artifacts (artifact/built-artifacts project)
              files (into base-files
                          (for [artifact built-artifacts]
                            [(artifact/coordinates project artifact)
                             (artifact/file-path project artifact)]))]
          (aether/install-artifacts :files files)))
      (f project))))

(defn activate []
  (robert.hooke/add-hook #'leiningen.install/install install))
