(defproject clojure-lab "0.1.0-SNAPSHOT"
  :description "Interactive materials for learning Clojure"
  :url "https://github.com/cognitect/clojure-lab"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-gorilla "0.3.5-SNAPSHOT"]]
  :dependencies [[org.clojure/clojure "1.7.0-beta1"]
                 [gorilla-repl "0.3.5-SNAPSHOT"]]
  :aot [start]
  :main start)
