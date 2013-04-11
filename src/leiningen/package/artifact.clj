(ns leiningen.package.artifact
  "Allows for the restriction or addition of files that are deployed to the remote repository."
  (:require [clojure.java.io :as io]
            [clojure.string :as twine]
            [leiningen.core.main :as main]
            [leiningen.jar :as jar]
            [leiningen.pom :as pom]))

(def jar {:extension "jar" :build "jar"})
(def pom {:extension "pom" })


(defn ^java.io.File file
  [project artifact]
  (let [target (io/file (:target-path project))
        suffix (if (:classifier artifact)
                   (str "-" (:classifier artifact) "." (:extension artifact))
                   (str "." (:extension artifact)))
        file-name (str (:name project) "-" (:version project) suffix)]
    (io/file target file-name)))

(defn ^java.lang.String file-path
  [project artifact]
  (.getAbsolutePath (file project artifact)))

(defn exists?
  [project artifact]
  (.exists (file project artifact)))

(defn artifactify
  [entry]
  (cond
    (map? entry) entry
    :else {:extension (name entry)}))

(defn artifacts
  [project]
  (let [raw-artifacts (get-in project [:package :artifacts])]
    (if raw-artifacts
      (let [configured (for [entry raw-artifacts] (artifactify entry))
            pomjar #{"pom" "jar"}]
        (filter
         #(or (:classifier %) (not (pomjar (:extension %))))
         configured)))))

(defn make-jar?
  [project]
  (not (true? (get-in project [:package :skipjar]))))

(defn make-jar
  [project]
  (if (make-jar? project)
    (jar/jar project)))

(defn buildable-artifacts
  [project]
  (if (make-jar? project) (cons jar (artifacts project)) (artifacts project)))

(defn all-artifacts
  [project]
  (concat [pom] (buildable-artifacts project)))

(defn build-artifact
  [project artifact]
  (main/debug "Building" (select-keys artifact [:extension :classifier]))
  (let [raw-args (twine/split (:build artifact) #"\s+")
        task-name (first raw-args)
        args (next raw-args)]
    (main/apply-task task-name project args)))

(defn build-artifacts
  [project artifacts]
  (doseq [artifact artifacts]
    (if (:build artifact)
      (build-artifact project artifact))))

(defn built-artifacts
  "Gathers artifacts that are already build or able to be built now."
  [project]
  (let [autobuild (get-in project[:package :autobuild])
        artifacts (artifacts project)]
    (filter #(cond
               (exists? project %) %
               (or autobuild (:autobuild %)) (or (build-artifact project %)
                                                 true)
               :else false)
            artifacts)))

(defn coordinates
  ([project]
     [(symbol (:group project) (:name project)) (:version project)])
  ([project artifact & [suffix]]
     (let [extension (str (:extension artifact) suffix)
           classifier (if (:classifier artifact)
                        [:classifier (:classifier artifact)])]
       (vec
        (concat
         [(symbol (:group project) (:name project))
          (:version project)
          :extension extension]
         classifier)))))

(defn mappings
  [project] 
  (let [pom-file (pom/pom project)
        pom-coord (coordinates project pom)
        pom-entry {:artifact pom :coordinate pom-coord :file pom-file}]
    (if (make-jar? project)
      (let [jar-files (make-jar project)
            jar-entries (map (fn [[k v]] {:artifact k :coordinate (coordinates project k) :file v}) jar-files)]
        (conj jar-entries pom-entry))
      (list pom-entry))))

(defn mappings->entries
  [coll]
  (into {} (map (fn [m] [(:coordinate m) (:file m)]) coll)))