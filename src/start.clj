(ns start
  (:require gorilla-repl.core)
  (:gen-class))
  
(defn -main [& args]
  (let [port (Long/parseLong (or (first args) "55555"))]
    (gorilla-repl.core/run-gorilla-server {:port port})
    (println (str "Load: http://127.0.0.1:" port "/worksheet.html?filename=src/cljlab/start.clj"))))