# clojure-lab

Clojure Lab materials 

## Run from repository

### Prerequisites

- Install a [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (1.6 or later)
- Install the [leiningen build tool](http://leiningen.org/)

### Step 1: Clone this repo

```
git clone git@github.com:cognitect/clojure-lab.git
cd clojure-lab
```

### Step 2: Start the gorilla repl server 

```
lein gorilla :port 55555
```

### Step 3: Open the lab page in your web browser

[Start](http://127.0.0.1:55555/worksheet.html?filename=src/cljlab/start.clj)

## Creating a standalone package 

Create the uberjar:

```
rm target
lein uberjar
```

Compress the directory into a zip (this may vary per platform):

```
cd ..
zip -r clojure-lab clojure-lab/images clojure-lab/src clojure-lab/target/*-standalone.jar
```

## Running the standalone package

### Step 1: Unzip the zip file 

```
unzip clojure-lab.zip
cd clojure-lab
```

### Step 2: Start the server

```
java -jar target/clojure-lab-0.1.0-SNAPSHOT-standalone.jar
```

### Step 3: View the page in your browser

[Start](http://127.0.0.1:55555/worksheet.html?filename=src/cljlab/start.clj)

## Contributions 

This repository is free for all to use in learning Clojure. You are welcome to use it for any non-commercial use - free public workshops, internal company workshops, etc.

Contributions to this repository are welcome via pull request! However, we would like to retain copyright by Cognitect for these materials. Thus, we request that you complete the following online agreement to assign copyright to Cognitect before any contribution can be accepted:

[Cognitect Contributor Agreement](https://secure.echosign.com/public/hostedForm?formid=8JU33Z7A7JX84U)

If you have any questions, please contact alex.miller@cognitect.com.

## License

Copyright © 2015 Cognitect

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" property="dct:title" rel="dct:type">Clojure Lab</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="https://github.com/cognitect/clojure-lab" property="cc:attributionName" rel="cc:attributionURL">Cognitect</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

