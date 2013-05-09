(ns leiningen.package.hooks.install
  "Allows for the addition of files to be installed in the local repository."
  (:require [leiningen.package :as package]
            [leiningen.package.artifact :as artifact]
            [robert.hooke]
            [leiningen.install]
            [leiningen.jar :as jar]
            [leiningen.pom :as pom]
            [cemerick.pomegranate.aether :as aether]))

(defn install
  [f project]
  (let [artifacts (artifact/artifacts project)]
    (if artifacts
      (do
        (package/clean project)
        (let [base-mappings (artifact/mappings project)
              base-entries (artifact/mappings->entries base-mappings)
              built-artifacts (artifact/built-artifacts project)
              arts (concat (into [] (map #(:coordinate %) base-mappings))
                           (for [artifact built-artifacts] 
                             (artifact/coordinates project artifact)))
              files (into base-entries
                          (for [artifact built-artifacts] 
                            [(artifact/coordinates project artifact)
                             (artifact/file-path project artifact)]))]
          (aether/install-artifacts :artifacts arts :files files)))
      (f project))))

(defn activate []
  (robert.hooke/add-hook #'leiningen.install/install install))
