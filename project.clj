(defproject clojure-lab "0.1.0-SNAPSHOT"
  :description "Interactive materials for learning Clojure"
  :url "https://github.com/cognitect/clojure-lab"
  :plugins [[lein-gorilla "0.3.6"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [gorilla-repl "0.3.6"]]
  :aot [start]
  :main start)
