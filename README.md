## Requirements

As this is a java/maven project, make sure maven is installed (installing maven will also install java if it isnt already installed)  

    apt-get install maven

You will also need JGeocoder, download from  

    http://sourceforge.net/projects/jgeocoder/files/latest/download

Then run the following command to install to your local repo

    mvn install:install -file \
       -DgroupId=net.sourceforge.jgeocoder \
       -DartifactId=jgeocoder\
       -Dpackaging=jar \
       -Dversion=0.5 \
       -Dfile=jgeocoder-0.5-jar-with-dependencies.jar \
       -Dclassifier=jar-with-dependencies

## To build and install

Issue the following (the first time this gets run, many dependencies are downloaded (~300MB)).  

    mvn clean install package

## Available modules

There are several modules, as of now all maven modules depend on demo-avro.  There are several dependencies already configured for each module.

### Maven modules

    demo-avro     Avro data structures
    demo-etl      Extract Translate Load utilities
    demo-unite    Entity de-duplication
    demo-spark    Spark
    demo-crawl    web crawling

### Other modules

    demo-ansible  Utilities to launch processing clusters into a VPC

## To build jar with-all-dependencies, run a command simliar to this:

This creates a single jar that contains all deps, making running hadoop jobs simpler.

    modules="demo-etl demo-crawl"  
    for module in $modules 
    do  
        mvn -f $module/pom.xml assembly:assembly  
    done

## Importing project into Eclipse

You will need to have scala-ide.org, PHP Development Tools, JDT (Java Development Tools) and m2e (Maven integration for eclipse) installed.  

Then, in eclipse, go to File/Import/Maven/Existing Maven Project and select this directory.  Select all modules and press okay.  

You will have to run the following to generate  avro classes  

    mvn generate-sources 
 
## Example hadoop job instantiation (you will need add code to the appropriate module)

    hadoop jar demo-module/target/demo-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar \  
        com.indatasights.demo.module.Class \  
        -Dmapreduce.input.fileinputformat.inputdir=inputLocation \  
        -Dmapreduce.output.fileoutputformat.outputdir=outputLocation \  
        -Djob.option="someValue"
