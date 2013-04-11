(ns leiningen.package.hooks.deploy
  "Allows for the restriction or addition of files that are deployed to the remote repository."
  (:require [leiningen.deploy :as deploy]
            [leiningen.package :as package]
            [leiningen.package.artifact :as artifact]
            [leiningen.pom :as pom]
            [robert.hooke]))

(defn deploy-files-for
  [f project repo]
  (let [sign? (deploy/sign-for-repo? repo)
        artifacts (artifact/artifacts project)]
    (if artifacts
      (do
        (package/clean project)
        (let [base-mappings (artifact/mappings project)
              base-entries (artifact/mappings->entries base-mappings)
              built-artifacts (artifact/built-artifacts project)
              entries (into base-entries
                            (for [artifact built-artifacts]
                              [(artifact/coordinates project artifact)
                               (artifact/file-path project artifact)]))
              
              sig-opts (when sign? (deploy/signing-opts project repo))
              base-signed (if sign?
                            (into {} (map (fn [m] [(artifact/coordinates project (:artifact m) ".asc")
                                                   (leiningen.deploy/sign (:file m) sig-opts)]) base-mappings)))
              signed (if base-signed
                       (into base-signed
                             (for [artifact built-artifacts]
                               [(artifact/coordinates project artifact ".asc")
                                (leiningen.deploy/sign
                                 (artifact/file-path project artifact)
                                 sig-opts)])))]
          (merge entries signed)))
      (f project repo))))

(defn activate []
  (robert.hooke/add-hook #'leiningen.deploy/files-for deploy-files-for))
