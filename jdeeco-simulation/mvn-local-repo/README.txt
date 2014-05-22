In order to make the jar lib accessible through the local maven repository you need to execute the following command:

mvn install:install-file -Dfile=<path-to-file> -DgroupId=<myGroup> \ 
                         -DartifactId=<myArtifactId> -Dversion=<myVersion> \
                         -Dpackaging=<myPackaging> -DlocalRepositoryPath=<path>