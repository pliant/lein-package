# About

Provides the ability to package, install, and deploy artifacts other than JARs.

## Why lein-package

The defacto artifact that is built, installed, and deployed by leiningen is a JARs.  While this satifies the use case of building a library, building anything more sophisticated with leiningen requires external deployment or installation scripts.  lein-package looks to satisfy the use cases that require multiple artifacts including or excluding JARs, such as:

+ Building and archive a WAR project.
+ Building and archive a library along with an additional ZIP archive with external configuration templates (such as properties files that are deployed outside of a WAR on the classpath).

## Usage

### Configure
To use add the plugin to your project.

```clojure
  :plugins [[lein-package "0.1.0"]]
```

Add hooks if you want the configured artifacts to be installed or deployed.

```clojure
  :hooks [leiningen.package.hooks.deploy ;; Will deploy configured packages to remote repos when lein deploy is issued
          leiningen.package.hooks.install ;; Will install configured packages to local repo when lein install is issued.]
```

Configuring what packages are built, deployed, and installed.

```clojure
 :package {:autobuild true ;; Will automatically build the artifact if it does not exist.
           :reuse false ;; If true, will use an existing artifact if available, else will build it if autobuild is true.
           :artifacts [ ;; Collection of artifacts to build/package/install/deploy
             {:build "war" ;; Lein command used to build the artifact. e.g ring war, ring uberwar, war, assemble
              :extension "war" ;; The extension/suffix that the artifact file can be identified by. (:todo regex)}]}
```
 
### Build/Package
To build all of the configured artifacts issue the following command:

```
lein package
```

### Install & Deploying
Installing and deploying packages are performed automatically with the normal lein install and lein deploy commands as long as the hooks have been configured.