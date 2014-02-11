# About

Provides the ability to generalize the packaging of generated artifacts beyond the standard JAR and plugin artifact command, as well as allowing the packaging to hook into the install and deploy lifecycles.

## Why lein-package

The de facto artifact which is built, installed, and deployed by leiningen is a JAR using the ``lein jar`` command.  Building anything more sophisticated with leiningen requires external plugins that impose their own plugin-specific commands to generate their artifacts.  Additionally, the leiningen installation and deployment lifecycles are keyed off of JAR artifacts, leaving non-JAR artifacts to be dealt with in a custom manner.  Managing the build and deployment of a project that is a single module is simple enough, but complexities arise when you have a project with multiple modules, each with it's own packaging requirements for it's artifacts.  For examle:

```
myproject
  \- subproject-client-jar
  \- subproject-app1-war
  \- subproject-app2-war
  \- subproject-services-war
```

From a packaging and deploy perspective, the same command should be used across all subprojects to 'package' the artifacts, allowing each subproject make the appropriate jar, war, or whatever.  lein-package looks to satisfy the use cases that require multiple artifact generation and managment, including or excluding JARs.

## Lein Version Compatibility
For details on compatibility with lein versions, see the [Compatibility](/COMPATIBILITY.md/) document. 

## Usage

### Configure
To use add the plugin to your project.

Lein 2.0
```clojure
  :plugins [[lein-package "2.0.1"]]
```

Lein 2.1
```clojure
  :plugins [[lein-package "2.1.1"]]
```

Add hooks if you want the configured artifacts to be installed or deployed.

```clojure
  :hooks [leiningen.package.hooks.deploy ;; Will deploy configured packages to remote repos when lein deploy is issued
          leiningen.package.hooks.install ;; Will install configured packages to local repo when lein install is issued.]
```

Configuring what packages are built, deployed, and installed.

```clojure
 :package {:skipjar false ;; If true, will not make a jar.  Defaults to false.
           :autobuild false ;; If true, will automatically build the artifact if it 
                            ;; does not exist.  Defaults to false.
           :reuse false ;; If true, will use an existing artifact if available.  
                        ;; Defaults to false.
           :artifacts [ ;; Collection of artifacts to build/package/install/deploy
             {:build "war" ;; Lein command used to build the artifact. e.g ring war, 
                           ;; ring uberwar, war, assemble
              :classifier "standalone" ;; Optional classifier for the artifact,
                                       ;; Use "standalone" for uberwar/uberjar
              :extension "war" ;; The extension/suffix that the artifact file can be 
                               ;; identified by. (:todo regex)}]}
```
 
### Build/Package
To build all of the configured artifacts issue the following command:

```
lein package
```

### Install & Deploying
Installing and deploying packages are performed automatically with the normal lein install and lein deploy commands as long as the hooks have been configured.  In order to include the packaged artifacts into these lifecycles, the ``:autobuild`` property must be set to true or the ``package`` task must be included in the call, such as ``lein do package, install`` or ``lein do package, deploy myrepo``.  Setting ``:autobuild`` to false allows for the installing deploying of only JARS yet being able to package other artifacts locally.
