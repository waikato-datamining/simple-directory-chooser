# RELEASE

Switch to Java 11.

Use the following command to make a new release:

```
mvn release:prepare release:perform
```

After the release perform:

```
git push
```

Go to the following URL and publish the artifact:

```
https://central.sonatype.com/publishing/deployments
```

Go to Github, edit the Maven-generated release [tag](https://github.com/waikato-datamining/simple-directory-chooser/tags) 
and add all the generated artifacts (incl .asc) from the `target` directory.

Update the version of the Maven artifact in [README.md](README.md#maven).
