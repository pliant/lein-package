(ns leiningen.package.hooks.deploy
  "Allows for the restriction or addition of files that are deployed to the remote repository."
  (:require [leiningen.deploy]
            [leiningen.package :as package]
            [leiningen.package.artifact :as artifact]
            [leiningen.pom :as pom]
            [robert.hooke]))

(defn sign?
  [project repo]
  (and (:sign-releases (second repo) true)
       (not (.endsWith ^String (:version project) "-SNAPSHOT"))))

(defn- val* [e] (if e (val e)))

(defn deploy-files-for
  [f project repo]
  (let [artifacts (artifact/artifacts project)]
    (if artifacts
      (do
        (package/clean project)
        (let [jar-file (val* (first (artifact/make-jar project)))
              pom-file (pom/pom project)
              jar-coord (artifact/coordinates project artifact/jar)
              pom-coord (artifact/coordinates project artifact/pom)
              built-artifacts (artifact/built-artifacts project)

              base-entries (if jar-file
                             {pom-coord pom-file jar-coord jar-file}
                             {pom-coord pom-file})
              entries (into base-entries
                            (for [artifact built-artifacts]
                              [(artifact/coordinates project artifact)
                               (artifact/file-path project artifact)]))

              base-signed (if (sign? project repo)
                            (if jar-file
                              {(artifact/coordinates project artifact/pom ".asc")
                               (leiningen.deploy/sign pom-file)
                               (artifact/coordinates project artifact/jar ".asc")
                               (leiningen.deploy/sign jar-file)}
                              {(artifact/coordinates project artifact/pom ".asc")
                               (leiningen.deploy/sign pom-file)}))
              signed (if base-signed
                       (into base-signed
                             (for [artifact built-artifacts]
                               [(artifact/coordinates project artifact ".asc")
                                (leiningen.deploy/sign
                                 (artifact/file-path project artifact))])))]
          (merge entries signed)))
      (f project repo))))

(defn activate []
  (robert.hooke/add-hook #'leiningen.deploy/files-for deploy-files-for))
