# Welcome to Clojure!

This repository is designed to be a set of interactive learning materials introducing Clojure.
The materials are suitable for use either individually or in group settings. The content is
delivered using [Gorilla REPL](http://gorilla-repl.org/), which combines text information with
embedded REPLs (interactive Clojure code editors).

The content in this course was developed by Cognitect for Clojure training events held at both
conferences and corporate settings. This particular version of the materials was first used in 
the Clojure/West 2015 Intro to Clojure 2-day course.

Table of contents:

- Syntax
- Functions
- Ordered collections
- Unordered collections
- Flow control
- Namespaces
- Sequences
- Polymorphism
- State

## Run from repository

This is the normal way to interact with these materials.

### Prerequisites

- Install a [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (1.6 or later)
- Install the [leiningen build tool](http://leiningen.org/)

1) Clone this repo

```
git clone git@github.com:cognitect/clojure-lab.git
cd clojure-lab
```

2) Start the gorilla repl server 

```
lein gorilla :port 55555
```

3) Open the lab page in your web browser

[Start](http://127.0.0.1:55555/worksheet.html?filename=src/cljlab/start.clj)

## Creating a standalone package 

If you are teaching a course this may be useful to create a standalone package for distribution to
students.

1) Create the uberjar:

```
rm target
lein uberjar
```

2) Compress the directory into a zip (this may vary per platform):

```
cd ..
zip -r clojure-lab clojure-lab/images clojure-lab/src clojure-lab/target/*-standalone.jar
```

## Running the standalone package

If you are taking a course with a standalone package, this method can be used to
run the materials.

1) Unzip the zip file 

```
unzip clojure-lab.zip
cd clojure-lab
```

2) Start the server

```
java -jar target/clojure-lab-0.1.0-SNAPSHOT-standalone.jar
```

3) View the page in your browser

[Start](http://127.0.0.1:55555/worksheet.html?filename=src/cljlab/start.clj)

## Contributions 

This repository is free for all to use in learning Clojure. You are welcome to use it for any non-commercial use - free public workshops, internal company workshops, etc.

Contributions to this repository are welcome via pull request! However, we would like to retain copyright by Cognitect for these materials. Thus, we request that you complete the following online agreement to assign copyright to Cognitect before any contribution can be accepted:

[Cognitect Contributor Agreement](https://secure.echosign.com/public/hostedForm?formid=8JU33Z7A7JX84U)

If you have any questions, please contact alex.miller@cognitect.com.

## Copyright

Copyright © 2015 Cognitect

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" property="dct:title" rel="dct:type">Clojure Lab</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="https://github.com/cognitect/clojure-lab" property="cc:attributionName" rel="cc:attributionURL">Cognitect</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

