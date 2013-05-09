From 2.0.0/2.1.0 to 2.0.1/2.1.1
====

Enhancements
----
* \#7: Added ability to have aliases as build command in package artifacts. @alexhall (many thanks)
* \#7: Enabled classifier jars in package artifacts. @alexhall (many thanks)
* \#7: Added test projects support aliases and classifiers.
* Refactored 2.0 branch to have same artifact polling as 2.1 branch.

Bug Fixes
----
none

From 0.1.2 to 2.0.0/2.1.0
====
2.0.0 and 2.1.0 are the first versions to mimic the leiningen versions they work with.

Enhancements
----
* \#3: Added test projects to run different versions of lein against.
* \#5: Created branches and documentation to signify compatibility.

Bug Fixes
----
* Reverted earlier 2.1 patch that broke 2.0, fixing \#4 across versions.
* \#6: Fix NullPointerException when using the hooks and :skipjar true. @jstoneham (many thanks)


From 0.1.1 to 0.1.2
====
0.1.2 is the last version whose version number is not representative of the version of lein it is compatible with.

Enhancements
----
* \#2: Clarified Readme.

Bug Fixes
----
none

From 0.1.0 to 0.1.1
====
0.1.1 release code enhancements and bug fixes provided by @hugoduncan.(Many Thanks)

Enhancements
----
* \#1: Provided compatibility with lein 2.1.0+
* \#1: Added debug logging for build

Bug Fixes
----
* \#1: fix disjoint?* bug

Release 0.1.0
====
Initial release providing packaging functionality for Lein 2-preview builds.

