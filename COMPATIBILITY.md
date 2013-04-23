# Latest Lein Compatibility List

<table>
	<thead>
		<tr>
			<th>Lein Version</th><th>Latest Lein Package Version</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>2.0</td><td>2.0.0</td>
		</tr><tr>
			<td>2.1</td><td>2.1.0</td>
		</tr>
	</tbody>
</table>

# About Compatibility

lein-package was originally design to be compatible with the 2.0 release of leiningen.  Since the original release of lein-package, leiningen has experienced many contributions which affect how lein-package integrates into it's task lifecycles.  In order to provide the same capabilities across different version of leiningen, as well as to make it easier for developers to know which version to use, the versioning scheme of lein-package is going to change to reflect which version of leiningen it is compatible with.

# Version/Compatiblity Scheme
The last version of lein-package that will not have it's version number reflect the leiningen version it is compatible with is 0.1.2, which is compatible with leiningen 2.0.  All versions following 0.1.2 will use the versioning scheme below:

```
${LEIN-MAJOR_VERSION}.${LEIN-MINOR-VERSION}.${LEIN-PACKAGE-BUILD}
```

As an example, if the latest build of lein-package is build 33, lein will have compatible versions of lein-package as:

<table>
	<thead>
		<tr>
			<th>Lein Version</th><th>Lein Package Version</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>2.0</td><td>2.0.33</td>
		</tr>
		<tr>
			<td>2.1</td><td>2.1.33</td>
		</tr>
	</tbody>
</table>


# Version Deprecation.
No more than 3 branches of lein-package will be maintaine, representing the latest 3 minor versions of leiningen, with the earliest version being 2.0 (Sorry, lein 1.0 users).
